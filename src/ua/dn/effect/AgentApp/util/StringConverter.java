package ua.dn.effect.AgentApp.util;

import java.io.*;

/**
 * Created by igrebeshkov on 17.04.14.
 */
public class StringConverter {
    private static String convertStreamToString(InputStream is, String encoding) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (final File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        //String result = new String(convertStreamToString(fin).getBytes(), "cp1251");
        String result = "";
        if (!file.getName().equals("mpd.html")){
            result = convertStreamToString(fin, "cp1251");

        }else
        {
            result = convertStreamToString(fin, "utf-8");
        }
        fin.close();
        return result;
    }
}
