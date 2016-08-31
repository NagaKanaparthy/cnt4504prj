package cnt.prjrmi;
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

public class Server implements Commander{
    public Server(){}
    public static void main(String[] args){
        try {
            Server obj = new Server();
            Commander stub = (Commander) UnicastRemoteObject.exportObject(obj, 0);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Commander", stub);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            e.printStackTrace();
        }
    }
    /*
    * */
    public String getDate(){ return getCommand(getCommand("date")); }
    /*
    * */
    public String getUptime(){
        return getCommand(getCommand("uptime"));
    }
    /*
    * */
    public String getNetstat(){
        return getCommand(getCommand("netstat"));
    }
    /*
    * */
    public String getWho(){
        return getCommand(getCommand("who"));
    }
    /*
    * */
    public String getProcess(){
        return getCommand(getCommand("ps aux"));
    }
    /*
    * */
    private String getCommand(String command){
        try{
            System.out.println("Command Hit");
            Process getDateProcess = Runtime.getRuntime().exec(command);
            BufferedReader dataFromProcess = new BufferedReader
                    (new InputStreamReader(getDateProcess.getInputStream()));
            String result = null;
            while((result = dataFromProcess.readLine()) != null){
                result += "\n" + result;
            }
            return result;
        }
        catch (Exception e){
            return "Error getting the data";
        }
    }
}
