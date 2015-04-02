package model;

import protocol.Crowds;
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

public class FoeCrowdsCOLLAB extends Node{
	
	public FoeCrowdsCOLLAB(){
		super();
	}

	public FoeCrowdsCOLLAB(int id, String name){
		super();
		super.setName(name);
		super.setId(id);

	}
	
	public Event receiveLayer3(Node sNode, Node rNode, Node receiver, Paket p){
		
		Network.getInstance();
		/**
		 * Add Information about the previous Node to the collabAL 
		 */
		Crowds.addCollabInformation(sNode);
		
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
}
	