package ua.dn.effect.AgentApp.data.store;

/**
 * User: igrebeshkov
 * Date: 04.09.13
 * Time: 10:42
 */

public class StorageException extends Exception{
    public StorageException(){
        super("Cannot initialize storage");
    }
}
