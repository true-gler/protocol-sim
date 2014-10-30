package event;

import model.Node;


/**
 * @author Thomas
 *
 */
public class RXEvent  extends Event {
	
	
	public  RXEvent (Node initNode, Node receiverNode){
		super(System.currentTimeMillis());
		super.setreceiverNode(receiverNode);
		super.setInitNode(initNode);
	}

}
