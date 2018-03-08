package ru.track.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.track.io.vendor.Bootstrapper;
import ru.track.io.vendor.FileEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.BitSet;

import static com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver.length;
import static java.lang.Integer.parseInt;

public final class TaskImplementation implements FileEncoder {

    /**
     * @param finPath  where to read binary data from
     * @param foutPath where to write encoded data. if null, please create and use temporary file.
     * @return file to read encoded data from
     * @throws IOException is case of input/output errors
     */
    @NotNull
    public File encodeFile(@NotNull String finPath, @Nullable String foutPath) throws IOException {

        File fin = new File(finPath);
        InputStream is = new FileInputStream(fin);

        final File fout;

        if (foutPath != null) {
            fout = new File(foutPath);
        } else {
            fout = File.createTempFile("C:\\Users\\Ivan\\Desktop\\based_file_", ".txt");
            fout.deleteOnExit();
        }

        StringBuilder builder = new StringBuilder();
        byte fileContent[] = new byte[(int)fin.length()];

        is.read(fileContent);
        is.close();
        String byt;

        for (byte i: fileContent) {
            byt = Integer.toBinaryString(i&0xFF);
            for (int z = 0; z < 8 - byt.length(); z++)
                builder.append("0");
            builder.append(Integer.toBinaryString(i&0xFF));
        }

        int eq = 0;
        while (builder.toString().length()%6 != 0) {
            builder.append("00000000");
            eq++;
        }
        String bitcode = builder.toString();

        int curr_index = 0;
        StringBuilder base = new StringBuilder();

        for (int t = 0; t != bitcode.length()/6 - eq; t++){
            String current_string = bitcode.substring(curr_index, curr_index+6);
            int a = Integer.parseInt(current_string, 2);

            base.append(toBase64[a]);
            curr_index += 6;
        }

        if (eq != 0){
            if (eq == 1)
                base.append("=");
            else
                base.append("==");
        }


        FileOutputStream writer = new FileOutputStream(fout);
        writer.write(base.toString().getBytes());
        writer.flush();
        writer.close();
        /* XXX: https://docs.oracle.com/javase/8/docs/api/java/io/File.html#deleteOnExit-- */
        return fout;
    }

    private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    public static void main(String[] args) throws Exception {
        final FileEncoder encoder = new TaskImplementation();
        // NOTE: open http://localhost:9000/ in your web browser
        (new Bootstrapper(args, encoder))
                .bootstrap("", new InetSocketAddress("127.0.0.1", 9000));

    }

}