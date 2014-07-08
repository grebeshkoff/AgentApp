package ua.dn.effect.AgentApp.data.store;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import ua.dn.effect.AgentApp.data.model.DataFile;
import ua.dn.effect.AgentApp.data.model.DataFileType;

public abstract class Storage {
    public static List<String> Folders = new ArrayList<String>();
    protected Context currentContext;
    protected File rootFolder;

    protected boolean isReady = false;

    private final String DictionaryFolder = "/dictionaries";
    private final String DictionaryTextFolder = DictionaryFolder + "/txt";
    private final String DictionaryZipFolder = DictionaryFolder + "/zip";

    private final String OrdersFolder = "/dictionaries";
    private final String OrdersTextFolder = OrdersFolder + "/txt";
    private final String OrdersZipFolder = OrdersFolder + "/zip";

    private final String ConfigFolder = "/cfg";

    public Storage(Context context){
        Folders.add(DictionaryFolder);
        Folders.add(DictionaryTextFolder);
        Folders.add(DictionaryZipFolder);
        Folders.add(OrdersFolder);
        Folders.add(OrdersTextFolder);
        Folders.add(OrdersZipFolder);
        Folders.add(ConfigFolder);
        currentContext = context;
    }

    public Context getCurrentContext(){
        return currentContext;
    }

    private File getFolder(String folderName) throws StorageException {
        File folder =  new File(rootFolder.getPath() + folderName);

        if(!Folders.contains(folderName)){
            throw new StorageException();
        }

        if(folder.exists()){
            return folder;
        }
        else{
            throw new StorageException();
        }
    }

    public File getInstance(){
        return rootFolder;
    }
    protected Storage init(){
        try {
            for (String folder : Folders){
                File f = new File(rootFolder.getPath() + folder);
                if(!f.exists()){
                    f.mkdir();
                }
            }
            isReady = true;
            return this;
        }catch (Exception e){
            isReady = false;
            return this;
        }
    }

    public File getZipFile(String name) throws StorageException {
        File folder = getDictionariesZipFolder();
        return getFile(folder, name);
    }

//    public File getTxtFile(DataFileType type) throws StorageException {
//        File folder = getDictionariesTextFolder();
//        return getFile(folder, type);
//    }


    private File getFile(File folder, String  name){
        File result = null;
        for (File f: folder.listFiles()){
            DataFile df = new DataFile(f);

            if (df.type == DataFile.getType(name)){
                if(df.type != DataFileType.PRICE_FILE){
                    result =  f;
                    break;
                }else{
                    if(DataFile.getAcronim(name).equals(df.priceAcronim)){
                        result = f;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public File getDictionariesZipFolder() throws StorageException {
        return getFolder(DictionaryZipFolder);
    }

    public File getDictionariesTextFolder() throws StorageException {
        return getFolder(DictionaryTextFolder);
    }

}
