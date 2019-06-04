package cc.flycode.loader.downloader;

import lombok.Getter;
import cc.flycode.loader.system.Loader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Getter
public class FileDownloader {

    private URL link;
    private File root;

    public FileDownloader(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.root = new File(System.getProperty("java.io.tmpdir"));

    }

    public FileDownloader(String link, File root) {
        this(link);
        this.root = root;
    }

    public File download() {
        InputStream inputStream = null;
        File file = null;
        FileOutputStream fos = null;
        try {
            file = File.createTempFile(Loader.prefix, ".jar"); // new RandomAccessFile(new File(root, "/wct62HD.jar"), "rw")
            fos = new FileOutputStream(file);
            URLConnection urlConn = link.openConnection();
            inputStream = urlConn.getInputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0)
                fos.write(buffer, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

}
