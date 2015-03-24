package interfaces;

import java.util.ArrayList;

import data.Paket;
import event.Event;
import event.RXEvent;
import event.TXEvent;
import model.Node;

/**
 * Interface for nodes
 * 
 * receive -> Layer7  --- "direct sending"
 * receiveLayer3 -> Layer3  --- "physical path"
 * 
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

	public RXEvent transmitLayer3 (Node initNode, Node nextNode, Paket p);
	
	public TXEvent startCommunication();
	
	public Paket getP();
	public void setP(Paket p);
	public String getName();
	public void setName(String name);
	public int getId();
	public void setId(int id);
}



