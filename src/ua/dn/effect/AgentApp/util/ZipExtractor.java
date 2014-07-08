package ua.dn.effect.AgentApp.util;

import ua.dn.effect.AgentApp.data.store.InternalStorage;
import ua.dn.effect.AgentApp.data.store.Storage;
import ua.dn.effect.AgentApp.data.store.StorageException;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;


import android.util.Log;

/**
 * User: igrebeshkov
 * Date: 02.09.13
 * Time: 15:47
 */
public class ZipExtractor {

    private Storage storage;

    public ZipExtractor(Storage s) {
        storage = s;
    }

    public void extractAll() throws IOException
    {
        int buffer = 2048;

        try {
            File zipFolder = storage.getDictionariesZipFolder();
            File txtFolder = storage.getDictionariesTextFolder();

            for(File file: txtFolder.listFiles()) file.delete();

            for (File file : zipFolder.listFiles()){

                if (file.getName().endsWith(".zip"))
                {
                    try {
                        ZipFile zipFile = new ZipFile(file);
                        Enumeration zipFileEntries = zipFile.entries();
                        while (zipFileEntries.hasMoreElements())
                        {
                            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();

                            File destFile = new File(txtFolder, entry.getName());
                            BufferedInputStream is = new BufferedInputStream(zipFile
                                    .getInputStream(entry));

                            int currentByte;

                            byte data[] = new byte[buffer];
                            FileOutputStream fos = new FileOutputStream(destFile);
                            BufferedOutputStream dest = new BufferedOutputStream(fos, buffer);

                            while ((currentByte = is.read(data, 0, buffer)) != -1) {
                                dest.write(data, 0, currentByte);
                            }
                            dest.flush();
                            dest.close();
                            is.close();
                        }
                    } catch(ZipException ex){
                       AgentAppLogger.Error(ex);
                    }
                }
                Log.e("AGNT", file.getName());
            }

        }catch (StorageException se){
            Log.e("AGNT", se.getMessage());
        }
    }


    public static boolean isValid(final File file) {
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(file);
            return true;
        } catch (ZipException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (zipfile != null) {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException e) {
            }
        }
    }
}
