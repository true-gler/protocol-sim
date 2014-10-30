package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;



import java.util.List;
import java.util.Map.Entry;

import model.Network;
import model.Node;
import model.ReachableList;
import model.ReachableNodes;
import event.Event;
import event.TXEvent;

/**
 * @author Simon
 * 
 * Problems: 
 * 		- Every way is output twice
 * 		- Also longer paths than the shortest one are given.
 */

public class Dijkstra {

	private boolean debug = true;

	/*
	 * Dijkstra Tripel: (Node, connectedNode, Latency)
	 */
	private class dijkstraTripel {

		public dijkstraTripel(Node v, double latency, Node previousV) {
			super();
			this.v = v;
			this.previousV = previousV;
			this.latency = latency;
		}

		private Node v;
		private Node previousV;
		private double latency;

		public Node getV() {
			return v;
		}

		public void setV(Node v) {
			this.v = v;
		}

		public Node getPreviousV() {
			return previousV;
		}

		public void setPreviousV(Node nextV) {
			this.previousV = nextV;
		}

		public double getLatency() {
			return latency;
		}

		public void setLatency(double latency) {
			this.latency = latency;
		}
	}

	/**
	 * This class creates a Hashmap, taking every Node in the Network as a key
	 * and stores the LinkedList of reachable Nodes for this node under this
	 * key.
	 * 
	 * @param n
	 * @param nForPath
	 * @return A Hashmap of Nodes and a LinkedList of the reachable nodes for
	 *         every node in the network.
	 */
	
	public HashMap<Node, LinkedList<Node>> getShortestPaths(Node nForPath) {

		Network n = Network.getInstance(); //Get the network

		ArrayList<dijkstraTripel> nodesCovered = new ArrayList<dijkstraTripel>(); 
		ArrayList<dijkstraTripel> edgeNodes = new ArrayList<dijkstraTripel>(); //Nodes that are reachable in each state

		HashMap<Node, LinkedList<Node>> retDijkstra = new HashMap<Node, LinkedList<Node>>(); //The HashMap to return
		
		dijkstraTripel startTripel = new dijkstraTripel(nForPath, 0, nForPath); //Starttripel to itself
		nodesCovered.add(startTripel);
		
		// Get the reachable nodes for the first Node
		LinkedList<ReachableNodes> ll = n.getNodesToReach().get(nForPath.getId()).getLl();
		for (int i = 0; i < ll.size(); i++) {
			ReachableNodes rn = (ReachableNodes) ll.get(i);
			Node currentV = rn.getN();
			double latency = rn.getLatency();
			dijkstraTripel dt = new dijkstraTripel(currentV, latency, nForPath);
			edgeNodes.add(dt);
		}

		// As long as not all the nodes are covered, keep going
		while (edgeNodes.isEmpty() == false) {
			double lat = Double.MAX_VALUE;
			int id = Integer.MIN_VALUE;
			for (int i = 0; i < edgeNodes.size(); i++) { //Get the best next Node
				if (lat > edgeNodes.get(i).getLatency()) {
					lat = edgeNodes.get(i).getLatency();
					id = i;
				}
			}

			dijkstraTripel bestTriple = edgeNodes.get(id);
			edgeNodes.remove(id);
			Node nodeToDijkstra = bestTriple.getV();
			LinkedList<ReachableNodes> listForCurrentNode = n.getNodesToReach().get(nodeToDijkstra.getId()).getLl();
			
			for (int i = 0; i < listForCurrentNode.size(); i++) {
				/*
				 * Überprüfen ob Knoten schon abgearbeitet, dann aus, sonst
				 * Randknoten dazunehmen
				 */
				ReachableNodes rn = (ReachableNodes) listForCurrentNode.get(i);

				/**
				 * Wenn Knoten schon in den covered Nodes ist, darf er zu den
				 * edgeNodes nichtmehr hinzugegeben werden!
				 */
				
				int index;
				index = isInList(rn.getN(), nodesCovered);
				if(index == -1){ //Not covered
					index = isInList(rn.getN(), edgeNodes);
					if(index != -1){//Allready in the edgeNodes
						if (edgeNodes.get(index).getLatency() > lat + rn.getLatency()) {
							edgeNodes.get(index).setLatency(lat);
							edgeNodes.get(index).setPreviousV(nodeToDijkstra);
						}
					}
					else {
						
						dijkstraTripel newEdgeNode = new dijkstraTripel(rn.getN(), lat + rn.getLatency(), nodeToDijkstra);
						edgeNodes.add(newEdgeNode);
					
					}
				}
				nodesCovered.add(bestTriple);
					
			}
			//System.out.println(bestTriple.previousV.getId() + " ; " + bestTriple.v.getId());
			
			/*
			 * At this point, all Nodes in the Network are covered We now
			 * determine the shortest path to every node, and find the shortest
			 * path between the two nodes that we want to communicate with each
			 * other.
			 */

			for (int i = 0; i < nodesCovered.size(); i++) {
				dijkstraTripel dtest = nodesCovered.get(i);
				LinkedList<Node> nodeList = new LinkedList<Node>();
				nodeList.add(dtest.v);
				dijkstraTripel toHashMap = dtest;
				while (!dtest.previousV.equals(dtest.v)) {
					dtest = previousTriple(nodesCovered, dtest);
					nodeList.add(dtest.v);
				}
				Collections.reverse(nodeList);
				retDijkstra.put(toHashMap.v, nodeList);
			}
		}
		
		
		return retDijkstra;

	}
	
	/**
	 * This function returns a DijkstraTripel, that is the previous triple on
	 * the way to the start Node
	 * 
	 * @param ArrayList
	 *            <dijkstraTripel> nodes
	 * @param dijkstraTripel
	 *            dt
	 * @return dijkstraTripel retTripel
	 */

	/*
	 * Function that returns a dT, which is the previous triple on the way
	 */
	public dijkstraTripel previousTriple(ArrayList<dijkstraTripel> nodes,
			dijkstraTripel dt) {

		/*
		 * Create a dT retTripel, that is the previous triple
		 */

		dijkstraTripel retTripel = null;
		//
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).v.equals(dt.previousV)) {
				retTripel = nodes.get(i);
				break;
			}
		}

		return retTripel;
	}

	public int isInList(Node test, ArrayList<dijkstraTripel> list) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getV().equals(test))
				return i;
		}
		return -1;
	}
}
