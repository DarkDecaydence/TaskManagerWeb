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
    private static RestClient restClient = new RestClient();
    private static SoapClient soapClient = new SoapClient();
    
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
                } else if("GetAttendantTasks".equals(message)){
                    attendantTasks(outputStream);
                } else if("CreateTask".equals(message)){
                    createTaskSoapRest(outputStream);
                } else if("DeleteTask".equals(message)){
                    deleteTaskSoapRest(outputStream);
                } else if("close".equals(message)){
                    resetServer(serverSocket);
                } else {
                    outputStream.writeUTF("Not a known command");
                    outputStream.flush();
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
    
    private static void attendantTasks(DataOutputStream outputStream) throws IOException{
        outputStream.writeUTF("GetAttendantTasks");
        outputStream.flush();
        // Internal logic for creating a task
        try {
            String[] info = dis.readUTF().split(",");
            String attendantTasks = GetAttendantTasks(info[0], Integer.parseInt(info[1]));
            outputStream.writeUTF(attendantTasks);
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(TaskManagerTCPServer.class.getName()).log(Level.SEVERE, null, ex);
            outputStream.writeUTF("Get Attendant Tasks failed");
            outputStream.flush();
        }
    }
    
    private static void createTaskSoapRest(DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF("CreateTask");
        outputStream.flush();
        try {
            int serviceOption = Integer.parseInt(dis.readUTF());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Task task = (Task) ois.readObject();
            CreateTask(CalSerializer.TaskToXmlString(task), serviceOption);
            
            outputStream.writeUTF("Created Task");
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(TaskManagerTCPServer.class.getName()).log(Level.SEVERE, null, ex);
            outputStream.writeUTF("Create Task failed");
            outputStream.flush();
        } catch (Exception ex) {
            Logger.getLogger(TaskManagerTCPServer.class.getName()).log(Level.SEVERE, null, ex);
            outputStream.writeUTF("Failed to create task, XML error");
            outputStream.flush();
        }
    }
    
    private static void deleteTaskSoapRest(DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF("DeleteTask");
        outputStream.flush();
        dis = new DataInputStream(socket.getInputStream());
        
        try {
            String infom = dis.readUTF();
            String[] info = infom.split(",");
            
            System.out.println("Info " + infom);
            DeleteTask(info[0], Integer.parseInt(info[1]));
            outputStream.writeUTF("Deleted Task");
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(TaskManagerTCPServer.class.getName()).log(Level.SEVERE, null, ex);
            outputStream.writeUTF("Delete Task failed");
            outputStream.flush();
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
    
    public static String GetAttendantTasks(String attendantId, int serviceOption) {
        String url = "http://trustcare.itu.dk/task-manager-rest/tasks/attendant/";
        String returnString = "";
        switch (serviceOption) {
            case 1:     returnString = soapClient.getAttendantTasks(attendantId);
                        break;
                
            case 2:     returnString = restClient.DoRestCall(url+attendantId,"GET", null);
                        break;
            
            case 3:     returnString = soapClient.getAttendantTasks(attendantId);
                        returnString += ", ";
                        returnString += restClient.DoRestCall(url+attendantId,"GET", null); 
                        break;
            
            default:    System.out.println("Please specify a service option");
                        break;
        }
        return returnString;
    }
    
    public static void CreateTask(String taskXml, int serviceOption) {
        String url = "http://trustcare.itu.dk/task-manager-rest/tasks/createtask";
        switch (serviceOption) {
            case 1:     soapClient.createTask(taskXml);
                        break;
            
            case 2:     restClient.DoRestCall(url, "POST", taskXml);
                        break;
            
            case 3:     soapClient.createTask(taskXml);
                        restClient.DoRestCall(url, "POST", taskXml);
                        break;
            
            default:    System.out.println("Please specify a service option");
                        break;
        }
    }
    
    public static void DeleteTask(String taskId, int serviceOption) {
        String url = "http://trustcare.itu.dk/task-manager-rest/tasks/DeleteTask?taskId=";
        switch (serviceOption) {
            case 1:     soapClient.deleteTask(taskId);
                        break;
            
            case 2:     restClient.DoRestCall(url+taskId, "DELETE", null);
                        break;
                
            case 3:     soapClient.deleteTask(taskId);
                        restClient.DoRestCall(url+taskId, "DELETE", null);
                        break;
            
            default:    System.out.println("Please specify a service option");
                        break;
        }
    }
}
