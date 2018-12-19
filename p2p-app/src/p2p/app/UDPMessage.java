
package p2p.app;


import java.net.InetAddress;
import java.io.IOException;
/**
 *
 * @author phil
 */
public class UDPMessage 
{ 
    
    public enum MSGType
    {
        UNDEFINED,
        REQUEST,
        ANSWER
    }
    
    public byte type;
    private String content;
    
    //Only defined for recieved messages
    private InetAddress addr;
    private int port;
    
    
    public UDPMessage(MSGType type, String data)
    {
        switch(type)
        {
            case REQUEST:
                this.type = 1;
                break;
            case ANSWER:
                this.type = 2;
                break;
            default:
                this.type = 0;
                break;
        }       
        this.content = data;
    }
    
    public UDPMessage(byte type, String data, InetAddress addr, int port)
    {
        this.type = type;
        this.content = data;
        this.addr = addr;
        this.port = port;
    }
    
    public String getContent()
    {
        return this.content;
    }
    
    
    public MSGType getType()
    {
        switch(this.type)
        {
            case 1:
                return MSGType.REQUEST;
            case 2:
                return MSGType.ANSWER;
            default:
                return MSGType.UNDEFINED;
        }
    }
    
    public byte[] toByteArray(int size)
    {
        byte[] data = new byte[size];
        data[0] = this.type;
        byte[] contentData = this.content.getBytes();
        
        for(int i =0; i< size-1 && i+1 <contentData.length;i++)
        {
            data[i] = contentData[i+1];
        }
        
        
        return data;
    }
    
    
    public int getSenderPort() throws IOException
    {
        if(this.type != 1)
        {
            throw new IOException("Incompatible type for sender address!");
        }
        return this.port;
    }
    
       public InetAddress getSenderAddress() throws IOException
    {
        if(this.type != 1)
        {
            throw new IOException("Incompatible type for sender address!");
        }
        return this.addr;
    }
    
    public static UDPMessage fromByteArray(byte[] data, InetAddress addr, int port)
    {
        System.out.println("Data recieved" + data[0]);
        return new UDPMessage(data[0], new String(data,1,data.length-1), addr, port);
    }
}
