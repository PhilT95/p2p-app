package p2p.app;

import java.util.ArrayDeque;
import java.util.Queue;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 *
 * @author phil
 */
public class UDPReciever 
{
    private Queue<UDPMessage> messages;
    public int port;
    private Thread thread;
    private RecieverThread rcThread;
    private DatagramSocket socket;
    
    public UDPReciever(int port) throws SocketException
    {
        this.messages = new ConcurrentLinkedQueue<UDPMessage>();
        this.port = port;
        this.socket = new DatagramSocket(port);
      
        this.rcThread = new RecieverThread(this);
        this.thread = new Thread(this.rcThread);
        thread.start();
    }
    
    public void finalize()
    {
        this.socket.close();
    }
    
    public void stop()
    {
        this.rcThread.stop();
        this.socket.close();
    }
    
    public UDPMessage getMessage()
    {
        UDPMessage msg = this.messages.peek();
	if (msg != null) 
        {
            this.messages.remove(msg);
        }
	return msg;
      
    }
    
    private class RecieverThread implements Runnable
    {
        private UDPReciever rcv;
        private boolean keepRunning;
        private byte[] recieveBuffer = new byte[1024];
        public RecieverThread(UDPReciever rcv)
        {
            this.rcv = rcv;
            this.keepRunning = true;
        }
        
        @Override
        public void run()
        {
            DatagramPacket packet = new DatagramPacket(recieveBuffer, recieveBuffer.length);
            
            
            while(keepRunning)
            {
                try
                {
                    this.rcv.socket.receive(packet);
                    if(packet.getPort() == this.rcv.port && packet.getAddress().isAnyLocalAddress())
                    {
                        continue;
                    }
                    UDPMessage msg = UDPMessage.fromByteArray(packet.getData(),packet.getAddress(),packet.getPort());
                    this.rcv.messages.add(msg);
                    
                }catch(IOException ex)
                {
                    ex.printStackTrace();
                }
                
            }
        }
        
        public void stop()
        {
            keepRunning = false;
        }
    }
    
}
