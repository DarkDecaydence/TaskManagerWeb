package itu.dk.smds.e2012.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
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

            //Create users:
//            for(String[] a : u){
//                UserRequestServer(socket, a);
//            }
            //Using post, create a task:
            Task task = new Task("0001" , "Do MDS Mandatory Exercise 1","18-09-2012",
                    "initialized","Task Manager simple setup", "Mikkel; Alex; Niklas; Morten");
            taskRequest(socket, task, "POST");
            Task taskDelete = new Task("0002" , "Clean up code","26-09-2012",
                    "initialized","Code needs to shine", "Mikkel; Alex; Niklas; Morten");
            taskRequest(socket, taskDelete, "POST");
            
            //Change task by sending put request
            Task taskput = new Task("0001", "Do MDS Mandatory Exercise 1","18-09-2012",
                    "done","Task Manager simple setup", "Mikkel; Alex; Niklas; Morten");
            taskRequest(socket, taskput,"PUT");
             
            //Get task list by id
            String id = "0001";
            getRequest(socket, id);
            
            //Delete a task by id
            String taskId = "0002";
            deleteRequest(socket, taskId);
            
            //Print file
            //PrintTaskServerRequest(socket);
            dos.writeUTF("close");
            dos.flush();
            socket.close();
        } catch (Exception e) {
            Logger.getLogger(TaskManagerTCPClient.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("error message: " + e.getMessage());
        }
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
    
    private static void taskRequest(Socket socket, Task task,String message) throws IOException {
        // creates data stream.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        dos.writeUTF(message);
        dos.flush();
        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        // gets server response
        String response = dis.readUTF();
        // prints server response
        System.out.println("Message from Server: " + response);
        
        //check response, and sends task request
        if (response.equals("POST")) {
            oos.writeObject(task);
            oos.flush(); 
            response = dis.readUTF();
            //prints response from task creation
            System.out.println("Message from Server: " + response);
        } else if( response.equals("PUT")) {
            oos.writeObject(task);
            oos.flush(); 
            response = dis.readUTF();
            //prints response from task creation
            System.out.println("Message from Server: " + response);
        }
        else { System.out.println("No response from server");}
    }
    
    private static void getRequest(Socket socket, String id) throws IOException {
        // creates data stream.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        dos.writeUTF("GET");
        dos.flush();
        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        // gets server response
        String response = dis.readUTF();
        // prints server response
        System.out.println("Message from Server: " + response);
        
        //check response, and sends request
        if (response.equals("GET")) {
            dos.writeUTF(id);
            dos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            try {
                List<Task> list = (List<Task>) ois.readObject();
                //prints response from task creation
                for(Task t : list){
                    System.out.println("Task: " + t.print());
                }
                System.out.println("Message from Server: " + response);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TaskManagerTCPClient.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Could not retrieve task list");
            }
            
        }
        else { System.out.println("No response from server");}
    }

    private static void deleteRequest(Socket socket, String id) throws IOException {
        // creates data stream.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        
        dos.writeUTF("DELETE");
        dos.flush();
        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        // gets server response
        String response = dis.readUTF();
        // prints server response
        System.out.println("Message from Server: " + response);
        
        //check response, and sends task request
        if (response.equals("DELETE")) {
            dos.writeUTF(id);
            dos.flush(); 
            response = dis.readUTF();
            //prints response from task creation
            System.out.println("Message from Server: " + response);
        }
        else { System.out.println("No response from server");}
    }
}
