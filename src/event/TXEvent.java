package event;

import model.Node;

/**
 * @author Thomas
 *
 */
public class TXEvent extends Event{
	
	
	public TXEvent(Node initNode, Node receiverNode){
		super(System.currentTimeMillis());		
		super.setInitNode(initNode);
		super.setreceiverNode(receiverNode);
	}
	
	
	
}
