package ua.dn.effect.AgentApp.data.model;

import ua.dn.effect.AgentApp.util.AgentAppLogger;

import java.io.*;


/**
 * User: igrebeshkov
 * Date: 28.08.13
 * Time: 9:18
 */

public class TradeAgent {

    private int tradeAgentId;
    private String tradeAgentName;
    private String defaultPriceAcronim;
    private File agentFile;
    public boolean isReady =false;
    //private String login;
    //private String Password;

    public TradeAgent(){

        File dir = new File("/data/data/ua.dn.effect.AgentApp/files/dictionaries/txt/");
        this.agentFile = null;

        if (dir.exists()){
            for (File f : dir.listFiles()){
                if (f.getName().startsWith(DataFileType.CLIENTS_FILE.abr)){
                    this.agentFile = f;
                    break;
                }
            }
        }

        if(this.agentFile == null){
            this.isReady = false;
            return;
        }

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(agentFile.getAbsolutePath()), "cp1251"));

            String str;
            str = br.readLine();

            while (str != null){

                if(str.startsWith("2=")){
                    this.setTradeAgentId(Integer.parseInt(str.split("=")[1]));
                    str = br.readLine();
                }

                if(str.startsWith("3=")){
                    this.setTradeAgentName(str.split("=")[1]);
                    str = br.readLine();
                }

                if(str.startsWith("4=")){
                    this.setDefaultPriceAcronim(str.split("=")[1]);
                    isReady = true;
                    return;
                }
                str = br.readLine();
            }
        }catch (FileNotFoundException e){
            AgentAppLogger.Error(e);
        } catch (UnsupportedEncodingException e) {
            AgentAppLogger.Error(e);
        } catch (IOException e) {
            AgentAppLogger.Error(e);
        }

    }

    public int getTradeAgentId() {
        return tradeAgentId;
    }

    public void setTradeAgentId(int tradeAgentId) {
        this.tradeAgentId = tradeAgentId;
    }

    public String getTradeAgentName() {
        return tradeAgentName;
    }

    public void setTradeAgentName(String tradeAgentName) {
        this.tradeAgentName = tradeAgentName;
    }

    public String getDefaultPriceAcronim() {
        return defaultPriceAcronim;
    }

    public void setDefaultPriceAcronim(String defaultPriceAcronim) {
        this.defaultPriceAcronim = defaultPriceAcronim;
    }
}


