package model;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
	
	/**
	 * Arraylist for the collaborating foes in the network. Every collaborating foe on
	 * the way of the packet adds the Node he received it from
	 */
	
	public static ArrayList<Node> collabAL = new ArrayList<>();
	/**
	 * Types of the nodes which are read in by the inputparser 
	 */
	private static HashMap <String, Integer> typesOfNodes;
	
	/**
	 * This hashmap reads the parameter out of the input file or from the textfield (when generating a 
	 * network) and generates Objects dynamically. These objects are stored into the Network-Singleton-Object
	 * String is the Name of the Object to get the parameter when the node needs it
	 * Object is the object itself. The Type of the object depends on the value that is read in.
	 * 	datatype: charaters -> String
	 * 			  numbers   -> float
	 */
	
	public static HashMap <String, Object> parameter;
	
	/**
	 *  The algorithm must be named after the class in the package algorithm it gets instantiated 
	 */
	public static Object algorithm;
	
	/**
	 *  The protocol must be named after the class in the package protocol it gets instantiated 
	 */
	public static Object protocol;


	public static Object getParameter(String parameterName){
		return parameter.get(parameterName);
	}
	public static void addParameter(String parameterName, String value) {
		parameter.put(parameterName, value);
	}
	public static void addParameter(String parameterName, Float value) {
		parameter.put(parameterName, value);
	}
	
	
	//We don't add a timestamp, since the transmission is successive
	public static void addCollabInformation(Node n){
		collabAL.add(n);
	}
		
	public static Network getInstance() {
	      if (instance == null) {
	            instance = new Network(allNodes);
	        }
	        return instance;
	    }
	 
	public Network(ArrayList<Node> allNodes){
		Network.nodesToReach = new ArrayList<ReachableList>();
		Network.allNodes = allNodes;
		Network.parameter = new HashMap<String, Object>() ;
		Network.algorithm = new HashMap <String, Object> ();
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
	
	public static String getNetworkOutput(){
		String str = "", type ="", key ="";
		
		str+= "Protocol: ";
		str += protocol.toString() + "\n\n";
		
		
	    str+= "Algorithm: ";
	    str += algorithm.toString() + "\n\n";
			
		str+= "Parameter:\n";
		Iterator it = parameter.entrySet().iterator();
		while(it.hasNext()){
			
			str += it.next().toString() + "\n";
			//str += key.toString() + " " + parameter.get(key) + "\n";
		}
	
		 
		 
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
	public static void setAlgorithm(Object algorithm) {
		Network.algorithm = algorithm;	
	}
	
	public static Object getAlgorithm() {
		return algorithm;
	}
	
	public static Object getProtocol() {
		return protocol;
	}
	public static void setProtocol(Object protocol) {
		Network.protocol = protocol;
	}
	
	
}
