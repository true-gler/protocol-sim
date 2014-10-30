package interfaces;

import java.util.ArrayList;

import event.Event;
import event.RXEvent;
import event.TXEvent;
import model.Node;
import model.Paket;

/**
 * @author Thomas
 *
 */
public interface INode{

	public Event receive(Node initNode, Node nextNode, Paket p);
	
	public Event receiveLayer3(Node initNode, Node nextNode, Node receiver, Paket p);
	
	/**
	 * The inital Node-payload must be set in the Gui.
	 * The following transmitted payload is the initialized
	 * @param nextNode
	 * @return 
	 */
	public RXEvent transmit(Node initNode, Node nextNode, Paket p);
	/** hier evtl Broadcast */

	public RXEvent transmitLayer3 (Node initNode, Node nextNode, Paket p);
	
	public ArrayList<RXEvent> broadcast(Node initNode);
	
	/*
	 * multicast needs to be checked, we need a different number of functions,
	 * since we need a different number of Nodes to mulitcast to.
	 * What happens to the multicast
	 * public abstract ArrayList<RXEvent> multicast(Node initNode, Node 1, Node 2)
	 */
		
	public ArrayList<RXEvent> multicast (Node initNode, ArrayList<Node> receivers);
	
	//add transmitLayer3 and receiveLayer3 abstract method to the interface of INODE


}



