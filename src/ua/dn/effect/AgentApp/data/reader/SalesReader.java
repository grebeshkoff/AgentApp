package ua.dn.effect.AgentApp.data.reader;

import android.widget.Toast;
import ua.dn.effect.AgentApp.data.model.*;
import ua.dn.effect.AgentApp.data.store.Storage;
import ua.dn.effect.AgentApp.data.store.StorageException;
import ua.dn.effect.AgentApp.util.AgentAppLogger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by igrebeshkov on 27.03.14.
 */
public class SalesReader {



    public static SalesHistory getHistory(Storage storage){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        SalesHistory history = new SalesHistory();

        String path = null;
        try {
            path = storage.getDictionariesTextFolder().getAbsolutePath() + "/";
        } catch (StorageException e) {
            return history;
        }

        File f = new File(path);

        ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));
        File salesHistoryFile = new File("zzzzzzzzzzz");
        for(File file : files){
            DataFile df = new DataFile(file);
            if (df.type == DataFileType.SALES_HISTORY){
                salesHistoryFile = new File(path + df.name);
                AgentAppLogger.Text(salesHistoryFile.getAbsolutePath());
            }
        }

        if(salesHistoryFile.exists()){

            BufferedReader br = null;
            try {
                br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(salesHistoryFile.getAbsolutePath()), "cp1251"));
            } catch (UnsupportedEncodingException e) {
                AgentAppLogger.Error(e);
                return history;
            } catch (FileNotFoundException e) {
                AgentAppLogger.Error(e);
                return history;
            }

            String str;

            try {
                while ((str = br.readLine()) != null) {

                    if (str.equals("#")){ // New client
                        str = br.readLine();
                        int clientId = Integer.parseInt(str);

                        str = br.readLine();
                        int groupsCount = Integer.parseInt(str);

                        HashMap<Integer, SaleGroup> clientSales = new HashMap<Integer, SaleGroup>();

                        for (int i = 0; i < groupsCount; i++){ // Group loop
                            str = br.readLine();
                            String [] parts = str.split("\\t");
                            SaleGroup sg = new SaleGroup();
                            int groupId = Integer.parseInt(parts[0]);
                            sg.groupSaleCount = parts[2];
                            str = br.readLine();
                            int salesCount = Integer.parseInt(str);
                            for(int j = 0; j < salesCount; j ++){
                                str = br.readLine();
                                String [] saleParts = str.split("\\t");
                                Sale s = new Sale();
                                s.entityId = Integer.parseInt(saleParts[0]);
                                try {
                                    s.saleDate = dateFormat.parse(saleParts[1]);
                                } catch (ParseException e) {
                                    s.saleDate = new Date(1900, 01, 01);
                                }
                                s.saleCount = saleParts[2];
                                sg.sales.add(s);
                            }
                            clientSales.put(groupId, sg);
                        }
                        history.list.put(clientId, clientSales);
                    }
                }
                br.close();
            } catch (IOException e) {
                AgentAppLogger.Error(e);
                return history;
            }
        }

        AgentAppLogger.Text(salesHistoryFile.getName());
        return history;
    }

}
