package mitw.bungee.util;

import mitw.bungee.Mitw;
import com.google.common.io.ByteStreams;

import java.io.*;

public class FileUtil {

    public static void saveResource(String filePath, boolean replace) {
        File file = new File(Mitw.INSTANCE.getDataFolder(), filePath);
        if (!file.exists() || replace) {
            final InputStream in = Mitw.class.getResourceAsStream("General.yml");
            try {
                final FileOutputStream out = new FileOutputStream(file);
                ByteStreams.copy(in, out);

                in.close();
                out.close();
            } catch (final FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

}
