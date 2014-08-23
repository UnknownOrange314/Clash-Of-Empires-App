package engine.general.network;


/**
 *This class is used to represent an interface between the client and the server.
 */
public abstract class  DisplayCommunicator extends Thread{
    
    /**
     * Clear the output stream.
     * TODO:Rename this method.
     */
    public abstract void flushInput();

    /**
     * Use this method to write an object to the client.
     * @param obj The object that should be written to the client.
     */

    public abstract void writeToClient(Object obj);

    /**
     * Use this method to read an object from the server.
     * @return The object that was read from the server.
     */
    public abstract Object readFromClient();
}