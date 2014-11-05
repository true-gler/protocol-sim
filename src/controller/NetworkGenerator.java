package controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.stage.Stage;
import exceptions.NodeTypeNotFoundException;
import model.Network;
import model.Node;
import model.ReachableList;

public class NetworkGenerator {

	private Stage primaryStage;
	// Array List which contains all nodes
	private static ArrayList<Node> allNodes = new ArrayList<Node>();

	// Array List which contains all connections between the Nodes with Latency
	private static ArrayList<ReachableList> nodesToReach = new ArrayList<ReachableList>();
	// totalAmount of Nodes
	private static int totalAmount;
	// Connections in network
	private static int totalConn = 0;

	private static int length = 0;

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
		float le = totalAmount / 20;
		length = (int) le;

		

		initArrayLists(totalNodeAmount);

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
				Node node = createObject(type);
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
				allNodes.set(id, node);
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
		} else {
			if (!generateRandom())
				return false;
		}

		Network.setAllNodes(allNodes);
		Network.setNodesToReach(nodesToReach);
		return true;
	}

	/**
	 * Generates random node (for each node min 2 connections)
	 * 
	 * @return
	 */
	private boolean generateRandom() {
		int to2, to;
		float latency, f, t;
		for (int i = 0; i < totalAmount; i++) {
			latency = getSecureRandomNumber() * 100;
			t = getSecureRandomNumber() * totalAmount;
			f = getSecureRandomNumber() * totalAmount;
			to = (int) t;
			to2 = (int) f;
			try {
				nodesToReach.get(i).addReachable(allNodes.get(to), latency);
				nodesToReach.get(to).addReachable(allNodes.get(i), latency);
				nodesToReach.get(i).addReachable(allNodes.get(to2), latency);
				nodesToReach.get(to2).addReachable(allNodes.get(i), latency);
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
		int to;
		float latency, f, t;

		for (int i = 0; i < length; i++) {
	
			float prob = 0.6F;
			while ( prob > 0.5) {
				t = getSecureRandomNumber() * length;
				to = (int) t;
				latency = getSecureRandomNumber() * 100;
				try {
					nodesToReach.get(i).addReachable(allNodes.get(to), latency);
					nodesToReach.get(to).addReachable(allNodes.get(i), latency);
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
		float latency;
		for (int i = length; i < totalAmount; i++) {
			for (int j = 0; j < i; j++) {
				latency = getSecureRandomNumber() * 100;

				float p_i = getSecureRandomNumber();
				float sumP_j = nodesToReach.get(j).getLl().size(); //0?
				sumP_j /= totalConn; 
				
				
				if (p_i<= sumP_j || sumP_j == 0) {
					try {
						nodesToReach.get(j).addReachable(allNodes.get(i),
								latency);
						nodesToReach.get(i).addReachable(allNodes.get(j),
								latency);
					} catch (Exception e) {
						return false;
					}
					totalConn++;
				}

			}
		}

		return true;
	}

	private void initArrayLists(int totalNodeAmount) {
		for (int i = 0; i < totalNodeAmount; i++) {
			allNodes.add(null);
			nodesToReach.add(null);
		}

	}

	public float getSecureRandomNumber() {
		SecureRandom random = new SecureRandom();
		return random.nextFloat();
	}

	private Node createObject(String type) {
		Object object = null;
		try {
			Class classDefinition = Class.forName(type);
			object = classDefinition.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
		return (Node) object;
	}
}
