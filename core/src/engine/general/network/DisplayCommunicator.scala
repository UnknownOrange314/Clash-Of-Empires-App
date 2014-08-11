package engine.general.network

/**
 *This class is used to represent an interface between the client and the server.
 */
abstract class  DisplayCommunicator extends Thread{
    
    /**
     * Clear the output stream.
     */
    def flushInput()

    /**
     * Use this method to write an object to the client.
     * @param obj The object that should be written to the client.
     */

    def writeToClient(obj:Object)

    /**
     * Use this method to read an object from the server.
     * @return The object that was read from the server.
     */
    def readFromClient():Object
}