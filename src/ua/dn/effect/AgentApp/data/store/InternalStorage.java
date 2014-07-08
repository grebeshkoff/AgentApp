package ua.dn.effect.AgentApp.data.store;

import android.content.Context;
import java.io.File;

/**
 * User: igrebeshkov
 * Date: 04.09.13
 * Time: 10:42
 */

public class InternalStorage extends Storage {
    public InternalStorage(Context context) throws StorageException{
            super(context);
            rootFolder = context.getFilesDir();
            if (!init().isReady){
                throw new StorageException();
            };
    }
}
