package itu.dk.smds.e2012.common;
import java.io.*;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.*;

/**
 * Class responsible for serialization of the cal class, creating an xml file.
 *
 * @author GIGAR
 */

public class CalSerializer {
    // assign path to the Xml, 
    static String path = System.getProperty("user.dir") + "/web/WEB-INF/task-manager-xml.xml";
    
    /**
     * Method responsible for converting a cal object to an xml document.
     * @param cal, the object to be serialized
     * @throws IOException 
     */
    public static void makeXmlFile(Cal cal) throws IOException{
        try {
            // create an instance context class, to serialize/deserialize.
            JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);

          
            // Serialize cal object into xml.
            StringWriter writer = new StringWriter();

            // We can use the same context object, as it knows how to 
            //serialize or deserialize Cal class.
            jaxbContext.createMarshaller().marshal(cal, writer);

            System.out.println("Printing serialized cal Xml before saving into file!");
            
            // Print the serialized Xml to Console.
            System.out.println(writer.toString());
            
            // Finally save the Xml back to the file.
            SaveFile(writer.toString(), path);

        } catch (JAXBException ex) {
            Logger.getLogger(CalSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method for saving the xml file
     * @param xml, the xml to be saved
     * @param path, the path location of the file
     * @throws IOException 
     */
    private static void SaveFile(String xml, String path) throws IOException {
        File file = new File(path);
        
        // create a bufferedwriter to write Xml
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        output.write(xml);
        output.close();
    }
    
    public static Cal getCal(){
        Cal returnCal = null;
        try {
        // create an instance context class, to serialize/deserialize.
        JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);

        // Create a file input stream for the university Xml.
        FileInputStream stream = new FileInputStream(path);

        // deserialize university xml into java objects.
         returnCal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);
        }
        catch (Exception e) { System.out.println("Coud not properly initialize: " + e); }
        return returnCal;
    }
    
    public static String TaskToXmlString(Task task) throws Exception{
        StringWriter writer = new StringWriter();
        try {
        JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
        

        jaxbContext.createMarshaller().marshal(task, writer);
        }
        catch (Exception e) {
            System.out.println("Error, could not create task:" + e);
        }
        return writer.toString();
    }
    
}
