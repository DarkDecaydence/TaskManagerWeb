package itu.dk.smds.e2012.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Alexander
 */
public class TaskManagerTCPClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 7896;
            
            Socket socket = new Socket(serverAddress, serverPort);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String[][] u = new String[][]{{"Alexander Kirk","goblin123"},
                {"Mikkel Stolborg","demon123"},{"Niklas Madsen","orc123"}};
            for(String[] a : u){
                boolean serverState = checkServer(socket);
                if (serverState == false) 
                    { throw new IOException("No server response"); }
                String request = new String("NewUser,"+a[0]+","+a[1]);
                
                dos.writeUTF(request);
                
                dos.flush();
            }

            
            
            //Create users:
            
            socket.close();
            
        } catch (Exception e) {
            Logger.getLogger(TaskManagerTCPClient.class.getName()).log(Level.SEVERE, null, e);
            
            System.out.println("error message: " + e.getMessage());
        }
    }
    
    private static boolean checkServer(Socket socket) throws IOException {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        String message = "Hello Server!";
        
        dos.writeUTF(message);
        dos.flush();
        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        
        String response = dis.readUTF();
        
        System.out.println("Message from Server: " + response);
        
        if (response.equals("Ready")) { return true; }
        else { return false; }
    }

}
