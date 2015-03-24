/**
 *  
 */
package event;

import data.Paket;
import model.Node;

/**
 * The ecent class represents the main event that is used to send packages 
 * in the network
 * RxEvent and TxEvent extend the Event class
 * @author Thomas
 *
 */

public class Event implements Comparable<Event>{

	   private Long timestamp;	 
	   private boolean Layer7Flag;
	   private Node initNode = null;
	   private Node receiverNode = null;

	   public Node getInitNode() {
			return initNode;
		}
		public void setInitNode(Node initNode) {
			this.initNode = initNode;
		}
			
	    public int compareTo(Event e1) {
	    	int a = 0;
	    	try {
				a= this.getTimestamp().compareTo(e1.getTimestamp());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//why come is here a failure :D
			}
	        return a;
	    }

	    public Event(long timestamp) {      
	        this.timestamp = timestamp;
	    }

	    public Long getTimestamp() {
	        return timestamp;
	    }
		public boolean isLayer7Flag() {
			return Layer7Flag;
		}
		public void setLayer7Flag(boolean layer7Flag) {
			Layer7Flag = layer7Flag;
		}
		
		public Node getreceiverNode() {
			return receiverNode;
		}
		public void setreceiverNode(Node receiverNode) {
			this.receiverNode = receiverNode;
		}
		public String toString(){
			if(this.isLayer7Flag())
				return "L7:" + this.getClass() +" TimeStamp: "+ this.getTimestamp() + " receiver: "+ this.getreceiverNode().getId();
			else 
				return "L3:" + this.getClass() +" TimeStamp: "+ this.getTimestamp() + " receiver: "+ this.getreceiverNode().getId();

		}


}
