
package p2p.app;


import java.net.InetAddress;
import java.io.IOException;
import java.nio.ByteBuffer;
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
    
    public static final int HEADER_SIZE = 5;
    public static final int MAX_PAYLOAD_SIZE = 1024;
    
    
    public UDPMessage(MSGType type, String data, int port)
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
        this.port = port;
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
    
    public byte[] toByteArray()
    {
        byte[] data = new byte[HEADER_SIZE + MAX_PAYLOAD_SIZE];
		
		//Header
		data[0] = this.type;
		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.putInt(this.port);
		byte[] portData = buff.array();
		for (int i = 0; i < 4; ++i) {
			data[i+1] = portData[i];
		}
		
		//Content
		byte[] cData = this.content.getBytes();
		
		for (int i = HEADER_SIZE; i < data.length; ++i) {
			if (i - HEADER_SIZE < cData.length)
				data[i] = cData[i-HEADER_SIZE];
			else
				data[i] = 0;
			
		}
		return data;
    }
    
    
    public int getSenderPort() throws IOException
    {
        return this.port;
    }
    
       public InetAddress getSenderAddress() throws IOException
    {
        return this.addr;
    }
    
    public static UDPMessage fromByteArray(byte[] data, InetAddress addr, int port)
    {
       	byte[] portData = new byte[4];
        for (int i = 0; i < 4; ++i) 
        {
            portData[i] = data[i+1];
        }
		
	ByteBuffer buff = ByteBuffer.wrap(portData);
	int destPort = buff.getInt();
		
	String content = new String(data, HEADER_SIZE, data.length - HEADER_SIZE);
		
	return new UDPMessage(data[0], content, addr, destPort);
    }
}
