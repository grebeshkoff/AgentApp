package ua.dn.effect.AgentApp.data.reader;

import android.content.res.AssetManager;
import org.rendersnake.HtmlCanvas;
import ua.dn.effect.AgentApp.data.model.DataFile;
import ua.dn.effect.AgentApp.data.model.DataFileType;
import ua.dn.effect.AgentApp.data.model.Report;
import ua.dn.effect.AgentApp.data.store.Storage;
import ua.dn.effect.AgentApp.data.store.StorageException;
import ua.dn.effect.AgentApp.util.AgentAppLogger;
import ua.dn.effect.AgentApp.util.DateCoder;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import ua.dn.effect.AgentApp.util.MpdRender;

/**
 * Created by igrebeshkov on 16.04.14.
 */
public class ReportReader {
    static List<Report> getHtmlReports(Storage storage){

        ArrayList<Report>  list = new ArrayList<Report>();

        String path = null;
        try {
            path = storage.getDictionariesTextFolder().getAbsolutePath() + "/";
        } catch (StorageException e) {
            return null;
        }

        if(path == null)
            return null;

        File dictionaryFolder = new File(path);
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(dictionaryFolder.listFiles()));

        for(File file : files){
            DataFile df = new DataFile(file);
            if (df.type == DataFileType.REPORT_NZ){
                Report result = new Report();
                result.name = Report.FAILED_ORDERS;
                result.file = new File(path + df.name);
                result.date = DateCoder.getDateFromFileName(result.file, ".htm");
                list.add(result);
            }
            if (df.type == DataFileType.REPORT_PLAN){
                Report result = new Report();
                result.name = Report.PLAN;
                result.file = new File(path + df.name);
                result.date = DateCoder.getDateFromFileName(result.file, ".htm");
                list.add(result);
            }
            if (df.type == DataFileType.REPORT_PD){
                Report result = new Report();
                result.name = Report.PD;
                result.file = new File(path + df.name);
                result.date = DateCoder.getDateFromFileName(result.file, ".htm");
                list.add(result);
            }
            if (df.type == DataFileType.REPORT_PP){
                Report result = new Report();
                result.name = Report.PP;
                result.file = new File(path + df.name);
                result.date = DateCoder.getDateFromFileName(result.file, ".htm");
                list.add(result);
            }
        }

        return list;
    }

    static Report getTestReport(){
        Report result = new Report();
        result.name = "Test";
        result.date  = new Date();
        result.file = null;
        return result;
    }

    static Report getMPDReport(Storage storage){
        try {
            String path = null;
            try {
                path = storage.getDictionariesTextFolder().getAbsolutePath() + "/";
            } catch (StorageException e) {
                return null;
            }

            if(path == null)
                return null;

            File dictionaryFolder = new File(path);
            ArrayList<File> files = new ArrayList<File>(Arrays.asList(dictionaryFolder.listFiles()));
            Report txt = new Report();
            for(File file : files){
                DataFile df = new DataFile(file);
                if (df.type == DataFileType.REPORT_MPD){
                    txt.name = Report.MPD_ORDERS;
                    txt.file = new File(path + df.name);
                    txt.date = DateCoder.getDateFromFileName(txt.file, ".txt");
                    break;
                }
            }

            Report result = new Report();
            MpdRender render = new MpdRender(storage, txt.file);
            result.name = txt.name;
            result.date = txt.date;
            result.file = render.renderReport();
            return result;

        } catch (Exception e) {
            AgentAppLogger.Error(e);
            return null;
        }
    }

    public static List<Report> getReportsList(Storage storage){
        List<Report> result  = new ArrayList<Report>();

        result.addAll(getHtmlReports(storage));

        Report rep = getMPDReport(storage);
        if (rep != null)
            result.add(getMPDReport(storage));

        return result;
    }


}
