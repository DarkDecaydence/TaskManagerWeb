package itu.dk.smds.e2012.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class handling the server
 */
public class TaskManagerTCPServer {
    private static Socket socket;
    private static DataInputStream dis;
    private static Cal cal = new Cal();
    
    /**
     * Main method for initializing the server
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
                int serverPort = 7896;
                ServerSocket serverSocket = new ServerSocket(serverPort);
                System.out.println("Server started at port: " + serverPort);

                socket = serverSocket.accept();

                InputStream is = socket.getInputStream();
                dis = new DataInputStream(is);

                while (true) {
                String message = "";
                //checks to see if client is still sending requests
                try{
                    message = dis.readUTF();
                } catch (IOException e){
                    resetServer(serverSocket);
                }
                
                System.out.println("Message from Client: " + message);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                
                //Checks message from client
                if (message.equals("Hello Server!")) {
                    outputStream.writeUTF("Ready");
                    outputStream.flush();
                } else if(message.equals("close")){
                    resetServer(serverSocket);
                } else {
                    String[] newMessage = message.split(",");
                    if (newMessage[0].equals("NewUser")) {
                        createUser(newMessage[1],newMessage[2]);
                        outputStream.writeUTF("New User Created");
                        outputStream.flush();
                    } else if (newMessage[0].equals("NewTask")) {
                        createTask(newMessage[1],newMessage[2],newMessage[3],newMessage[4],
                                newMessage[5],newMessage[6]);
                        
                        outputStream.writeUTF("New Task Created");
                        outputStream.flush();
                    } else if (newMessage[0].equals("PrintTask")){
                        calToXml();
                        outputStream.writeUTF("Task List Printed");
                        outputStream.flush();
                    }
                }
            }
        } catch (IOException e) {
            Logger.getLogger(TaskManagerTCPServer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("error message: " + e.getMessage());
        }
    }
    
    /**
     * Creates an user object
     * @param name, the name of the user
     * @param password, the password of the user
     */
    private static void createUser(String name, String password){
        cal.addUser(new User(name, password));
    }
    /**
     * Creates a task object
     * @param id, the id of the task
     * @param name, the name of the task
     * @param date, the date of the task
     * @param status, the task status
     * @param description, the description of the task
     * @param attendant, the task attendants 
     */
    private static void createTask(String id, String name, String date, String status,
            String description, String attendant){
        cal.addTask(new Task(id, name, date, status, description, attendant));
    }
    /**
     * Method for resetting server
     * @param serverSocket, the socket to be reset
     */
    private static void resetServer(ServerSocket serverSocket){
        try{
            socket.close();
            socket = serverSocket.accept();
            InputStream is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            System.out.println("Could not reset server");
        }     
    }
    /**
     * Method for creating an xml file
     */
    private static void calToXml(){
        try{
            CalSerializer.makeXmlFile(cal);
        } catch(IOException e) {
            System.out.println("No file printed");
        }
    }
}
