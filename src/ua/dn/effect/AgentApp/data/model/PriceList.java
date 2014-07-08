package ua.dn.effect.AgentApp.data.model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: igrebeshkov
 * Date: 07.11.13
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public class PriceList {
    public String [] priceTypes;
    public String FileName;
    public ArrayList<EntityGroup> list = new ArrayList<EntityGroup>();
    private boolean isReady = false;

    public int currentPriseTypeId = 0;

    public void setIsReady(boolean val){
        isReady = val;
    }

    public boolean getIsReady(){
        return isReady;
    }

    public void addGroup(EntityGroup eg){
        list.add(eg);
    }

    public Entity getEntityById(int id){
        for (EntityGroup eg : list){
            for (Entity e : eg.entry) {
                if(e.id == id){
                    return e;
                }
            }
        }
        return null;
    }
}
