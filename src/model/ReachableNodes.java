package model;

import java.util.LinkedList;


/**
 * LinkedList Object for target nodes
 * @author Thomas
 *
 */
public class ReachableNodes {
	
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
		
		
	

} 
