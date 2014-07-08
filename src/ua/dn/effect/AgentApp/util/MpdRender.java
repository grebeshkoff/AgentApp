package ua.dn.effect.AgentApp.util;

import android.widget.ArrayAdapter;
import org.rendersnake.DocType;
import org.rendersnake.HtmlCanvas;

import java.io.*;
import java.util.ArrayList;

import org.rendersnake.Renderable;
import org.rendersnake.internal.CharactersWriteable;
import ua.dn.effect.AgentApp.data.config.AgentAppConfig;
import ua.dn.effect.AgentApp.data.model.DataFileType;
import ua.dn.effect.AgentApp.data.store.Storage;
import ua.dn.effect.AgentApp.data.store.StorageException;

import static org.rendersnake.HtmlAttributesFactory.*;


public class MpdRender {

    private MpdCash data;
    private File textFile;
    private Storage storage;
    private HtmlCanvas canvas;
    private final String fileName = "mpd";
    private final String fileExtension = ".html";

    public MpdRender(Storage s , File mpdFile){
        storage = s;
        textFile = mpdFile;
        data = new MpdCash(textFile);
    }

    public File renderReport() throws StorageException, IOException {
        clearOldReport();

        writeClientFiles();

        return writeTableOfContent();
    }

    private void writeClientFiles() throws IOException, StorageException {
        for (int i = 0; i < data.getClientsCount(); i++) {
             renderClientFile(i);
        }
    }

    private void renderClientFile(int i) throws IOException, StorageException {
        File resultFile = new File(storage.getDictionariesTextFolder().getAbsolutePath() + "/" + fileName + i + fileExtension);
        resultFile.createNewFile();
        FileWriter fw = new FileWriter(resultFile);
        canvas = new HtmlCanvas();
        renderClientPage(canvas, i);
        String data = canvas.toHtml();
        fw.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        fw.append(data);
        fw.flush();
        fw.close();
    }

    private void renderClientPage(HtmlCanvas canvas, int i) throws IOException, StorageException {

        //canvas.content("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");

        canvas
            .render(DocType.HTML5)
                .html()
                .head()
                    .title().content("Hello")
                    .meta(charset("utf-8"))
                    .meta(name("viewport").content("initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui"))
                    .meta(name("apple-mobile-web-app-capable").content("yes"))
                    .meta(name("apple-mobile-web-app-status-bar-style").content("black"))

                    .link(href("file:///android_asset/www/lib/ratchet/css/ratchet.min.css").rel("stylesheet"))
                    .script(src("file:///android_asset/www/lib/ratchet/js/ratchet.min.js"))
                    ._script()
                ._head()
                .body()
                    .header(class_("bar bar-nav"))
                        .a(href(storage.getDictionariesTextFolder().getAbsolutePath() + "/" + fileName + fileExtension))
                        .button(class_("btn btn-link btn-nav pull-left"))
                            .span(class_("icon icon-left-nav"))._span()
                            .write("Список")
                        ._button()
                        ._a()
                        .h1(class_("title")).content("Отчет")
                    ._header()
                        .div(class_("content"));

                            renderNav(canvas, i, storage);

                            canvas.div(class_("card"))
                                .div(class_("content-padded").style("text-align: center"))
                                    .h5().content(data.getClientHead(i)[0])
                                ._div()
                            ._div()
                            .div(class_("card"))
                                .div(class_("content-padded"))
                                    .p().content(data.getReportDate())
                                    .p().content(data.getReportPeriod())
                                ._div()
                            ._div();

                            ArrayList<ArrayList<String>>docs = data.getClientDocuments(i);
                            for (ArrayList<String> block : docs){
                                renderDocument(canvas, block, false);
                            }

                    renderNav(canvas, i, storage);
                    canvas._div()
                ._body()
                ._html();


    }

    private void renderNav(HtmlCanvas canvas, int i, Storage storage) throws StorageException, IOException {
        canvas.p(class_("content-padded"));
                if(i > 0){
                    canvas.a(href(storage.getDictionariesTextFolder().getAbsolutePath() + "/" + fileName + (i - 1) + fileExtension))
                            .button(class_("btn btn-primary pull-left"))
                            .span(class_("icon icon-left-nav"))._span()
                            .write("Предыдущий")
                            ._button()
                            ._a();
                }

                if(i < data.getClientsCount() - 1){
                    canvas.a(href(storage.getDictionariesTextFolder().getAbsolutePath() + "/" + fileName + (i + 1) + fileExtension))
                            .button(class_("btn btn-primary pull-right"))
                            .write("Следующий")
                            .span(class_("icon icon-right-nav"))._span()
                            ._button()
                            ._a();
                }

                canvas._p()
                .br();
    }

