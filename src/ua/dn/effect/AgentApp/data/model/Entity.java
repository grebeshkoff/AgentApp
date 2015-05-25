package ua.dn.effect.AgentApp.data.model;

import ua.dn.effect.AgentApp.AgentApplication;
import ua.dn.effect.AgentApp.data.config.AgentAppConfig;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

/**
 * User: igrebeshkov
 * Date: 31.10.13
 * Time: 12:12
 */
public class Entity {
    public int id;
    public String name;
    public int countInBox;
    public Double minSale;
    public Double inWarehouse;
    public String action;
    public String repricing;

    ArrayList<Double> prices = new ArrayList<Double>();
    public Entity(int i, String n){
        id = i;
        name = n;
    }
    public void addPrice(Double pr){
        prices.add(pr);
    }

    @Override
    public String toString(){
        return name;
    }

    public String getNameWithHistory(int clientId, int groupId, final SalesHistory sh){
        if(clientId > 0){

            HashMap<Integer, SaleGroup> clientSales = AgentApplication.salesHistory.list.get(clientId);
            if(clientSales != null){
                SaleGroup group = clientSales.get(groupId);
                if(group != null){
                    for (Sale s :group.sales){
                        if(s.entityId == this.id){
                            return s.saleCount + " " + name;
                        }
                    }
                }
            }
        }

        return name;
    }

    public String getPriceText(int priceTypeId) {
        return "Цена: " + prices.get(priceTypeId) + " " + AgentAppConfig.CURRENCY_ABR;
    }
    public Double getPriceValue(int priceTypeId) {
        return prices.get(priceTypeId);
    }
}
