package p2p.app;


import p2p.app.p2pClient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author phil
 */
public class Client3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] strArray = new String[2];
        strArray[0] = "Beatles Titel: All My Loving";
        strArray[1] = "Interpret: Rolling Stones Titel: Satisfaction";
        
        p2pClient client = new p2pClient(50001,50010,strArray);
        client.run();
    }
    
}
