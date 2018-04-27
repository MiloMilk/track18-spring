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

        final File fout;

        if (foutPath != null) {
            fout = new File(foutPath);
        } else {
            fout = File.createTempFile("based_file_", ".txt");
            fout.deleteOnExit();
        }

        StringBuilder builder = new StringBuilder();
        byte[] i = new byte[3];
        String byt;
        OutputStream out = null;


        try(InputStream is = new FileInputStream(fin)) {
            int res = (int) fin.length() % 3;
            int num = (int) fin.length()/3;
            int c;

            out = new FileOutputStream(fout);

            while (num>0){
                is.read(i);
                num --;
                c = (int)((i[0]&0xFF)>>2);
                out.write(toBase64[c]);

                c = (int) ( (i[0]&0xFF>>6)<<4) + ((i[1]&0xFF)>>4);
                out.write(toBase64[c]);

                c = (int) ((i[1]&0xFF>>4)<<2) + ((i[2]&0xFF)>>6);
                out.write(toBase64[c]);

                c = (int)(i[2]&0xFF>>2);
                out.write(toBase64[c]);
                out.flush();
            }

            if (res==1){
                is.read(i);
                c = (int)((i[0]&0xFF)>>2);
                out.write(toBase64[c]);

                c = (int) ((i[0]&0xFF>>6)<<4);
                out.write(toBase64[c]);
                out.write('=');
                out.write('=');
            }

            if (res==2){
                is.read(i);
                c = (int)((i[0]&0xFF)>>2);
                out.write(toBase64[c]);

                c = (int) ( (i[0]&0xFF>>6)<<4) + ((i[1]&0xFF)>>4);
                out.write(toBase64[c]);

                c = (int) ((i[1]&0xFF>>4)<<2);
                out.write(toBase64[c]);
                out.write('=');
            }

        } catch (Exception e) {
            if(out!=null){
                out.close();
            }
        }
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