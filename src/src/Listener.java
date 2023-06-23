package src;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

public class Listener extends Thread {

    private boolean listening;
    SocketChannel clientChannel;
    int nBytes;
    BlockingQueue<ByteBuffer> incoming;

    public Listener(SocketChannel clientChannel, BlockingQueue<ByteBuffer> incoming) {
        try {
            this.clientChannel = clientChannel;
            this.incoming = incoming;
            this.listening = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (listening) {
                ByteBuffer header = bytesRead(6);
                if (header == null) {
                    clientChannel.close();
                } else {
                    header.getShort(); // Msg_Type
                    int size = header.getInt();
                    header.rewind();

                    ByteBuffer body = bytesRead(size - 6);
                    ByteBuffer msg = ByteBuffer.allocate(size);
                    msg.put(header).put(body).flip();

                    try {
                        incoming.put(msg);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public ByteBuffer bytesRead(int bytes) throws IOException {
        ByteBuffer msg = ByteBuffer.allocate(bytes);
        int readBytes = 0;
        while (readBytes < bytes) {
            int readCount = clientChannel.read(msg);
            if (readCount == -1) {
                clientChannel.close();
            }
            readBytes += readCount;
        }
        msg.flip();
        return msg;
    }
}
