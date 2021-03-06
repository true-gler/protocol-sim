package protocol;

import java.io.IOException;
import java.util.ArrayList;

import model.Node;
import controller.EventHandler;
import controller.LogHandler;
import data.Network;
import interfaces.IProtocol;

/**
 * Implementation of a protocol
 * 
 * @author Thomas, simon
 *
 */
public class Crowds implements IProtocol{
	
	private LogHandler lh = LogHandler.getInstance();

	/**
	 * Arraylist for the collaborating foes in the network. Every collaborating foe on
	 * the way of the packet adds the Node he received it from
	 */
	
	public static ArrayList<Node> collabAL = new ArrayList<>();
	//We don't add a timestamp, since the transmission is successive
	
	public static void addCollabInformation(Node n){
		collabAL.add(n);
	}

	@Override
	public void executePreSimulation(Object... object) {
		// TODO Auto-generated method stub
		try {
			lh.writeHeader();
		} catch (IOException e2) {
			System.out.println("log write error");
		}
		
	}	
	
	@Override
	public void executeTX(Object... object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeRX(Object... object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeFinished(Object... object) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Checks the theorem of crowds see documentation paper
	 */
	@Override
	public void executePostSimulation(Object... object) {

		lh = LogHandler.getInstance();
		if(collabAL.size() > 0){
			lh.appendData("\nThe following Nodes have been unmasked by the collaborating Nodes\n");
			for (int i = 0; i < collabAL.size(); i++){
				lh.appendData(collabAL.get(i).toString() + "\n");					
			}
			if(collabAL.get(0) == EventHandler.getStartNode()){
				lh.appendData("\n!PROOF Unvieled the initiator of the Communication \n");
				System.out.println("\n!PROOF Unvieled the initiator of the Communication \n");
				int collabAmount = Network.getInstance().getTypeOfNode("model.FoeCrowdsCOLLAB");
				// getProbability to forwad
				float probForward = (float) Network.getParameter("pf");
				
				/**
				 * Beweisbare unschuld gegeben (probable innocence) aber verletzt, da 
				 * startknoten herausgefunden, siehe Crowds Theorem 5.2 
				 */
				if(collabAmount > 0){
					if(Network.getInstance().getAllNodes().size() >= (probForward/(probForward-0.5))*
							(collabAmount +1)){
						System.out.println("PROOF: Probable innocence according to the paper violated");
						System.out.println("\nProbable innocence given but violated by collaborating foes\n");
						lh.appendData("Probable innocence according to the paper violated");
						lh.appendData("\nProbable innocence given but violated by collaborating foes\n");
					}
				}
					
				}
		}
		
	}


}
