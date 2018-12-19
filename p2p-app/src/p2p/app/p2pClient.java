package p2p.app;

import java.net.SocketException;
import java.io.IOException;
import java.net.InetAddress;
import p2p.app.UDPMessage.MSGType;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author phil
 */
public class p2pClient 
{

    public UDPReciever reciever;
    private String[] iniData;
    private List<String> songData; 
    private int minPort;
    private int maxPort;
    
    public p2pClient(int minPort, int maxPort, String[] iniData)
    {
        this.iniData = iniData;
        this.maxPort = maxPort;
        this.minPort = minPort;
        
        this.songData = new ArrayList<>();
        for(int i = 0; i< iniData.length; i++)
        {
            this.songData.add(iniData[i]);
        }
        
        boolean canConnect = false;
        for(int i=minPort; i<= maxPort && !canConnect; i++)
        {
            try
            {
                this.reciever = new UDPReciever(i);
                canConnect = true;
            }catch(SocketException ex)
            {
                if(i==maxPort)
                {
                    ex.printStackTrace();
                    System.exit(i);
                }
            }
        }
    }
    
    private void sendReply(UDPMessage msg)
    {
        String msgData = "";
        for(int i = 0; i < iniData.length; i++)
        {
            msgData += iniData[i] + "\n";
        }
        
        UDPMessage outMsg = new UDPMessage(MSGType.ANSWER, msgData);
        try
        {
           UDPSender.send(msg.getSenderAddress(), msg.getSenderPort(), outMsg);
        }catch(SocketException ex)
        {
            ex.printStackTrace();
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    
    private void addAnswerToList(String content)
    {
        String[] data = content.split("\n");
        for(int i = 0; i < data.length;i++)
        {
            boolean add = true;
            for(String str : this.songData)
            {
                if(str.compareTo(data[i]) == 0)
                {
                    add = false;
                }
            }
            if(add)
            {
                this.songData.add(data[i]);
            }
        }
        
        printSongData();
    }
    
    private void printSongData()
    {
        System.out.println("SONGS von Client");
        for(String str : this.songData)
        {
            System.out.println(str);
        }
    }
    
    private void processMessage(UDPMessage msg)
    {
        switch(msg.getType())
        {
            case REQUEST:
                sendReply(msg);
                break;
            case ANSWER:
                addAnswerToList(msg.getContent());
                break;
        }
    }
    
    public void sendRequest()
    {
        for(int i = minPort; i<= maxPort; i++)
        {
            try
            {
            UDPSender.send(InetAddress.getLocalHost(), i, new UDPMessage(MSGType.REQUEST, ""));
        
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    public void run()
    {
        sendRequest();
        while(true)
        {
            
            if(this.reciever != null)
            {
                UDPMessage msg = this.reciever.getMessage();
                if(msg != null)
                {
                processMessage(msg);
                }
            }
         
        }
    }
    
    
}
