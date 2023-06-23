package src;

import java.nio.ByteBuffer;

public class Client extends Thread{

    private boolean running = true;
    Communicator launcherChannelSend;
    private int size;
    private int descSize;
    private byte[] jobDesc;
    private short msg_type;
    private float capacity;

    String launcherChannelDesc;
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1:5000");

    }

    public Client(String launcherChannelDesc) {
        try {
            this.launcherChannelDesc = "127.0.0.1:5000";

            Thread.sleep(1000);
            this.launcherChannelSend = new Communicator();
            this.launcherChannelSend.connectServer(launcherChannelDesc);
            this.launcherChannelSend.sendMsgNewWorker(launcherChannelSend.getSocket(), "Professor deu clolher de chá", "agora é com você pequeno aprendiz...");

            System.out.println("Cliente Iniciado =>");
            System.out.println("\t\t Seu channel eh: " + this.launcherChannelSend.clientLocalChannelDesc() + "\n");

            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            ByteBuffer buf = null;

            /////////////////////////////////////////////////////////////////////////
            System.out.println("Cliente =>");
            System.out.println("\t Recebendo Mensagens ... \n");
            /////////////////////////////////////////////////////////////////////////

            while (this.running) {
                buf = this.launcherChannelSend.receiveMessages();
                msg_type = buf.getShort();
                size = buf.getInt();
                switch (msg_type) {
                    case Config.NEW_JOB:
                        String jobId = Communicator.readString(buf).trim();
                        descSize = size - 2 - 4 - (jobId.length() + 1);
                        jobDesc = new byte[descSize];
                        //buf.get(jobDesc);
                        //worker.newJob(jobId, jobDesc);
                        break;

                    default:
                        System.out.println("Cliente =>");
                        System.out.println("\t\t LOST MESSAGE! Type: " + msg_type + "\n");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
