package data;

import java.util.Iterator;
import java.util.LinkedList;

import model.Node;

/**
 * The reachable list represents all nodes that a reached by another node
 * Each entry in a ReachableList contains a LinkedList  
 * @author Thomas
 *
 */

public class ReachableList {
	
	private Node n; 				//Following node of the identifier of the LinkedList
	private LinkedList ll;			//Reachable Nodes and Latencies
	public ReachableList(Node n) {
		this.n = n;		
		ll = new LinkedList(); 
	}
	
	public void addReachable(Node n, double latency){
		ll.add(new ReachableNodes(n,latency));
	}

	public Node getN() {
		return n;
	}

	public void setN(Node n) {
		this.n = n;
	}

	public LinkedList getLl() {
		return ll;
	}

	public void setLl(LinkedList ll) {
		this.ll = ll;
	}
	

	}
	
	


