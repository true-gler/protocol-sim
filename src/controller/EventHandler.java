package controller;

import interfaces.IAlgorithm;
import interfaces.IProtocol;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import data.Network;
import data.Paket;
import algorithm.Dijkstra;
import model.Node;
import event.Event;
import event.RXEvent;
import event.SimulationFinishedEvent;
import event.TXEvent;

/**
 * Main Class of Communications between nodes in a network
 * The EventHandler simulates the communication due to the use of events
 * Events are stored in a PriorityQueue and performed sequentially FIFO
 * 
 * @author Thomas
 * 
 */
public class EventHandler {

	/* Event data */
	private PriorityQueue<Event> queue;
	private static EventHandler instance = null;
	
	/* Messages and Debugging */
	private static boolean debug = false;
	
	/* Nodes */
	private static Node startNode;
	private static Node endNode;
	private static Paket paket;

	private IAlgorithm algorithm;
	private IProtocol protocol;
	private LinkedList<Node> listForCommunication;
	
	/* Logging */
	private static LogHandler lh;
	
	/**
	 * The returned hashmap of the Dijkstra-Algorithm, containing all shortest
	 * paths (to all nodes) for a node
	 * 
	 * The Hashmap to store the processed HashMaps (Paths) for every node
	 */
	

	public static EventHandler getInstance(Node initNode, Node endNode, Paket paket) {
		if (instance == null) {
			setStartNode(initNode);
			startNode.setP(paket);
			instance = new EventHandler(initNode, endNode, paket);
		}
		return instance;
	}

	public EventHandler(Node initNode, Node endNode, Paket paket) {
		queue = new PriorityQueue<Event>();			
		this.setStartNode(initNode);
		this.setEndNode(endNode);
		this.setPaket(paket);
		lh = LogHandler.getInstance();
		algorithm = (IAlgorithm) Network.getAlgorithm();
		protocol = (IProtocol) Network.getProtocol();
	}

	
	public boolean simulate(Network n) {
		
		protocol.executePreSimulation(null);
		
		/**
		 * Let the node choose which the next node is
		 */	
	//	startNode.setP(this.paket);

		
		TXEvent startEvent = getStartNode().startCommunication();
		if(startEvent == null) //DoS foe 
		{
			System.out.println("Communication stopped, no Event");
			lh.appendData("Communication stopped, no Event");
			return false;
		}
		startEvent.setLayer7Flag(true); //The initial Event is on Layer7, followed by L3 events
		this.addEvent(startEvent);
		
		/**
		 * Eintrag f�r das Initiale Senden (nicht start Event)
		 */
		TXEvent initEvent = new TXEvent(startEvent.getInitNode(), this.getEndNode());
		initEvent.setLayer7Flag(true);
		String output1 = lh.formattingLogOutput(initEvent);
		lh.appendData(output1);
		/**
		 * Ende
		 */
		Event e = null;
		long begin = System.currentTimeMillis();
		try {
			while (!this.queue.isEmpty()) {
					e = this.queue.poll(); // gets and deletes
					
					/**
					 * Check if the attack is a DOS-Attack
					 * If a DOS happens, we have no package that gets transmitted
					 */
					try {
						e.getInitNode().getP();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						return false;
					}

					// Formatting for CSV File
					String output = lh.formattingLogOutput(e);
					lh.appendData(output);
					
					if (e instanceof SimulationFinishedEvent) {
						executeEvent(e);									
						return false;
					}
					else
						executeEvent(e);
				}

		} catch (Exception e1) {
			if(debug) e1.printStackTrace();
			System.out.println("Something wrong with simulate");
		} finally {
			long end = System.currentTimeMillis();
			System.out.println((end - begin) + "ms");
			
			
			protocol.executePostSimulation(null); 
			
			
			lh.writeTotalTime((end-begin));
		}
		return true;
	}

