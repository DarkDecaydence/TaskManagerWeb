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
 *
 * @author Alexander
 */
public class TaskManagerTCPServer {
    private static Socket socket;
    private static DataInputStream dis;
    private static Cal cal = new Cal();
    /**
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
                try{
                    message = dis.readUTF();
                } catch (IOException e){
                    resetServer(serverSocket);
                }
                
                System.out.println("Message from Client: " + message);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                
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
                //socket.close();
            }
            } catch (IOException e) {
                Logger.getLogger(TaskManagerTCPServer.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("error message: " + e.getMessage());
            }
        
    }
    
    private static void createUser(String name, String password){
        cal.addUser(new User(name, password));
    }
    
    private static void createTask(String id, String name, String date, String status,
            String description, String attendant){
        cal.addTask(new Task(id, name, date, status, description, attendant));
    }
    private static void resetServer(ServerSocket serverSocket){
        try{
            socket.close();
            socket = serverSocket.accept();
            InputStream is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            
        }
        
        
    }
    private static void calToXml(){
        try{
            CalSerializer.makeXmlFile(cal);
        } catch(IOException e) {
            System.out.println("No file printed");
        }
        
    }
}
