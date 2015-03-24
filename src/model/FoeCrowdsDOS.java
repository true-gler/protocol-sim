package model;



import controller.EventHandler;
import data.Paket;
import event.Event;
import event.RXEvent;
import event.SimulationFinishedEvent;
import event.TXEvent;

/**
 * Foe for the crowds Protocol. 
 * This is basically a "denyal of service"(DOS) Foe, which upon receiving a packet, won't 
 * forward it and therefore stop the communication
 *
 * @author simon, thomas
 */

public class FoeCrowdsDOS extends Node{

	public FoeCrowdsDOS(){
		super();
	}

	public FoeCrowdsDOS(int id, String name){
		super();
		super.setName(name);
		super.setId(id);
	}
	
	public Event receive(Node sNode, Node rNode, Paket p) {

		/** PacketList FIFO */
		System.out.println("L7 | Receive: \tNode: " + this.getId() + " received Packet: " + p.getId() + 
			"  with payload: '" + p.getPayload() + "'");
				
		SimulationFinishedEvent simFin = new SimulationFinishedEvent(sNode, sNode);
		return (Event)simFin;
	}
	
	public RXEvent transmitLayer3(Node initNode, Node nextNode, Paket p){
		
		/**
		 * This is where your paket should be transmitted, but it simply doesn't
		 */
		System.out.println("No paket transmitted. DoS Node aborted transmit");
		
		RXEvent rxe = new RXEvent(null, null);
		return rxe;
	}
	
	/**
	 * This is probably useless since a foe wouldn't really send something through the network
	 */
	public TXEvent startCommunication(){

		return null;
	}
	
	
}