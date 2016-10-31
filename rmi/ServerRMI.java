/*
@author Nagavarun Kanaparthy
@version 0.1
@since 2016-08-31
* */
import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI implements Commander{
    public ServerRMI() throws RemoteException {}
    public static void main(String[] args){
        try {
            ServerRMI obj = new ServerRMI();
            Commander stub = (Commander) UnicastRemoteObject.exportObject(obj, 0);
            // Bind the remote object's stub in the registry
            Registry reg = LocateRegistry.getRegistry();
            reg.bind("Commander", stub);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            e.printStackTrace();
        }
    }
    /*
* */
public String getDate(){ return getCommand("date"); }
/*
* */
public String getMemory(){ return getCommand("free -m"); }
/*
**/
public String getUptime(){ return getCommand("uptime"); }
/*
* */
public String getNetstat(){ return getCommand("netstat"); }
/*
* */
public String getWho(){ return getCommand("who"); }
/*
* */
public String getProcess(){ return getCommand("ps aux"); }
/*
* */
private static String getCommand(String cmd){
    String data = "";
    try {
        System.out.println("Command Requsted : " + cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        //attempt to initializing buffer
        BufferedReader stdInput = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
        //read the output from the command
        String line = null;
        while ((line = stdInput.readLine()) != null) {
            data += line+"\n";
        }
    }
    catch(Exception e){
        data = "error";
    }
    return data;
}
}
