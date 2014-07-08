package ua.dn.effect.AgentApp.data.reader;

import android.widget.ArrayAdapter;
import ua.dn.effect.AgentApp.AgentApplication;
import ua.dn.effect.AgentApp.data.exchenge.FtpFilesNotCheckedException;
import ua.dn.effect.AgentApp.data.model.*;
import ua.dn.effect.AgentApp.data.store.Storage;
import ua.dn.effect.AgentApp.data.store.StorageException;
import ua.dn.effect.AgentApp.util.AgentAppLogger;

import java.io.*;
import java.util.*;

/**
 * User: igrebeshkov
 * Date: 31.10.13
 * Time: 12:38
 */
public class EntitiesReader {
    HashMap<String, File> priceListFileCollection = new HashMap<String, File>();
    PriceList priceList = new PriceList();

    Storage storage;

    public EntitiesReader(Storage s) throws StorageException{
        storage = s;
        String path = storage.getDictionariesTextFolder().getAbsolutePath() + "/";

        File f = new File(path);

        ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));

        for(File file : files){
            DataFile df = new DataFile(file);
            if (df.type == DataFileType.PRICE_FILE){
                String name = file.getName();
                String acronim = name.split("_")[1];
                priceListFileCollection.put(acronim, file);
                //AgentAppLogger.Text(acronim + " -> " + file.getAbsolutePath());
            }
        }
    }

    public PriceList getList() throws FileNotFoundException {
        priceList.setIsReady(false);
        File priceFile =  priceListFileCollection.get(AgentApplication.tradeAgent.getDefaultPriceAcronim());

        if(priceFile == null){
            return priceList;
        }

        if(!priceFile.exists()){
            return priceList;
        }

        BufferedReader br = null;
        try {


            br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(priceFile.getAbsolutePath()), "cp1251"));
            String str;
            str = br.readLine();
            str = br.readLine();
            str = br.readLine();
            String [] priceTypes = str.split("=")[1].split("#");
            priceList.priceTypes = priceTypes;

            EntityGroup currentGroup = null;
            Date date = new Date();
            AgentAppLogger.Text("!!!!!!!!!!!!!!" + date.toString());
            while ((str = br.readLine()) != null) {


                if (str.equals("#")){
                    int id = Integer.parseInt(br.readLine());
                    String name = br.readLine();

                    if(id == 0){
                        String secondId = br.readLine();
                        id = Integer.parseInt(secondId.substring(1));
                        EntityGroup group = new EntityGroup(id, name);
                        //AgentAppLogger.Text(group.name);
                        this.priceList.addGroup(group);
                        //adapter.add(group);
                        //adapter.notifyDataSetChanged();
                        currentGroup = group;

                    }else {
                        Entity entity = new Entity(id, name);
                        entity.countInBox = Integer.parseInt(br.readLine());
                        entity.minSale = Integer.parseInt(br.readLine());
                        entity.inWarehouse = Double.parseDouble(br.readLine().replace(",", "."));


                        if(AgentApplication.ftpConnection.Host.equals("81.21.3.12") || (AgentApplication.ftpConnection.Host.equals("192.168.2.100"))){
                            br.readLine();
                            br.readLine();
                            br.readLine();
                            br.readLine();
                            entity.action = br.readLine();
                            entity.repricing = br.readLine();
                        }  else {
                            entity.action = "";
                            entity.repricing = "";
                        }

                        for(String type : priceList.priceTypes){
                            String p = br.readLine();

                            if(p.length()!=0){
                                entity.addPrice(Double.parseDouble(p.replace(",", ".")));
                            }else{
                                entity.addPrice(0.0);
                            }
                        }

                        currentGroup.addEntity(entity);
                        //AgentAppLogger.Text(entity.name);
                    }
                }


            }
            date = new Date();
            AgentAppLogger.Text("!!!!!!!!!!!!!!" + date.toString());
        } catch (UnsupportedEncodingException e) {
            AgentAppLogger.Error(e);
        } catch (IOException ie){
            AgentAppLogger.Error(ie);
        } catch (Exception e){
            AgentAppLogger.Error(e);
        }
        priceList.FileName = priceFile.getName();
        priceList.setIsReady(true);
        return priceList;
    }
}