    private void renderDocument(HtmlCanvas canvas, ArrayList<String> block, boolean isOps) throws IOException {
        canvas.div(class_("card"));

        if((!block.get(0).equals(""))||(!block.get(1).equals("")))
        {

            if(!block.get(0).equals("")){           // Total
                canvas
                .div(class_("content-padded")).p()
                    .div().h5().content("Всего: ")._div()
                    .hr()
                    .div().h5().content("На начало:")._div()
                    .div().content("Дебет: " + block.get(4))
                    .div().content("Кредит: " + block.get(5))
                    .hr()
                    .div().h5().content("За период: ")._div()
                    .div().content("Дебет: " + block.get(6))
                    .div().content("Кредит: " + block.get(7))
                    .hr()
                    .div().h5().content("На конец: ")._div()
                    .div().content("Дебет: " + block.get(8))
                    .div().content("Кредит: " + block.get(9))
                    .hr()
                    .div().h5().content("Просрочено: " + block.get(10))._div()._p()
                ._div();
            }else {
                canvas.div(class_("content-padded")).p();//by payment type
                canvas.div().h5().content(block.get(1))._div()
                        .hr()
                        .div().h5().content("На начало: ")._div();
                        canvas.div().content("Дебет: "  + block.get(5));
                        canvas.div().content("Кредит: " + block.get(6));
                        canvas.hr();
                        canvas.div().h5().content("За период: ")._div();
                        canvas.div().content("Дебет: " + block.get(7));
                        canvas.div().content("Кредит: " + block.get(8));
                        canvas.hr();
                        canvas.div().h5().content("На конец: ")._div();
                        canvas.div().content("Дебет: " + block.get(9));
                        canvas.div().content("Кредит: " + block.get(10));
                        canvas.hr();
                        canvas.div().h5().content("Просрочено: " + block.get(11))._div();
                canvas._p()._div();            }


        }  else {
            canvas.div(class_("content-padded")).p();

            canvas.div().content(block.get(3));
            canvas.div().content(block.get(4) + " № " + block.get(2));

            canvas.div().content(block.get(7));
            canvas.div().content(block.get(8));
            canvas._p()._div();
        }


        canvas._div();
    }

    private void clearOldReport() throws StorageException {
        File dir = storage.getDictionariesTextFolder();
        for (File f : dir.listFiles()){
            String name = f.getName();
            if(name.startsWith(fileName) && name.endsWith(fileExtension)){
                f.delete();
            }
        }
    }

    private File writeTableOfContent() throws StorageException, IOException {
        File resultFile = new File(storage.getDictionariesTextFolder().getAbsolutePath() + "/" + fileName + fileExtension);
        resultFile.createNewFile();
        FileWriter fw = new FileWriter(resultFile);
        HtmlCanvas canvas = new HtmlCanvas();
        renderContentPage(canvas);
        String data = canvas.toHtml();
        fw.write(data);
        fw.flush();
        fw.close();
        return resultFile;
    }

    private void renderContentPage(HtmlCanvas html) throws IOException, StorageException {
        html    .render(DocType.HTML5)
                .html()
                .head()
                    .title().content("Hello")
                    .meta(charset("utf-8"))
                    .meta(name("viewport").content("initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui"))
                    .meta(name("apple-mobile-web-app-capable").content("yes"))
                    .meta(name("apple-mobile-web-app-status-bar-style").content("black"))

                    .link(href("file:///android_asset/www/lib/ratchet/css/ratchet.min.css").rel("stylesheet"))
                    .script(src("file:///android_asset/www/lib/ratchet/js/ratchet.min.js"))
                    ._script()
                ._head()
                .body()
                    .header(class_("bar bar-nav"))
                        .h1(class_("title")).content(data.getReportName())
                    ._header()
                    .div(class_("content"))
                        .div(class_("card"))
                            .div(class_("content-padded"))
                                .h5().content("Отчет: " + data.getReportName())
                                .h5().content(data.getReportComment())
                                .h5().content(data.getReportAgent())
                            ._div()
                        ._div()
                        .div(class_("card"))
                            .div(class_("content-padded"))
                                .p().content(data.getReportDate())
                                .p().content(data.getReportPeriod())
                            ._div()
                        ._div();
        renderClientsHeaders(html)
        ._div()
    ._body()
    ._html();
    }

    private HtmlCanvas renderClientsHeaders(HtmlCanvas html) throws IOException, StorageException {

        html.div(class_("card")).ul(class_("table-view"));

        for (int i = 0; i < data.getClientsCount(); i++) {
            String [] clientMetaData = data.getClientHead(i);


            html.li(class_("table-view-cell"))
            .a(class_("push-right").href(storage.getDictionariesTextFolder().getAbsolutePath() + "/" + fileName + i + fileExtension))
                    .strong().content(clientMetaData[0]);
            html.div();
            if(!clientMetaData[1].equals("")){
                html.span(class_("badge badge-positive")).content("+" + clientMetaData[1]);
                html.span().content(" ");
            }

            if(!clientMetaData[2].equals("")){
                html.span(class_("badge badge-negative")).content("-" + clientMetaData[2]);
            }
            html._div()._a()._li();
        }
        html._ul()._div();
        return html;
    }
}
