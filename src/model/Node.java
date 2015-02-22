package model;



import interfaces.INode;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;

import controller.EventHandler;
import event.Event;
import event.RXEvent;
import event.SimulationFinishedEvent;
import event.TXEvent;

/**
 * @author Thomas
 * Node For the crowds Communication
 */
public class Node implements INode {

	private Paket p;
	private String name;
	private int id;

	
	public Node() {
		super();
	}
	/**
	 * Test
	 * @param id
	 * @param name
	 */
	public Node(int id, String name) {
		super();
		this.name=name;		
		this.id = id;		
	}

	/** For the server node
	 * 
	 * @param name
	 */
	public Node(String name) {
		super();
		this.name=name;		
	}

	public TXEvent startCommunication(){
		/**
		 * Get a random node from all existing, to start the communication with
		 */
		
		Network network = Network.getInstance();
		int n = (int) (getSecureRandomNumber() * network.getAllNodes().size());
		Node receiver = network.getAllNodes().get(n);
		this.setP(EventHandler.getPaket());
		TXEvent startEvent = new TXEvent(this, receiver);
		
		System.out.println("L7 | Start Communication from: " + this.getId() + " to: " + receiver.getId());
		return startEvent;
		
	}
	
	
	
	public Event receive(Node sNode, Node rNode, Paket p) {

		/** PacketList FIFO */
		System.out.println("L7 | Receive: \tNode: " + this.getId() + " received Packet: " + p.getId() + "  with payload: '" + p.getPayload() + "'");
//		this.setP(p);		
		
		/**
		 * Crowds Probability node logic 
		 * for new protocolls implement a new node class and overwrite receive and transmit 
		 */
	
		EventHandler ev = EventHandler.getInstance(sNode,rNode,p);
		Node endNode = ev.getEndNode();
		
		if(endNode.equals(rNode)){ //receive the last Package	
			SimulationFinishedEvent eFinish = new SimulationFinishedEvent(rNode, endNode);	
			eFinish.setLayer7Flag(true); 
			return (Event)eFinish;	
		}
		
		double pForward = Double.parseDouble(Network.getParameter("pf").toString());		
		double pRandom  = getSecureRandomNumber();
		
		/**
		 * If the Node which gets the Paket is not the Endnode then do another Event
		 * // If the Endnode is choosen before by probability or the Probability to forward is smaller than a random number		
		 */
		if(pForward > pRandom){		
		
			System.out.println("L7 | --- Get random node for next transmit --- ");
			
			int n = (int) (getSecureRandomNumber() * Network.getInstance().getAllNodes().size());
			Node receiver = Network.getInstance().getAllNodes().get(n);
			TXEvent txe = new TXEvent(rNode, receiver);
			txe.setLayer7Flag(true);
			
			System.out.println("--- " + rNode +" -> " + receiver );
			return (Event)txe;						
		}
		else{
			 System.out.println("L7 | --- End with transmit to final receiver --- ");
			TXEvent txe = new TXEvent(rNode, endNode);
			txe.setLayer7Flag(true);
			return (Event)txe;		
		}
	}
	
	/**
	 * receiver is the next node in the list of Dijkstra order
	 * @param sNode
	 * @param receiver
	 * @param p
	 * @return
	 */
	public Event receiveLayer3(Node sNode, Node rNode, Node receiver, Paket p){
		
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
	public RXEvent transmit(Node initNode, Node nextNode, Paket p) {
		
		/** Send the Packet to the next Node */	
		
		System.out.println("L7 | \tnode: " + initNode.getId() + " to " + nextNode.getId() +  " payload: " + p.getPayload());	
		nextNode.setP(p);
		RXEvent rxe = new RXEvent(initNode, nextNode); 
		return rxe;
	}
	
	public RXEvent transmitLayer3(Node initNode, Node nextNode, Paket p){
	/** Send the Packet to the next Node */	
		
		System.out.println("L3 | sending from : \tnode: " + initNode.getId() + " to " + nextNode.getId() +  " payload: " + p.getPayload());	
		nextNode.setP(p);
		RXEvent rxe = new RXEvent(initNode, nextNode); 
		return rxe;
	}
	
	/**
	 * @return the p
	 */
	public Paket getP() {
		return p;
	}
	/**
	 * @param p
	 *            the p to set
	 */
	public void setP(Paket p) {
		this.p = p;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString(){
		return " id: " + this.getId();
	}

	public float getSecureRandomNumber(){
	  	  SecureRandom random = new SecureRandom();	      
		  return random.nextFloat();
	}

}
