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
 * The task manager client
 */
public class TaskManagerTCPClient {

    /**
     * The method for starting the client
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //new string array containing all the users of the system
            String[][] u = new String[][]{{"Alexander Kirk","goblin123"},
                {"Mikkel Stolborg","demon123"},{"Niklas Madsen","orc123"},{"Morten Dresdner","devil123"}};
            
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 7896;

            Socket socket = new Socket(serverAddress, serverPort);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            boolean serverState = checkServer(socket);
            if (serverState == false) 
                { throw new IOException("No server response"); }
            //Create users:
            for(String[] a : u){
                UserRequestServer(socket, a);
            }
            //Create Task
            TaskRequestServer(socket,"0001" , "Do MDS Mandatory Exercise 1","18-09-2012",
                    "done","Task Manager simple setup", "Mikkel; Alex; Niklas; Morten");
            
            //Print file
            PrintTaskServerRequest(socket);
            dos.writeUTF("close");
            dos.flush();
            socket.close();
        } catch (Exception e) {
            Logger.getLogger(TaskManagerTCPClient.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("error message: " + e.getMessage());
        }
    }
    
    /**
     * Method used to check server state
     * @param socket, the server socket
     * @return true if ready, false if no response
     * @throws IOException 
     */
    private static boolean checkServer(Socket socket) throws IOException {
        // creates data stream.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        String message = "Hello Server!";
        
        dos.writeUTF(message);
        dos.flush();
        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        // gets server response
        String response = dis.readUTF();
        
        System.out.println("Message from Server: " + response);
        
        if (response.equals("Ready")) { return true; }
        else { return false; }
    }
    
    /**
     * Method for sending user request
     * @param socket, the server socket
     * @param user, string containing information regarding the user
     * @throws IOException 
     */
    private static void UserRequestServer(Socket socket, String[] user) throws IOException {
        // creates data stream.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        String message = "Hello Server!";
        
        dos.writeUTF(message);
        dos.flush();
        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        // gets server response
        String response = dis.readUTF();
        
        // prints server response
        System.out.println("Message from Server: " + response);
        
        //check response, and sends user request
        if (response.equals("Ready")) {
            message = "NewUser,"+user[0]+","+user[1];
            dos.writeUTF(message);
            dos.flush();
            response = dis.readUTF();
            //response from user request
            System.out.println("Message from Server: " + response);
        }
        else { System.out.println("No response from server");}
    }
    
    /**
     * Method for sending task request
     * @param socket, server socket
     * @param id, id of the task
     * @param name, name of the task
     * @param date, the date of the task
     * @param status, the status of the task
     * @param description, the description of the task
     * @param attendant, the attendants of the task
     * @throws IOException 
     */
    private static void TaskRequestServer(Socket socket, String id, String name,
            String date, String status, String description, String attendant) throws IOException {
        // creates data stream.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        String message = "Hello Server!";
        
        dos.writeUTF(message);
        dos.flush();
        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        // gets server response
        String response = dis.readUTF();
        // prints server response
        System.out.println("Message from Server: " + response);
        
        //check response, and sends task request
        if (response.equals("Ready")) {
            message = "NewTask,"+id+","+name+","+date+","+status+","+description+
                    ","+attendant;
            dos.writeUTF(message);
            dos.flush(); 
            response = dis.readUTF();
            //prints response from task creation
            System.out.println("Message from Server: " + response);
        }
        else { System.out.println("No response from server");}
    }
    /**
     * Method for requesting xml file
     * @param socket, server socket
     * @throws IOException 
     */
    private static void PrintTaskServerRequest(Socket socket) throws IOException {
         // creates data stream.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        String message = "Hello Server!";
        
        dos.writeUTF(message);
        dos.flush();
        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        // gets server response
        String response = dis.readUTF();
        // prints server response
        System.out.println("Message from Server: " + response);
        //checks response and sends print request
        if (response.equals("Ready")) {
            message = "PrintTask";
            dos.writeUTF(message);
            dos.flush(); 
            response = dis.readUTF();
            //prints response from print request
            System.out.println("Message from Server: " + response);
        }
        else { System.out.println("No response from server");}
    }

}
