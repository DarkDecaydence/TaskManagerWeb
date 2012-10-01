package itu.dk.smds.e2012.common;

import javax.jws.WebService;

/**
 *
 * @author Alexander
 */
@WebService(serviceName = "TaskManagerService", portName = "BasicHttpBinding_ITaskManagerService", endpointInterface = "org.tempuri.ITaskManagerService", targetNamespace = "http://tempuri.org/", wsdlLocation = "WEB-INF/wsdl/SoapClient/trustcare.itu.dk/task-manager-soap/TaskManagerService.svc.wsdl")
public class SoapClient {
    BasicHttpBinding_ITaskManagerService httpBinding = new BasicHttpBinding_ITaskManagerService();

    public java.lang.String getAllTasks() {
        return httpBinding.getAllTasks();
    }

    public java.lang.String getAttendantTasks(java.lang.String attendantId) {
        return httpBinding.getAttendantTasks(attendantId);
    }

    public java.lang.String getTask(java.lang.String taskId) {
        return httpBinding.getTask(taskId);
    }

    public java.lang.String createTask(java.lang.String taskXml) {
        return httpBinding.getCreateTask(taskXml);
    }

    public java.lang.String deleteTask(java.lang.String taskId) {
        return httpBinding.getDeleteTask(taskId);
    }

}
