/*@author Nagavarun Kanaparthy
* */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Commander extends Remote{
    public String getDate() throws RemoteException;
    public String getUptime() throws RemoteException;
    public String getNetstat() throws RemoteException;
    public String getWho() throws RemoteException;
    public String getProcess() throws RemoteException;
}
