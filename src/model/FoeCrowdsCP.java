package model;

import controller.EventHandler;
import data.Network;
import data.Paket;
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
 * @author simon, thomas
 */

public class FoeCrowdsCP extends Node{

	
	
	public FoeCrowdsCP(){
		super();
	}

	public FoeCrowdsCP(int id, String name){
		super();
		super.setName(name);
		super.setId(id);

	}

	/** Gets the Paket and sends it to the nextnode which is referenced in the LinkedList of Nodes  
	 * @return */
	public RXEvent transmitLayer3(Node initNode, Node nextNode, Paket p) {
		
		/**
		 * Change the content of the package and then send it to the next node
		 *
		 */	
		System.out.println("L3 | sending from : \tnode: " + initNode.getId() + " to " + nextNode.getId() +  " payload: " + p.getPayload());
		p.setPayload("changed");
		nextNode.setP(p);
		RXEvent rxe = new RXEvent(initNode, nextNode); 
		return rxe;
	}
}
	