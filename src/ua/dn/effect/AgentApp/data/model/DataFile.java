package ua.dn.effect.AgentApp.data.model;

import java.io.File;
import java.util.Date;

/**
 * User: igrebeshkov
 * Date: 31.10.13
 * Time: 12:32
 */

public class DataFile{
    public String name;
    public DataFileType type;
    public Date created;
    public  String priceAcronim = "";
    private File file;

    public DataFile(File file){
        this.file = file;
        created = new Date(file.lastModified());
        name = file.getName();
        setType();
        if(this.type == DataFileType.PRICE_FILE){
            priceAcronim = name.split("_")[1];
        }
    }

    public void setCreated(Date d){
        created = d;
    }

    private void setType(){
        type = getType(name);
    }

    public static  DataFileType getType(String fileName){
        for (DataFileType s : DataFileType.values()){
            if (fileName.startsWith(s.abr + "_")){
                return s;
            }
        }
        return DataFileType.UNDEFINED;
    }

    public static String getAcronim(String str){
        String result = "";
        String [] parts = str.split("_");
        if(parts.length >= 3){
            return parts[1];
        }
        return "";
    }
}
