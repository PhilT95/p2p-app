package p2p.app;

/**
 *
 * @author phil
 */
public class P2pApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String[] strArray = new String[2];
        strArray[0] = "Interpret: Michael Jackson Titel: Thriller";
        strArray[1] = "Interpret: Razorlight Titel: Wire to Wire";
        
        p2pClient client = new p2pClient(50001,50010,strArray);
        client.run();
        
    }
    
}
