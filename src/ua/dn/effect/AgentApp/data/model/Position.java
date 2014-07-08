package ua.dn.effect.AgentApp.data.model;

import ua.dn.effect.AgentApp.AgentApplication;

/**
 * User: igrebeshkov
 * Date: 25.10.13
 * Time: 15:53
 */
public class Position {
    public Entity entity;
    public int count1;
    public int count2;
    public int priceTypeId1;
    public int priceTypeId2;
    public PaymentType paymentType1;
    public PaymentType paymentType2;

    @Override
    public String toString() {
        return entity.name + " [" + (count1 + count2) + "]";
    }
    public String getPriceTypeName(int id){
        return AgentApplication.priceList.priceTypes[id];
    }
    public int getPriceTypeId(String name){
        int i = 0;
        for (String val : AgentApplication.priceList.priceTypes){
            if (val.equals(name)){
                return i;
            }
            i++;
        }
        return 0;
    }
}
