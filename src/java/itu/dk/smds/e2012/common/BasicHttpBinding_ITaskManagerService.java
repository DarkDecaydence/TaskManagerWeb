/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package itu.dk.smds.e2012.common;

import itu.dk.smds.e2012.common_client.ITaskManagerService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author Alexander
 */
@Path("basichttpbinding_itaskmanagerservice")
public class BasicHttpBinding_ITaskManagerService {
    private ITaskManagerService port;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of BasicHttpBinding_ITaskManagerService
     */
    public BasicHttpBinding_ITaskManagerService() {
        port = getPort();
    }

    /**
     * Invokes the SOAP method GetAllTasks
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("getalltasks/")
    public String getAllTasks() {
        try {
            // Call Web Service Operation
            if (port != null) {
                java.lang.String result = port.getAllTasks();
                return result;
            }
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }

    /**
     * Invokes the SOAP method GetAttendantTasks
     * @param attendantId resource URI parameter
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("getattendanttasks/")
    public String getAttendantTasks(@QueryParam("attendantId") String attendantId) {
        try {
            // Call Web Service Operation
            if (port != null) {
                java.lang.String result = port.getAttendantTasks(attendantId);
                return result;
            }
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }

    /**
     * Invokes the SOAP method GetTask
     * @param taskId resource URI parameter
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("gettask/")
    public String getTask(@QueryParam("taskId") String taskId) {
        try {
            // Call Web Service Operation
            if (port != null) {
                java.lang.String result = port.getTask(taskId);
                return result;
            }
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }

    /**
     * Invokes the SOAP method CreateTask
     * @param taskXml resource URI parameter
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("createtask/")
    public String getCreateTask(@QueryParam("taskXml") String taskXml) {
        try {
            // Call Web Service Operation
            if (port != null) {
                java.lang.String result = port.createTask(taskXml);
                return result;
            }
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }

    /**
     * Invokes the SOAP method DeleteTask
     * @param taskId resource URI parameter
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("deletetask/")
    public String getDeleteTask(@QueryParam("taskId") String taskId) {
        try {
            // Call Web Service Operation
            if (port != null) {
                java.lang.String result = port.deleteTask(taskId);
                return result;
            }
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }

    /**
     *
     */
    private ITaskManagerService getPort() {
        try {
            // Call Web Service Operation
            itu.dk.smds.e2012.common_client.TaskManagerService service = new itu.dk.smds.e2012.common_client.TaskManagerService();
            itu.dk.smds.e2012.common_client.ITaskManagerService p = service.getBasicHttpBindingITaskManagerService();
            return p;
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }
}
