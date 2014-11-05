package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import model.Network;
import model.Node;
import model.Paket;
import event.Event;
import event.RXEvent;
import event.SimulationFinishedEvent;
import event.TXEvent;

/**
 * Main Class of Communications between Nodes in a Network
 * 
 * @author Thomas
 * 
 */
public class EventHandler {

	/* Event data */
	private PriorityQueue<Event> queue;
	private Network network = null;
	private static EventHandler instance = null;
	
	/* Messages and Debugging */
	private static boolean debug = false;
	
	/* Nodes */
	private Node startNode;
	private Node endNode;
	private Paket paket;

	private Dijkstra dijkstra = new Dijkstra();
	private LinkedList<Node> listForComm;
	
	/* Logging */
	private static LogHandler lh = new LogHandler();
	
	/**
	 * The returned hashmap of the Dijkstra-Algorithm, containing all shortest
	 * paths (to all nodes) for a node
	 * 
	 * The Hashmap to store the processed HashMaps (Paths) for every node
	 */
	private static HashMap<Node, HashMap> allProcessedNodes;

	public static EventHandler getInstance(Node initNode, Node endNode, Paket paket) {
		if (instance == null) {
			instance = new EventHandler(initNode, endNode, paket);
		}
		return instance;
	}

	public EventHandler(Node initNode, Node endNode, Paket paket) {
		queue = new PriorityQueue<Event>();
		setNetwork(Network.getInstance());
		allProcessedNodes = new HashMap<Node, HashMap>();
		this.setStartNode(initNode);
		this.setEndNode(endNode);
		this.setPaket(paket);
		startNode.setP(paket);
	}

	
	public boolean simulate(Network n) {
		
		try {
			lh.writeHeader();
		} catch (IOException e2) {
			System.out.println("log write error");
		}
		/**
		 * Let the node choose which the next node is
		 */	
		TXEvent startEvent = getStartNode().startCommunication();
		startEvent.setLayer7Flag(true); //The initial Event is on Layer7, followed by L3 events
		this.addEvent(startEvent);

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
						this.executeEvent(e);
						return false;
					}
					else
						this.executeEvent(e);
				}

		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Something wrong with simulate");
		} finally {
			long end = System.currentTimeMillis();
			System.out.println((end - begin) + "ms");
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
			rNode.receive(sNode,rNode, rNode.getP());			
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
				if(listForComm.size() > 0){
				Event ev = rNode.receiveLayer3(sNode, rNode, listForComm.pollFirst(), sNode.getP()); 
				// if pollLast returns null then there is no node left in the listForComm and the returning Event(ev) is a L7 RX Event
				this.addEvent(ev);
				}
				else {  // Für den Fall dass der Dijkstra den Pfad aus 2 Elementen baut - dann würde dieses pollLast (null) liefern und eine Exception 
						//schmeissen 
					Event ev = rNode.receiveLayer3(sNode, rNode, null, sNode.getP());
					this.addEvent(ev);
				}
			}

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
					
					listForComm = null; 
					try {
						if (allProcessedNodes.get(sNode).get(rNode) != null) {
							listForComm = (LinkedList) allProcessedNodes.get(sNode).get(rNode); // Get the path if it exists for
											// this node
						}
					} catch (NullPointerException ex) {
						allProcessedNodes.put(sNode, dijkstra.getShortestPaths(sNode));
						// Process the Dijkstra for the node to get the path
						
						listForComm = (LinkedList) allProcessedNodes.get(sNode).get(
								rNode); // and the the path
					}
					
					System.out.println(listForComm.toString());
					
					/**
					 * invoking L3 Event for Layer 3 communication
					 * the list of dijkstra nodes to go simply becomes shorter
					 */
					
					if(listForComm.size() > 1){
						Node firstDijkstraNode = listForComm.pollFirst();
						Node nodeToSendTo = listForComm.pollFirst();					
						firstDijkstraNode.setP(sNode.getP());
						TXEvent eFirst = new TXEvent(firstDijkstraNode, nodeToSendTo);
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
						

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			/**
			 * Layer3 just execute 
			 */
			else {
				RXEvent eLayer3 = rNode.transmitLayer3(sNode, rNode, sNode.getP());
				eLayer3.setLayer7Flag(false);
				this.addEvent(eLayer3);
			}
		}
		else { // Dieser Fall sollte nicht eintreten
				System.out.println("!!!!!!!!! unknown Event!?");
		}

		if (debug) {
			try {
				this.printQueue();
			} catch (Exception e2) {
				e2.printStackTrace();
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
	

	
	/**
	 * Instantiates the network for communication
	 * 
	 * @param network
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}
	public Node getStartNode() {
		return startNode;
	}
	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}
	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public Paket getPaket() {
		return paket;
	}

	public void setPaket(Paket paket) {
		this.paket = paket;
	}




}
