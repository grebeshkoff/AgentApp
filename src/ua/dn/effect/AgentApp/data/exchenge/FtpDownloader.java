package ua.dn.effect.AgentApp.data.exchenge;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

import org.apache.commons.net.ftp.*;
import ua.dn.effect.AgentApp.AgentApplication;
import ua.dn.effect.AgentApp.data.model.DataFile;
import ua.dn.effect.AgentApp.data.model.DataFileType;
import ua.dn.effect.AgentApp.data.store.InternalStorage;
import ua.dn.effect.AgentApp.data.store.StorageException;
import ua.dn.effect.AgentApp.util.*;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.io.*;

/**
 * User: igrebeshkov
 * Date: 28.08.13
 * Time: 12:49
 */
public class FtpDownloader {

    private FTPClient ftp = new FTPClient();
    private InternalStorage storage;
    private String localFolder;
    private ArrayList<FTPFile> filesList = new ArrayList<FTPFile>();
    private FtpConnection connection;
    public boolean isReady = false;


    public FtpDownloader(FtpConnection con, InternalStorage s) throws StorageException {
        this.connection = con;
        if (checkConnection(this.connection)) return;
        storage = s;
        localFolder = storage.getDictionariesZipFolder().getAbsolutePath() + "/";
        getFilesList();
    }

    private boolean checkConnection(FtpConnection connection) {
        try  {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(connection.Host, connection.Port), 5000);
            isReady = true;
        } catch (IOException ex) {
            return true;
        } catch (Exception e){
            return true;
        }
        return false;
    }

    public void downloadFile() {
        for (FTPFile f : filesList){
            DataFileType fileType = DataFile.getType(f.getName());
            if(fileType != DataFileType.UNDEFINED){
                String fileName = f.getName();
                try {
                    File oldFile = storage.getZipFile(fileName);
                    if (oldFile != null){
                        if((oldFile.getName().equals(fileName))&&(ZipExtractor.isValid(oldFile))){
                            continue;
                        }
                    }

                    FileOutputStream fos;
                    fos = new FileOutputStream(localFolder + fileName);
                    InputStream is = this.ftp.retrieveFileStream(fileName);
                    IOUtils.copy(is,   fos);
                    fos.flush();
                    IOUtils.closeQuietly(fos);
                    IOUtils.closeQuietly(is);
                    boolean commandOK = ftp.completePendingCommand();
                    File test = new File(localFolder + fileName);
                    if(oldFile != null){
                        if(ZipExtractor.isValid(test)) {
                            AgentAppLogger.Text(oldFile.getName());
                            oldFile.delete();
                        }
                    }

                } catch (IOException e) {
                    disconnect();
                    AgentAppLogger.Error(e);
                } catch (StorageException e) {
                    disconnect();
                    AgentAppLogger.Error(e);
                }
            }
        }
        disconnect();
    }

    private void getFilesList(){
        filesList.clear();
        try {
            int reply;
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            ftp.setRemoteVerificationEnabled(false);
            ftp.connect(connection.Host, connection.Port);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
            ftp.enterLocalPassiveMode();
            ftp.login(connection.UserName, connection.UserPassword);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            FTPFile [] list =  ftp.listFiles();

            for (FTPFile f : list){
                String name = f.getName();
                if((!name.equals("."))&&(!name.equals(".."))){
                    filesList.add(f);
                }
            }
        }catch (IOException ioe){
            disconnect();
            AgentAppLogger.Error(ioe);
        }
    }

    public void disconnect() {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                AgentAppLogger.Error(f);
            }
        }
    }
}
