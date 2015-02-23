package controller;

import interfaces.IAlgorithm;
import interfaces.INode;
import interfaces.IProtocol;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import data.Network;
import data.ReachableList;
import javafx.stage.Stage;
import exceptions.NodeTypeNotFoundException;
import model.Node;

/**
 * This is the algorithm to generate the network, using the barabasi albert model.
 * According to research this algorithm models the structure of todays internet very well
 * because nodes with a large number of connections get even more connections and those
 * with fewer connections are not as likely to get new connections
 */

public class NetworkGenerator {

	private static boolean debug = false;
	//private Stage primaryStage;
	// Array List which contains all nodes
	private static ArrayList<Node> allNodes = null;

	// Array List which contains all connections between the Nodes with Latency
	private static ArrayList<ReachableList> nodesToReach = null;
	// totalAmount of Nodes
	private static int totalAmount;
	// Connections in network
	private static int totalConn = 0;

	private static int length = 0;
	
	/*
	 * Define the maximum number of connections for each node
	 * This is used to increase the performance of the system. The Dijkstra takes to long otherwise
	 */
	private final int maxNodeDegree = 20;
	
	public boolean generateNetwork(HashMap<String, Integer> hm,
			int totalNodeAmount) throws NodeTypeNotFoundException {
		Network network = Network.getInstance();
		// totalNodeAmoun is nodes + foes
		int numFoes = 0;
		int diffrentTypes = 0;
		ArrayList<String> types = new ArrayList<String>();
		ArrayList<Integer> amount = new ArrayList<Integer>();
		float p = 0;
		totalAmount = totalNodeAmount;
		float le = totalAmount / 10;
		length = (int) le;

		
		if(nodesToReach == null && allNodes == null)
			initArrayLists(totalNodeAmount);
		else
			resetArrayLists(totalNodeAmount);

		for (Map.Entry<String, Integer> entry : hm.entrySet()) {
			types.add(entry.getKey());
			amount.add(entry.getValue());
			numFoes += entry.getValue();
		}

		/**
		 * Generate all Foes and place them random over the Network
		 */
		for (int i = 0; i < amount.size(); i++) {
			for (int j = 0; j < amount.get(i); j++) {
				p = getSecureRandomNumber();
				String type = types.get(i);
				INode node = createObjectNode(type);
				if (node == null) {
					throw new NodeTypeNotFoundException();
				}
				float idf = (float) p * totalNodeAmount;
				int id = (int) idf;
				
				while (allNodes.get(id) != null) {
					p = getSecureRandomNumber();
					idf = (float) p * totalNodeAmount;
					id = (int) idf;
				}
				node.setId(id);
				node.setName(types.get(i));
				allNodes.set(id, (Node) node);
				ReachableList rl = new ReachableList(allNodes.get(id));
				nodesToReach.set(id,rl);

			}
		}
		/**
		 * Set all null Values in the arraylist to nodes
		 */
		for (int i = 0; i < totalNodeAmount; i++) {
			if (allNodes.get(i) == null) {
				Node node = new Node();
				node.setId(i);
				node.setName("Node");
				allNodes.set(i, node);
				ReachableList rl = new ReachableList(allNodes.get(i));
				nodesToReach.set(i,rl);
			}
		}
		if (totalAmount > 100) {
			if (!initBarabasi())
				return false;
			if (!barabasi())
				return false;
			checkBarabasi();
		} else {
			if (!generateRandom())
				return false;
		}

		Network.setAllNodes(allNodes);
		Network.setNodesToReach(nodesToReach);
		return true;
	}

	/**
	 * It could be that the barabasi generates subnetworks, here check it again
	 */
	private void checkBarabasi() {
		int to, lat;
		float latency, f, t;
		latency = getSecureRandomNumber() * 100;
		lat = (int) latency;
		t = getSecureRandomNumber() * totalAmount;
		to = (int) t;
				
		for(int i = 0; i < totalAmount; i ++){
			if(nodesToReach.get(i).getLl().size() <= 1){
				nodesToReach.get(i).addReachable(allNodes.get(to), lat);
				nodesToReach.get(to).addReachable(allNodes.get(i), lat);
					
			}
		}
		
	}

