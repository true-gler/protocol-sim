package model;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class Network{

	/** 
	 * Class for connections. Stores the Name of a node and a LinkedList with all connected nodes and the latencies
	 * 
	 */
	
	/**
	 * ArrayList for ReachableLists which contains the target Nodes
	 */
	
	private static Network instance = null;
	private static ArrayList<ReachableList> nodesToReach;
	private static ArrayList<Node> allNodes;
	private static float probabilityForward;
	private static HashMap <String, Integer>typesOfNodes;
	/*
	 * Arraylist for the collaborating foes in the network. Every collaborating foe on
	 * the way of the packet adds the Node he received it from
	 */
	public static ArrayList<Node> collabAL = new ArrayList<>();

	public static Network getInstance() {
	      if (instance == null) {
	            instance = new Network(allNodes);
	        }
	        return instance;
	    }
	 
	public Network(ArrayList<Node> allNodes){
		Network.nodesToReach = new ArrayList<ReachableList>();
		Network.allNodes = allNodes;
	}
	
	public void addReachableList(ReachableList e){
		Network.nodesToReach.add(e);
	}
	
	public ArrayList<ReachableList> getNodesToReach() {
		return nodesToReach;
	}

	public static void setNodesToReach(ArrayList<ReachableList> nodesToReach) {
		Network.nodesToReach = nodesToReach;
	}

	public ArrayList<Node> getAllNodes() {
		return allNodes;
	}

	public static void setAllNodes(ArrayList<Node> allNodes) {
		Network.allNodes = allNodes;
	}

	public Network getNetwork() {		
		return instance;
	}
	
	/**
	 * Output for the Textarea
	 */
	@Override
	public String toString(){
		
		String str = "";
		for(int i = 0; i < nodesToReach.size(); i++){
			str += "Node: " + nodesToReach.get(i).getN().getId(); str += "\n";
			for(int j = 0; j< nodesToReach.get(i).getLl().size(); j++){	//this is where the magic happens
				ReachableNodes rn= (ReachableNodes) nodesToReach.get(i).getLl().get(j); // this is also some kind of magic :)  			
				str += " Node: " + rn.getN().getId() + " \tLatency: " + rn.getLatency() + "\n"; 
			}		
			str += "\n";
		}
			
		return str;
		
	}

	public float getProbabilityForward() {
		return probabilityForward;
	}

	public static void setProbabilityForward(float probabilityForward) {
		Network.probabilityForward = probabilityForward;
	}
	public static String getNetworkOutput(){
		String str = "", type ="";
		for(int i = 0; i < nodesToReach.size(); i ++){
			type = nodesToReach.get(i).getN().getClass().toString();
			type = type.substring(12, type.length());
			
			if (type.contains("Node")){
				str += nodesToReach.get(i).getN().getId() + " " + type + "\t\t  " + nodesToReach.get(i).getLl().toString() + " \n";
			} else {
				str += nodesToReach.get(i).getN().getId() + " " + type + "  " + nodesToReach.get(i).getLl().toString() + " \n";
			}
			
		}
		return str;
	}
	public static void printNetwork(){
		System.out.println("\n List which nodes are reached by another \n ");
		System.out.println(getNetworkOutput());
	}
	
	//We don't add a timestamp, since the transmission is successive
	public static void addCollabInformation(Node n){
		collabAL.add(n);
	}

	public static void setTypesOfNodes(HashMap<String, Integer> hm) {
		typesOfNodes = hm;
	}
	
	public static int getTypeOfNode(String nodeType){
		if(typesOfNodes != null){
			return typesOfNodes.get(nodeType);
		} else {
			return 0;
		}

	}
	
}
