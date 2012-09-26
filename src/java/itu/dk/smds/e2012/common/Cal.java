package itu.dk.smds.e2012.common;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
/**
 * The class responsible for the task manager data
 */
@XmlRootElement(name = "cal")
public class Cal {
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    public List<User> users;
    
    @XmlElementWrapper(name = "tasks")
    @XmlElement(name = "task")
    public List<Task> tasks;
    
    /**
     * Constructor creating the lists of users and tasks.
     */
    public Cal(){
        users = new ArrayList<User>();
        tasks = new ArrayList<Task>();
    }
    
    /**
     * Method for adding a new user
     * @param u, the user to be added to the task manager system
     */
    public void addUser(User u){
        users.add(u);
    }
    /**
     * Method for adding a new task
     * @param t, the task to be added to the task manager system 
     */
    public void addTask(Task t){
        tasks.add(t);
    }
}
