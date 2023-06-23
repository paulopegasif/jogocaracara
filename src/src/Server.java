package src;

import java.nio.ByteBuffer;

public class Server extends Thread {

    private Communicator comm_server;
    private Communicator comm_client;
    private boolean running = true;
    private short msg_type;
    private int size;
    private int descSize;


    public static void main(String[] args) {
       
        Server server = new Server("127.0.0.1", 5000);

    }

    public Server(String defaultIP, int defaultPort) {
        try {
            this.comm_server = new Communicator(defaultIP, defaultPort);

            System.out.println("Servidor iniciado =>");
            System.out.println("\t Seu canal eh: " + this.comm_server.serverChannelDescription() + "\n");

            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            ByteBuffer buf = null;
            System.out.println("\t Recebendo Mensagens ... \n");

            while (this.running) {
                buf = this.comm_server.receiveMessages();
                msg_type = buf.getShort();
                size = buf.getInt();
                switch (msg_type) {
                    case Config.NEW_WORKER:
                        String workerId = Communicator.readString(buf).trim();
                        System.out.println("Recebi nova mensagem: " + workerId);
                        String channelDesc = Communicator.readString(buf).trim();
                        System.out.println("Recebi mais uma mensagem: " + channelDesc);
                        break;

                    default:
                        System.out.println("InterfaceProxy =>");
                        System.out.println("\t\t LOST MESSAGE! Type: " + msg_type + "\n");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