	/**
	 * Generates random node (for each node min 2 connections)
	 * 
	 * @return
	 */
	private boolean generateRandom() {
		int to2, to, lat;
		float latency, f, t;
		for (int i = 0; i < totalAmount; i++) {
			latency = getSecureRandomNumber() * 100;
			lat = (int) latency;
			t = getSecureRandomNumber() * totalAmount;
			f = getSecureRandomNumber() * totalAmount;
			to = (int) t;
			to2 = (int) f;
			try {
				nodesToReach.get(i).addReachable(allNodes.get(to), lat);
				nodesToReach.get(to).addReachable(allNodes.get(i), lat);
				nodesToReach.get(i).addReachable(allNodes.get(to2), lat);
				nodesToReach.get(to2).addReachable(allNodes.get(i), lat);
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	private boolean initBarabasi() {
		/*
		 * create random edges for m0 -> 4 Nodes initial
		 */
		int to, lat;
		float latency, f, t;

		for (int i = 0; i < length; i++) {
	
			float prob = 0.6F;
			while ( prob > 0.5) {
				t = getSecureRandomNumber() * length;
				to = (int) t;
				latency = getSecureRandomNumber() * 100;
				lat = (int) latency;
				//Can generate multiple connections between 2 nodes
				try {
					nodesToReach.get(i).addReachable(allNodes.get(to), lat);
					nodesToReach.get(to).addReachable(allNodes.get(i), lat);
					totalConn++;
					prob = getSecureRandomNumber();
				} catch (Exception e) {
					return false;
				}
				
			}
		}
		return true;
	}

	/**
	 * Get the Node with the highest amount of neighbours if the prob is higher
	 * than the calculated value (lt. formula) then add else get the next lower
	 * node with the smaller degree and calculate the formula
	 * 
	 * @return
	 */
	private boolean barabasi() {
		int lat;
		float latency;
		int i;
		for (i = length; i < totalAmount; i++) {			
			int count = 0; 
			for (int j = 0; j < i; j++) {
				float probAdd = getSecureRandomNumber();
				float p_i = nodesToReach.get(j).getLl().size(); 
							
				
				p_i = p_i / totalConn; 
				//System.out.println("probAdd " + probAdd + " <  p_i" + p_i + (probAdd <= p_i || p_i == 0) + " allow "  + allowConnection);
				/*
				 * if the random number probAdd is bigger than p_i, the ratio of connections in a node 
				 * by the number of total connections
				 * p_i == 0 is used to determine if a node has no connections so far
				 */
				
				   if ((probAdd <= p_i || p_i == 0)) { 
					latency = getSecureRandomNumber() * 100;
					lat = (int) latency;
					try {
						
						
						nodesToReach.get(j).addReachable(allNodes.get(i),lat);
						nodesToReach.get(i).addReachable(allNodes.get(j),lat);					
						
						
					} catch (Exception e) {
						if(debug) e.printStackTrace();
						return false;
					}
				
					totalConn++;
				   }
				
			}	
			
			
		}

		return true;
	}
	private void initArrayLists(int totalNodeAmount) {
		allNodes = new ArrayList<Node>();
		nodesToReach = new ArrayList<ReachableList>();
		for (int i = 0; i < totalNodeAmount; i++) {
			allNodes.add(null);
			nodesToReach.add(null);
		}
	}
	private void resetArrayLists(int totalNodeAmount) {
		for (int i = 0; i < totalNodeAmount; i++) {
			allNodes.set(i,null);
			nodesToReach.set(i,null);
		}
	}

	public float getSecureRandomNumber() {
		SecureRandom random = new SecureRandom();
		return random.nextFloat();
	}


	private INode createObjectNode(String type){		
		INode castToINode = null;
		try {
			Class classDefinition = Class.forName(type);			
			castToINode = (INode) classDefinition.newInstance();			
		} catch (InstantiationException e) {
			if(debug) System.out.println(e);
		} catch (IllegalAccessException e) {
			if(debug) System.out.println(e);
		} catch (ClassNotFoundException e) {
			if(debug) System.out.println(e);
		}
		return castToINode;
	}	
}
