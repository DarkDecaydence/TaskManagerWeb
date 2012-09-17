/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package itu.dk.smds.e2012.common;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author GIGAR
 */
@XmlRootElement(name = "cal")
public class Cal {
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    public List<User> users;
    
    @XmlElementWrapper(name = "tasks")
    @XmlElement(name = "task")
    public List<Task> tasks;
    public Cal(){
        users = new ArrayList<User>();
        tasks = new ArrayList<Task>();
    }
    
    public void addUser(User u){
        users.add(u);
    }
    
    public void addTask(Task t){
        tasks.add(t);
    }
}
