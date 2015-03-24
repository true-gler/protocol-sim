/**
 * 
 */
package event;

import model.Node;

/**
 * The simulationfinished event is used for the notification to the eventhandler to finish the simulation
 * @author Thomas
 *
 */
public class SimulationFinishedEvent extends Event{

	public SimulationFinishedEvent(long timestamp) {
		super(timestamp);
		// TODO Auto-generated constructor stub
	}

	public SimulationFinishedEvent(Node thisNode, Node receiverNode) {
		super(System.currentTimeMillis());
		super.setInitNode(thisNode);
		super.setreceiverNode(receiverNode);
	
	}
	public String toString(){
		if(this.isLayer7Flag())
			return "L7:" + this.getClass() +" TimeStamp: "+ this.getTimestamp() + " receiver: "+ this.getreceiverNode().getId();
		else 
			return "L3:" + this.getClass() +" TimeStamp: "+ this.getTimestamp() + " receiver: "+ this.getreceiverNode().getId();

	}

}