	/**
	 * Am Anfang sollte ein L7 Event vom InitNode erzeugt werden (ich glaube
	 * aber das gibt es schon in der Queue -> vom Node erzeugt sonst würde er
	 * gar nicht zu der erzeugung aus der linkedlist kommen) Dann ein L3 zu dem
	 * Initnode, dann sollte die LinkedList durchgegangen werden und L3 Events
	 * erzeugt werden. zum Schluss noch das L7 Event für den receiverNode.
	 * 
	 * Allerdings ist die Frage ob das nicht schon vom Node erzeugt wird
	 * 
	 */
	private void executeEvent(Event e) {
		Node sNode = ((Event) e).getInitNode();
		Node rNode = ((Event) e).getreceiverNode();
	
		
		/**
		 * Simulation Finished
		 */
		
		if (e instanceof SimulationFinishedEvent) {
			// Invokes the last receive
			rNode.receive(sNode,rNode, sNode.getP());
			protocol.executeFinished(null);
		}
		/**
		 * receive
		 */
		else if (e instanceof RXEvent) {
			if(e.isLayer7Flag()){
				Event ev = rNode.receive(sNode, rNode, sNode.getP());
				this.addEvent(ev);
			}
			
			/**
			 * Layer 3 is different because no random choose of the next destination -> it takes the next out of the list
			 */
			else{
				if(listForCommunication.size() > 0){
				Event ev = rNode.receiveLayer3(sNode, rNode, listForCommunication.pollFirst(), sNode.getP()); 
				// if pollLast returns null then there is no node left in the listForComm and the returning Event(ev) is a L7 RX Event
				this.addEvent(ev);
				}
				else {  // Für den Fall dass der Dijkstra den Pfad aus 2 Elementen baut - dann würde dieses pollLast (null) liefern und eine Exception 
						//schmeissen 
					Event ev = rNode.receiveLayer3(sNode, rNode, null, sNode.getP());
					this.addEvent(ev);
				}
			}
			
			protocol.executeRX(null);

			/**
			 * Layer 7 events are splitted to L3 Events Layer 7 events don't get
			 * into the queue
			 */
		} else 
			
			
		if (e instanceof TXEvent) {
			if (e.isLayer7Flag()) { //Only goes in there for the first Time

				/**
				 * Get the shortestPath (Dijkstra) for the sending Node if it is
				 * already in the hashmap (processedDijkstraNodes) then get it
				 * out of it else process the shortest Path for the Node and
				 * store it for further use
				 */
				try {
					
					/** holds the path to go for
					* layer 3 but in reverse order
					*/
					/***************************FEHLER*********************************************/
					 
					listForCommunication = algorithm.getPath(sNode, rNode);								
					if(listForCommunication == null) {
						lh.appendData("Node cannot be reached");
						System.out.println("Node cannot be reached");
						//this.addEvent(new SimulationFinishedEvent(System.currentTimeMillis()));
					} 
					
					else {
						System.out.println(listForCommunication.toString());
					
					
					/**
					 * invoking L3 Event for Layer 3 communication
					 * the list of dijkstra nodes to go simply becomes shorter
					 */
					
					if(listForCommunication.size() > 1){
						Node firstNode = listForCommunication.pollFirst();
						Node nodeToSendTo = listForCommunication.pollFirst();					
						firstNode.setP(sNode.getP());
						TXEvent eFirst = new TXEvent(firstNode, nodeToSendTo);
						eFirst.setLayer7Flag(false); //Layer 3
						this.addEvent(eFirst);
					} else /*
						Sending to the same node
					*/
					{
						RXEvent eFirst = new RXEvent(sNode, rNode);
						eFirst.setLayer7Flag(true); //Layer 7
						this.addEvent(eFirst);
					}
					}

				} catch (Exception e1) {
				
					if(debug) e1.printStackTrace();
				}
			}
			/**
			 * Layer3 just execute 
			 */
			else {
				
				try {					
					RXEvent eLayer3 = rNode.transmitLayer3(sNode, rNode, sNode.getP());
					eLayer3.setLayer7Flag(false);
					this.addEvent(eLayer3);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					if(debug)  e1.printStackTrace();
				}
			}
			
			protocol.executeTX(null);
		}
		else { // Dieser Fall sollte nicht eintreten
				System.out.println("!! unknown Event!?");
		}		
		if (debug) {
			try {
				this.printQueue();
			} catch (Exception e2) {
				if(debug) e2.printStackTrace();
			}
		}
	}

	/**
	 * Adds a Event to the queue
	 * 
	 * @param e
	 */
	public void addEvent(Event e) {
		if (debug)
			if (e instanceof TXEvent){
				if(e.isLayer7Flag())
					System.out.print("L7 ");
				else 
					System.out.print("L3 ");
				
				 	System.out.println("| Event added "
					+ e.getClass() + " Time: " + e.getTimestamp()
					+ " InitNode: " + e.getInitNode().getName().toString()
					+ " receiver " + e.getreceiverNode().getId());
			}
			else if (e instanceof RXEvent){
				if(e.isLayer7Flag())
					System.out.print("L7 ");
				else 
					System.out.print("L3 ");
				
				System.out.println("| Event added "
						+ e.getClass() + "Time: " + e.getTimestamp()
						+ " Receiver " + e.getreceiverNode().getId());
			}
			

		this.queue.add(e);
	}

	public void printQueue() {
		System.out.println("\t---- Queue Elements -----");
		java.util.Iterator<Event> it = this.queue.iterator();
		while (it.hasNext()) {
			System.out.println(" \t" + it.next().toString());
		}
	}
	

	
	public static Node getStartNode() {
		return startNode;
	}
	public static void setStartNode(Node startNode) {
		EventHandler.startNode = startNode;
	}
	public static Node getEndNode() {
		return endNode;
	}

	public static void setEndNode(Node endNode) {
		EventHandler.endNode = endNode;
	}

	public static Paket getPaket() {
		return paket;
	}

	public static void setPaket(Paket paket) {
		EventHandler.paket = paket;
	}




}
