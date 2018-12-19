/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.app;

import java.util.ArrayDeque;
import java.util.Queue;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.SocketException;
/**
 *
 * @author phil
 */
public class UDPReciever 
{
    private Queue<UDPMessage> messages;
    private int port;
    private Thread thread;
    private DatagramSocket socket;
    
    public UDPReciever(int port) throws SocketException
    {
        this.port = port;
        this.messages = new ArrayDeque<UDPMessage>();      
        this.socket = new DatagramSocket(port);
      
        
        this.thread = new Thread(new RecieverThread(this));
        thread.start();
    }
    
    public UDPMessage getMessage()
    {
        return this.messages.poll();
      
    }
    
    private class RecieverThread implements Runnable
    {
        private UDPReciever rcv;
        private boolean keepRunning = true;
        private byte[] recieveBuffer = new byte[1024];
        public RecieverThread(UDPReciever rcv)
        {
            this.rcv = rcv;
        }
        
        @Override
        public void run()
        {
            DatagramPacket packet = new DatagramPacket(recieveBuffer, 1024);
            
            
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
