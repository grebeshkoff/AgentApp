/**
 * User: igrebeshkov
 * Date: 05.09.13
 * Time: 0:45
 */
package ua.dn.effect.AgentApp.data.reader;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import ua.dn.effect.AgentApp.AgentApplication;
import ua.dn.effect.AgentApp.data.exchenge.FtpFileNameHelper;
import ua.dn.effect.AgentApp.data.exchenge.FtpFilesNotCheckedException;
import ua.dn.effect.AgentApp.data.model.Client;
import ua.dn.effect.AgentApp.data.model.DataFile;
import ua.dn.effect.AgentApp.data.model.DataFileType;
import ua.dn.effect.AgentApp.data.model.TradeAgent;
import ua.dn.effect.AgentApp.data.store.Storage;
import ua.dn.effect.AgentApp.data.store.StorageException;
import ua.dn.effect.AgentApp.util.AgentAppLogger;
import ua.dn.effect.AgentApp.util.DateCoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientsReader {

    //TODO Init value
    private File agentClientsFile;
    private Storage storage;
    private String clientParamPrefix = "8=";


    Context ctx;

    public ClientsReader(Storage s) throws StorageException {
        storage = s;
        String path = storage.getDictionariesTextFolder().getAbsolutePath() + "/";
        File f = new File(path);

        ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));

        for(File file : files){
            DataFile df = new DataFile(file);
            if (df.type == DataFileType.CLIENTS_FILE){
                agentClientsFile = new File(path + df.name);
                AgentAppLogger.Text(agentClientsFile.getAbsolutePath());
            }
        }


//        try {
//            agentClientsFile = new File(storage.getDictionariesTextFolder().getAbsolutePath() + "/" + AgentApplication.getAgentZipFile());
//        } catch (FtpFilesNotCheckedException e) {
//            AgentAppLogger.Error(e);
//        }
//        ctx = storage.getCurrentContext();
//        Log.e("AGNT", agentClientsFile.getAbsolutePath());
    }



    public List<Client> getList() {
        ArrayList<Client> list = new ArrayList<Client>();
        try{
            if(!agentClientsFile.exists()){
                Toast.makeText(ctx, "Данные о клиентах не загружены", Toast.LENGTH_LONG);
            }else{
                BufferedReader br =     new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(agentClientsFile.getAbsolutePath()), "cp1251"));

                int i = 0;
                String str;

                ArrayList<String> params = new ArrayList<String>();

                while ((str = br.readLine()) != null) {
                    if(str.startsWith("6=")){
                        Date d = new Date(agentClientsFile.lastModified());
                        String date = DateCoder.encryptDate(str.split("=")[1], false);
                        Date correction = new SimpleDateFormat("MMddHHmm").parse(date);
                        correction.setYear(d.getYear());
                        AgentApplication.controlDate = correction;
                        str = br.readLine();
                    }
                    if(str.equals("#")){
                        int id = Integer.parseInt(br.readLine());
                        String name = br.readLine();
                        String [] paramsValues  = br.readLine().split(";");

                        HashMap<String, String> p = new HashMap<String, String>();

                        p.put(params.get(0), paramsValues[0].split("=")[1]);
                        p.put(params.get(1), paramsValues[1].split("=")[1]);
                        p.put(params.get(2), paramsValues[2].split("=")[1]);
                        p.put(params.get(3), paramsValues[3].split("=")[1]);

                        br.readLine();

                        Client client = new Client(id, name);
                        client.setParams(p);
                        list.add(client);
                    } else{
                        if(str.startsWith(clientParamPrefix)){
                            String [] s = str.split("=");
                            params.add(s[s.length - 1]);
                        }
                    }
                    i++;
                }
                list.add(0, new Client(-1, Client.NEW_CLIENT));
            }
        }catch (FileNotFoundException fne){
            Log.e("AGNT", fne.getMessage());
        }catch (UnsupportedEncodingException e) {
            Log.e("AGNT", e.getMessage());
        }
        catch (IOException ioe){
            Log.e("AGNT", ioe.getMessage());
        }
        catch (Exception e){
            Log.e("AGNT", e.getMessage());
        }
        return list;
    }

    public TradeAgent getTradeAgent(){
        //TODO Read from AK_FILE
        return null;
    }
}
