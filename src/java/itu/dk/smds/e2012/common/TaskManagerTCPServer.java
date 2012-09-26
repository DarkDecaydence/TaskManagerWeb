package itu.dk.smds.e2012.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Class handling the server
 */
public class TaskManagerTCPServer {
    private static Socket socket;
    private static DataInputStream dis;
    private static Cal cal = CalSerializer.getCal();
    
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
                if( "POST".equals(message)){
                    post(outputStream);
                } else if("PUT".equals(message)){
                    put(outputStream);
                } else if("GET".equals(message)){
                    get(outputStream);
                } else if("DELETE".equals(message)){
                    delete(outputStream);
                } else if("close".equals(message)){
                    resetServer(serverSocket);
                } else {
                    outputStream.writeUTF("Not a known command");
                    outputStream.flush();
                } 
                
                if(message.equals("close")){
                    resetServer(serverSocket);
                } else {
                    String[] newMessage = message.split(",");
                    if (newMessage[0].equals("NewUser")) {
                        createUser(newMessage[1],newMessage[2]);
                        outputStream.writeUTF("New User Created");
                        outputStream.flush();
                    }  else if (newMessage[0].equals("PrintTask")){
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
    
    private static void post(DataOutputStream outputStream) throws IOException{
        outputStream.writeUTF("POST");
        outputStream.flush();
        // Internal logic for creating a task
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Task task = (Task) ois.readObject();
            cal.POST(task);
            outputStream.writeUTF("Task created");
            outputStream.flush();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TaskManagerTCPServer.class.getName()).log(Level.SEVERE, null, ex);
            outputStream.writeUTF("Task creation failed");
            outputStream.flush();
        }
    }
    
    private static void put(DataOutputStream outputStream) throws IOException{
        outputStream.writeUTF("PUT");
        outputStream.flush();
        // Internal logic for creating a task
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Task task = (Task) ois.readObject();
            cal.PUT(task);
            outputStream.writeUTF("Task modified");
            outputStream.flush();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TaskManagerTCPServer.class.getName()).log(Level.SEVERE, null, ex);
            outputStream.writeUTF("Task modification failed");
            outputStream.flush();
        }
    }
    
    private static void get(DataOutputStream outputStream) throws IOException{
        outputStream.writeUTF("GET");
        outputStream.flush();
        // Internal logic for creating a task
        String id = dis.readUTF();
        List<Task> list = cal.GET();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(list);
        oos.flush();
    }
    
    private static void delete(DataOutputStream outputStream) throws IOException{
        outputStream.writeUTF("DELETE");
        outputStream.flush();
        
        // Internal logic for creating a task
        String id = dis.readUTF();
        cal.DELETE(id);
        
        outputStream.writeUTF("Task Deleted");
        outputStream.flush();
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
