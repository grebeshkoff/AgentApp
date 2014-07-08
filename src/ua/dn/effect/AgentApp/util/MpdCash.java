package ua.dn.effect.AgentApp.util;

import ua.dn.effect.AgentApp.data.model.DataFileType;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by igrebeshkov on 24.04.14.
 */
public class MpdCash {
    private File textFile;
    private int headerLenght = 7;
    private int clientsCount = 0;
    private String clientSeparator = "!";
    private String blockSeparator = "#";


    private int debtOffset = 9;
    private int creditOffset = 10;

    private int transactionOffset = 11;
    ArrayList<String> strings = new ArrayList<String>();
    private ArrayList<Integer> clientFirstStringNumbers = new ArrayList<Integer>();
    private Integer totalStringsCount = 0;

    public boolean isValid = false;
    
    public MpdCash(File mpdFile){
        textFile = mpdFile;
        isValid = checkTxtData();
    }

    private boolean checkTxtData() {
        try {
            InitStrings();

            int counter = 0;
            for (String s : strings){
                String [] parts = s.split("=", 2);
                if(parts.length == 2){
                    strings.set(counter, parts[1]);
                    counter++;
                }else {
                    break;
                }
            }

            if (counter - 1!= headerLenght) {
                throw new MPDValidationException();
            }

            if (!strings.get(0).equals(DataFileType.REPORT_MPD.abr)){
                throw new MPDValidationException();
            }

            Integer s1 = (totalStringsCount - 1);
            Integer s2 = Integer.parseInt(strings.get(1));
            if (!s1.equals(s2)) {
                throw new MPDValidationException();
            }

            if(!strings.get(strings.size() -1).equals("End")){
                throw new MPDValidationException();
            }
            
            int i;
            for (i = headerLenght + 1; i < totalStringsCount; i++){
                String str = strings.get(i);
                if(!str.equals(blockSeparator)) break;
                i = i + 1;
                str = strings.get(i);
                if(str.equals(clientSeparator)){
                    clientFirstStringNumbers.add(i + 1);
                    clientsCount += 1;
                }
                i += transactionOffset;
            }

            return true;
        } catch (MPDValidationException e){
            AgentAppLogger.Error(e);
            return false;
        } catch (Exception e) {
            AgentAppLogger.Error(e);
            return false;
        }

    }

    private void InitStrings() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(textFile.getAbsolutePath()), "cp1251"));
        String str = "";
        while ((str = br.readLine()) != null) {
            strings.add(str);
        }
        totalStringsCount = strings.size();
    }

    /* Public interface */
    public String getReportName(){
        return strings.get(3);
    }
    
    public String getReportComment(){
        return strings.get(5);
    }
    
    public String getReportAgent(){
        return strings.get(7);
    }
    
    public String getReportDate(){
        return strings.get(6);
    }

    public String getReportPeriod(){
        return strings.get(4);
    }

    public int getClientsCount(){
        return clientsCount;
    }

    public String[] getClientHead(int i) {
        String [] resultSet = new String[3];
        resultSet[0] = strings.get(clientFirstStringNumbers.get(i)).trim();
        resultSet[1] = strings.get(clientFirstStringNumbers.get(i) + debtOffset).trim();
        resultSet[2] = strings.get(clientFirstStringNumbers.get(i) + creditOffset).trim();
        return resultSet;
    }

    public ArrayList<ArrayList<String>> getClientDocuments(int clientNumber){

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        int start = clientFirstStringNumbers.get(clientNumber) ;

        String startString = strings.get(start);

        ArrayList<String> doc = new ArrayList<String>();

        while (!startString.equals(clientSeparator)){

            if(startString.equals("End")){
                return result;
            }

            if(startString.equals(blockSeparator)){
                result.add(doc);
                doc = new ArrayList<String>();
                start++;
                startString = strings.get(start);
                continue;
            }
            doc.add(startString);
            start++;
            startString = strings.get(start);

        }



        return result;
    }

}
