package data;

import java.util.LinkedList;

import model.Node;


/**
 * LinkedList Object for target nodes
 * @author Thomas
 *
 */
public class ReachableNodes implements Comparable<Node> {
	
		private Node n; 		//target node
		private double latency; //Latency to target node		
		
		public ReachableNodes(Node n, double latency){
			this.n = n;
			this.latency = latency;
		}

		public Node getN() {
			return n;
		}

		public void setN(Node n) {
			this.n = n;
		}

		public double getLatency() {
			return latency;
		}

		public void setLatency(double latency) {
			this.latency = latency;
		}

		@Override
		public String toString() {
			return "  " + this.getN().getId() + " Latency: "+  this.getLatency() + "\t";
			
		}

		@Override
		/**
		 * To sort the output of the reachable Nodes in the GUI
		 */
		public int compareTo(Node o) {
			int id = this.getN().getId();
			if (id < o.getId()){
				return 1;
			}
			else if (id > o.getId()){
				return -1;
			}
			else {
			return 0;
			}
		}
		
} 
