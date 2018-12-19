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
public class Client2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String[] strArray = new String[2];
        strArray[0] = "Interpret: Beatles Titel: I Wanna Be Your Man";
        strArray[1] = "Interpret: Sportfreunde Stiller Titel: Ein Komplimen";
        
        p2pClient client = new p2pClient(50001,50010,strArray);
        client.run();       
    }
    
}
