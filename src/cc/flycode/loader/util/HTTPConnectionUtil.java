package cc.flycode.loader.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by FlyCode on 03/08/2018 Package cc.flycode.BNCP.Utils
 */
public class HTTPConnectionUtil {
    public static String getResponse(String URL) {
        String out;
        try {
            URLConnection connection = new URL(URL).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            String value = sb.toString();
            out = value;
        } catch (IOException ex) {
            out = "[ERROR] - " + ex.getMessage();
        }
        return out;
    }
}
