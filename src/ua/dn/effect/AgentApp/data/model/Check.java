package ua.dn.effect.AgentApp.data.model;

/**
 * Created by igrebeshkov on 24.06.14.
 */
public class Check {
    public TradeAgent agent;
    public Client client = new Client();
    public Client tempClient  = new Client();

    public void CommitTempClient(){
        client = tempClient;
        tempClient = new Client();
    }

    public String number;
    public double sum;
}
