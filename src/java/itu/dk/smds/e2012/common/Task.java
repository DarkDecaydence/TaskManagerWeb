/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package itu.dk.smds.e2012.common;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author GIGAR
 */
@XmlRootElement(name = "task")
public class Task {
    @XmlAttribute(name = "id")
    public String id;
    @XmlAttribute(name = "name")
    public String name;
    @XmlAttribute(name = "date")
    public String date;
    @XmlAttribute(name = "status")
    public String status;
    
    public String description;
    public String attendant;
    
    public Task(){}
    public Task(String id, String name, String date, String status,
            String description, String attendant){
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.description = description;
        this.attendant = attendant;
    }
}
