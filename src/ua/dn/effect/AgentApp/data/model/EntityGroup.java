package ua.dn.effect.AgentApp.data.model;

import ua.dn.effect.AgentApp.AgentApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: igrebeshkov
 * Date: 06.11.13
 * Time: 15:40
 */
public class EntityGroup {
    public int id;
    public String name;
    public ArrayList<Entity> entry = new ArrayList<Entity>();

    public EntityGroup(int i, String n){
        id = i;
        name = n;
    }
    public void addEntity(Entity e){
        entry.add(e);
    }

    @Override
    public String toString() {
        if(AgentApplication.currentOrder.client.getId() > 0){
            int clientId = AgentApplication.currentOrder.client.getId();
            HashMap<Integer, SaleGroup> clientSales = AgentApplication.salesHistory.list.get(AgentApplication.currentOrder.client.getId());
            if(clientSales != null){
                SaleGroup group = clientSales.get(this.id);
                if(group != null){
                    return group.groupSaleCount + " " + name;
                }
            }
        }

        return name;
    }
}
