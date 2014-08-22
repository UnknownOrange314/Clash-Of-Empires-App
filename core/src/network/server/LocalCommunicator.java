package network.server;


import java.util.concurrent.ConcurrentLinkedQueue;
import server.model.mapData.GameOption;
import engine.general.network.DisplayCommunicator;

public class LocalCommunicator extends DisplayCommunicator{

	final GameOption options;
	final int some1;
	
    ConcurrentLinkedQueue<Object> inputQueue=new ConcurrentLinkedQueue<Object>();
    ConcurrentLinkedQueue<Object> outputQueue=new ConcurrentLinkedQueue<Object>();
    
	public LocalCommunicator(int s, GameOption opt){
		options=opt;
		some1=s;
	}
	
    public void writeToClient(Object obj){
        outputQueue.add(obj);
    }

    public boolean emptyOutput(){
    	return outputQueue.isEmpty();
    }
    
    public Object getOutput(){
       return outputQueue.poll();
    }

    public Object readFromClient(){
        if (inputQueue.isEmpty()){
            return null;
        }
        return inputQueue.poll();
    }

    public void writeToServer(Object obj){
        inputQueue.add(obj);
    }

    //TODO: Modify the abstract superclass DisplayCommunicator.java so this method is not necessary.
	@Override
	public void flushInput() {
		
		
	}
}