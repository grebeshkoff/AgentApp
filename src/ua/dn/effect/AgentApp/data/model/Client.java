package ua.dn.effect.AgentApp.data.model;

import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * User: igrebeshkov
 * Date: 05.09.13
 * Time: 13:15
 */
public class Client {

    public static final String NEW_CLIENT = "НОВЫЙ КЛИЕНТ";
    private int id;
    private String name;
    private String realName = "";
    private Map<String, String> params = new HashMap<String, String>();

    public Client(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Client(String [] source) {
    }

    public Client() {
        id = 0;
        name = "...";
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public void setParams(Map<String, String> p ){
        params = p;
    }

    public Map<String, String> getParams(){
        return params;
    }

    @Override
    public String toString(){

        if (name.equals(Client.NEW_CLIENT)){
            if(realName.equals(""))
                return this.name;
            else
                return this.name + " (" + realName + ")";
        }else{
            return this.name;
        }

    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
