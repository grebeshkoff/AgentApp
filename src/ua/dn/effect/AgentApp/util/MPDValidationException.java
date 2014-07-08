package ua.dn.effect.AgentApp.util;

/**
 * Created by igrebeshkov on 17.04.14.
 */
public class MPDValidationException extends Exception {
    public MPDValidationException(){
        super("MPD file structure is broken");
    }
}
