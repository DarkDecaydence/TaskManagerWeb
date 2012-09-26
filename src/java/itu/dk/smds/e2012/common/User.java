package itu.dk.smds.e2012.common;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
/**
 * Class for creating users
 */
@XmlRootElement(name = "user")
/**
 * Constructor for creating a standard user
 */
public class User {
    public String name;
    public String password;
    
    /**
     * serialization constructor
     */
    public User(){}
    public User(String name, String password){
        this.name = name;
        this.password = password;
    }
}
