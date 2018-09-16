package me.skymc.taboolib.util;

import com.ilummc.tlib.util.IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * @Author sky
 * @Since 2018-08-28 15:01
 */
public class FileUtils {

    /**
     * 获取插件资源文件
     *
     * @param target   类
     * @param filename 文件名
     * @return {@link InputStream}
     */
    public static InputStream getResource(Class target, String filename) {
        try {
            URL url = target.getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            } else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (IOException ignored) {
            return null;
        }
    }

    /**
     * 写入文件
     *
     * @param inputStream 输入流
     * @param file        文件
     */
    public static void inputStreamToFile(InputStream inputStream, File file) {
        try {
            String text = new String(IO.readFully(inputStream), Charset.forName("utf-8"));
            FileWriter fileWriter = new FileWriter(FileUtils.createNewFile(file));
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * 检测文件并创建
     *
     * @param file 文件
     */
    public static File createNewFile(File file) {
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ignored) {
            }
        }
        return file;
    }

    /**
     * 检测文件并创建（目录）
     *
     * @param file 文件
     */
    public static void createNewFileAndPath(File file) {
        if (!file.exists()) {
            String filePath = file.getPath();
            int index = filePath.lastIndexOf(File.separator);
            String folderPath;
            File folder;
            if ((index >= 0) && (!(folder = new File(filePath.substring(0, index))).exists())) {
                folder.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

}
