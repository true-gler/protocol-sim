package controller;

import data.Network;
import data.Paket;
import model.Node;

/**
 * Many Simulators
 * Simulator which invokes a Communication ten
 * 
 * @author Thomas
 * 
 */

public class Simulator {
	/* Simulation */
	
	private boolean running = false;
	private Network network;
	private EventHandler ev;
	
	
	public Simulator(Node initNode, Node receiver, Paket paket) {
		network = Network.getInstance();
		ev = EventHandler.getInstance(initNode, receiver, paket);		
	}
	
	
	public void startSimulation() {
		running = true;
		try {
			while(isRunning())
			{
				running = ev.simulate(network);
				
			}	
		} catch (Exception ex) {
			System.out.println("error at simulator");
			ex.printStackTrace();
		}
	}
	
	public boolean isRunning() {
		return this.running;
	}

}
