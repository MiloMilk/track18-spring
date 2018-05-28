package ru.track.prefork;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * - multithreaded +
 * - atomic counter +
 * - setName() +
 * - thread -> Worker +
 * - save threads
 * - broadcast (fail-safe)
 */
public class Server {
    static Logger log = LoggerFactory.getLogger(Server.class);
    int current_id = 0;
    volatile Map<String, Socket> list = new HashMap<>();

    private int port;
    public Server(int port) {
        this.port = port;
    }

    public void serve() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port, 10, InetAddress.getByName("localhost"));

        try {
            while (true) {
                //log.info("on select...");
                String name = "Client[" + current_id + "]@" + InetAddress.getByName("localhost") + ":" + port;
                Socket socket = serverSocket.accept();
                list.put(name, socket);

                MyThread newclient = new MyThread(socket, current_id);
                newclient.setName(name);
                newclient.start();

                current_id++;

            }
        } catch (Exception e) {
            serverSocket.close();
            System.out.println("Server off");
        }
    }

    public class MyThread extends Thread {

        Socket socket = null;
        int id;
        OutputStream out = null;
        String message;

        MyThread(Socket socket, int id){
            this.socket = socket;
            this.id = id;
        }

        public void run(){

            while(true){

                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] buf = new byte[1024];
                    int nRead = inputStream.read(buf);
                    //message = this.getName()+" "+new String(buf, 0, nRead);
                    message = new String(buf, 0, nRead);

                    if (message.equals("list")){
                        System.out.println("Отправляем админский запрос");
                        Integer flag0 = 0;
                        OutputStream out = this.socket.getOutputStream();
                        for (Map.Entry i: list.entrySet()){
                            if (!this.socket.equals(i.getValue())) {
                                String z = (String) i.getKey();
                                out.write(z.getBytes());
                                out.flush();
                                flag0++;
                            }
                        }
                        if (flag0 == 0){
                            out.write("Only you connected".getBytes());
                            out.flush();
                        }
                    }

                    if (message.split(" ")[0].equals("Drop")){
                        Integer flag1 = 0;
                        for (Map.Entry i: list.entrySet()){
                            String currID = i.getKey().toString().split("[\\[|\\]]")[1];
                            if (currID.equals(message.split(" ")[1])) {
                                Socket t = (Socket) i.getValue();
                                out = t.getOutputStream();

                                out.write("Server: прощай".getBytes());
                                out.flush();
                                list.remove(i.getKey());
                                t.close();
                                flag1++;
                                break;
                            }
                        }
                        if (flag1 == 0){
                            out = this.socket.getOutputStream();
                            out.write("No such user".getBytes());
                            out.flush();
                        }
                    }

                    System.out.println(this.getName()+ " " + message);
                    message = "Server " + message;
                    for (Map.Entry i: list.entrySet()){
                        if (!this.socket.equals(i.getValue())) {
                            Socket t = (Socket) i.getValue();
                            out = t.getOutputStream();

                            out.write(message.getBytes());
                            out.flush();
                        }
                    }

                } catch (IOException e){
                    System.out.println("IOExeption");
                    try{
                        socket.close();
                    } catch (IOException ee){
                        //----
                    }
                    break;
                }
            }

            list.remove(this.getName());
            try{
                socket.close();
            } catch (IOException ee){
                //----
            }
            System.out.println("Конец связи");
        }

    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(9000);
        server.serve();
    }
}
