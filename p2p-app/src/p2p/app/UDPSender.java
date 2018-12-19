
package p2p.app;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.io.IOException;

/**
 *
 * @author phil
 */
public class UDPSender 
{
    public static void send(InetAddress dest, int port, UDPMessage msg) throws SocketException
    {
        DatagramSocket socket = new DatagramSocket();
        
        byte[] data = msg.toByteArray(1024);
        
        DatagramPacket packet =  new DatagramPacket(data, data.length, dest, port);
        
        try
        {
            socket.send(packet);
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
        
        socket.close();
    }
    
    
    
   
    
    
}
