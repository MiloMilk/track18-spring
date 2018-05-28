package ru.track.prefork;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    static Logger log = LoggerFactory.getLogger(Client.class);

    private int port;
    private String host;

    public Client(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public class SendMessage extends Thread {
        Socket socket;

        SendMessage(Socket socket){

            this.socket = socket;
            start();
        }

        public void run(){
            final OutputStream out;

            try{
                out = socket.getOutputStream();
                Scanner scanner = new Scanner(System.in);

                while (true) {
                    //System.out.println("ReadMessager");
                    String line = scanner.nextLine();
                    if ("q".equals(line)) {
                        break;
                    }
                    out.write(line.getBytes());
                    out.flush();
                }
                out.close();

            } catch (IOException e){
               //----
            }
        }
    }

    public class GetMessage extends Thread {
        Socket socket;

        GetMessage(Socket socket){

            this.socket = socket;
            start();
        }

        public void run(){
            InputStream in = null;

            try{
                in = socket.getInputStream();
                Integer s;
                //Scanner scanner = new Scanner(in);

                while (true) {
                    //String line = scanner.nextLine();

                    byte [] by = new byte[1024];
                    s = in.read(by);
                    System.out.println(new String(by));

                    if (s==-1){
                        System.out.println("Вас отключили");
                        break;
                    }
                }


            } catch (IOException e){
                //----
                try {
                    in.close();
                } catch (Exception z){
                    //
                }
            }
        }

    }

    public void loop() throws Exception {
        Socket socket = new Socket(host, port);
        SendMessage send = new SendMessage(socket);
        GetMessage get = new GetMessage(socket);
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(9000, "localhost");
        client.loop();
    }
}
