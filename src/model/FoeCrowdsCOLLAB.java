package model;

import controller.EventHandler;
import event.Event;
import event.RXEvent;
import event.SimulationFinishedEvent;
import event.TXEvent;

/**
 * Foe for the crowds Protocol. 
 * 
 * Foe for the crowds Protocol. 
 * This is basically a "change packet"(CP) Foe, which upon receiving a packet, 
 * changes the content of the packet and then sends this packet to the next node. 
 * This could be used to insert malicious data or just to corrupt a communication.
 * 
 * @author stischle
 */

public class FoeCrowdsCOLLAB extends Node{
	
	public FoeCrowdsCOLLAB(){
		super();
	}

	public FoeCrowdsCOLLAB(int id, String name){
		super();
		super.setName(name);
		super.setId(id);

	}
	
	/**
	 * This is probably useless since a foe wouldn't really send something through the network
	 */
	public TXEvent startCommunication(){
		/**
		 * Get a random node from all existing, to start the communication with
		 */
		
		Network network = Network.getInstance();
		int n = (int) (getSecureRandomNumber() * network.getAllNodes().size());
		Node receiver = network.getAllNodes().get(n);
		
		TXEvent startEvent = new TXEvent(this, receiver);
		startEvent.setLayer7Flag(true); //The initial Event is on Layer7, followed by L3 events
		System.out.println("Start Communication from: " + this.getName() + " to: " + receiver.getName());
		return startEvent;	
	}
	
	public Event receive(Node sNode, Node rNode, Paket p) {
		
		/** PacketList FIFO */
		System.out.println("L7 | Receive: \tNode: " + this.getId() + " received Packet: " + p.getId() + "  with payload: '" + p.getPayload() + "'");
		
		/**
		 * Crowds Probability node logic 
		 * for new protocolls implement a new node class and overwrite receive and transmit 
		 */

		Node endNode = EventHandler.getInstance(sNode , rNode, p).getEndNode();
		
		if(endNode.equals(rNode)){ //receive the last Package	
			SimulationFinishedEvent eFinish = new SimulationFinishedEvent(rNode, endNode);	
			eFinish.setLayer7Flag(true); 
			return (Event)eFinish;	
		}
		
		double pForward = (double) Network.getInstance().getProbabilityForward();			
		double pRandom  = getSecureRandomNumber();
		
		/**
		 * If the Node which gets the Paket is not the Endnode then do another Event
		 * // If the Endnode is choosen before by probability or the Probability to forward is smaller than a random number		
		 */
		if(pForward > pRandom){		
			
			System.out.println("--- Get random node for next transmit --- ");
			
			int n = (int) (getSecureRandomNumber() * Network.getInstance().getAllNodes().size());
			Node receiver = Network.getInstance().getAllNodes().get(n);
			TXEvent txe = new TXEvent(rNode, receiver);
			txe.setLayer7Flag(true);
			
			System.out.println("--- " + rNode +" -> " + receiver );
			return (Event)txe;						
		}
		else{
			 System.out.println("--- End with transmit to final receiver --- ");
			TXEvent txe = new TXEvent(rNode, endNode);
			txe.setLayer7Flag(true);
			return (Event)txe;		
		}
				
	}
	
	public Event receiveLayer3(Node sNode, Node rNode, Node receiver, Paket p){
		
		Network.getInstance();
		/**
		 * Add Information about the previous Node to the collabAL 
		 */
		Network.addCollabInformation(sNode);
		
		//if(sNode.equals(receiver)){ //receive the last Package
		if(receiver == null){
		// Add a Layer 7 RXEvent 
			RXEvent endLayer3Event = new RXEvent(sNode, rNode);
			endLayer3Event.setLayer7Flag(true);
			//SimulationFinishedEvent txe1 = new SimulationFinishedEvent(sNode, receiver);	
			return endLayer3Event;	
		}
		
		System.out.println("L3 | \tNode: " + this.getId() + " received Packet: " + p.getId() + "  with payload: '" + p.getPayload() + "'");
		TXEvent txe = new TXEvent(rNode, receiver);
		return (Event)txe;	
	}
	
	/** Gets the Paket and sends it to the nextnode which is referenced in the LinkedList of Nodes  
	 * @return */
	public RXEvent transmitLayer3(Node initNode, Node nextNode, Paket p) {
	
		nextNode.setP(p);
		System.out.println("L3 | sending from : \tnode: " + initNode.getId() + " to " + nextNode.getId() +  " payload: " + p.getPayload());
		RXEvent rxe = new RXEvent(initNode, nextNode); 
		return rxe;
	}
}
	