/**
 * 
 */
package controller;

import interfaces.IAlgorithm;
import interfaces.INode;
import interfaces.IProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import data.Network;
import data.ReachableList;
import exceptions.AlgorithmNotFoundException;
import exceptions.NodeTypeNotFoundException;
import exceptions.ProtocolNotFoundException;
import model.Node;

/**
 * @author thomas, simon
 *  Input Parser - reads in the file that contains the Nodes 
 *  
 *  example File:
 *  Protocol: Crowds
 *  Algorithm: Dijkstra
 *  Paramter 
 *  pf 0.8
 *  -
 *  0 Node
 *  1 Node
 *  2 Foe 
 *	-
 *	0 1 10
 *  0 2 20
 *  1 2 30
 *  
 */
public class InputParser {
	
	private static InputParser instance = null;

	// Array List which contains all nodes
	private static ArrayList<Node> allNodes = new ArrayList<Node>();
	
	// Array List which contains all connections between the Nodes with Latency
	private static ArrayList<ReachableList> nodesToReach = new ArrayList<ReachableList>();

	//Encoding for the File
	final static Charset ENCODING = StandardCharsets.UTF_8;
	
	private boolean debug = false;

	private String line;
	
	public static InputParser getInstance() {
	      if (instance == null) {
	            instance = new InputParser();
	        }
	        return instance;
	    }
	 
	
	/**
	 * Function to read the given (selected in the FileChooser) file
	 * @param File ( Input File)
	 * @return Network (network filled with nodes, foes, etc) 
	 * @throws IOException
	 * @throws NodeTypeNotFoundException
	 */
	public Network readFile(String File) throws NodeTypeNotFoundException {
		
		Path path = Paths.get(File);
		String type, name;
		int id = 0, to=0; 
		double latency;
		String[] buffer = null ; 
		Network network;
		
		/**
		 * Reads the first Section of the file with the node definitions 
		 */
		try{
			BufferedReader reader = Files.newBufferedReader(path, ENCODING);
			line = reader.readLine();
			
			while (line != null && !line.startsWith("-")) {
				if(!line.startsWith("#")){
					/**
					 * Generate the algorithm object due to the given string
					 */
				
					if(line.startsWith("Algorithm")){
						buffer = line.split(":");
						String classType = buffer[1].trim();
						IAlgorithm algorithm = createObjectAlgorithm(classType);						
						if (algorithm == null){							
							throw new AlgorithmNotFoundException();
						} else {
							Network.setAlgorithm(algorithm);						
						}
					} else if(line.startsWith("Protocol")){
						buffer = line.split(":");
						String classType = buffer[1].trim();
						IProtocol protocol = createObjectProtocol(classType);						
						if (protocol == null){							
							throw new ProtocolNotFoundException();
						} else {
							Network.setProtocol(protocol);						
						}
					}
					else if(line.startsWith("Parameter")){
						line = reader.readLine();// Skip parameter
					
						while(!line.startsWith("-")){ // DO until the end of section
						
							buffer = line.split(" ");
							/**
							 * buffer[0] : name of the object
							 * buffer[1] : value of the object
							 */
							name = buffer[0].trim();
							String stringValue = buffer[1];
							
							// type cast error = String
							// type cast success = float
							
							try {
								float value = Float.parseFloat(stringValue);
								Network.addParameter(name, value);	
							} catch (Exception e) {							
								// TODO Auto-generated catch block
								String value = stringValue;
								Network.addParameter(name, value);	
								if(debug) e.printStackTrace();
							}
							line = reader.readLine();
						}
						break; //go to section 2
					}
				} 
				line = reader.readLine(); 
			}
		
		
			
			
		/**
		 * Reads the second Section of the file with the node definitions 
		 */
			
			
				
			line = reader.readLine();
			
			while (line != null && !line.startsWith("-")) {
				if(!line.startsWith("#")){
					buffer = line.split(" ");
					/**
					 * buffer[0] : id of the Node
					 * buffer[1] : type of the Node
					 */
					id = Integer.parseInt(buffer[0]);
					type = "model." + buffer[1];
					name = buffer[1];
					
					INode node = createObjectNode(type);
					/**
					 * In the case that the given class in the textfile is wrong
					 */
					if (node == null){
						throw new NodeTypeNotFoundException();
					}
									
					node.setId(id);
					node.setName(name);
				
					allNodes.add(id, (Node) node);
					
					
					/**
					 * Creates the reachable for every node
					 */
					ReachableList rl = new ReachableList(allNodes.get(id));
					nodesToReach.add(rl);				
					
				}
				line = reader.readLine();
			}	
			
			
			
			/**
			 * Read the first section (connections and latency) of the file
			 */
			line = reader.readLine();
			buffer = line.split(" ");
			while(line != null) {
				if(!line.startsWith("#")){
				// buffer[0] Connection node from
				// buffer[1] Connection node to
				// buffer[2] Connection latency
				buffer = line.split(" ");
				id = Integer.parseInt(buffer[0]);
				to = Integer.parseInt(buffer[1]);
				 latency = Integer.parseInt(buffer[2]);	
				
				/**
				 * as long as the sending node is the same add a reachable
				 */
				
				 try {
						nodesToReach.get(id).addReachable(allNodes.get(to), latency);
					} catch (Exception e) {
						if (debug) e.printStackTrace();
						return null;
					}
				 
				 try {
						
						nodesToReach.get(to).addReachable(allNodes.get(id), latency);
					} catch (Exception e) {
						if (debug) e.printStackTrace();
						
						return null;
					}
				}					
				line = reader.readLine();
			}
		}
		catch(NumberFormatException e2){
			 if(debug) e2.printStackTrace();
				return null;
		}
		catch(IndexOutOfBoundsException ex){
			return null;
		}
		catch(Exception e1){
			 if(debug)e1.printStackTrace();
				return null;
		}
	
	
		//Everything worked fine now return the thing
		
		network = Network.getInstance();
		Network.setAllNodes(allNodes);
		Network.setNodesToReach(nodesToReach);	
		
		Network.printNetwork();
		return network;
				
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
	
	private IProtocol createObjectProtocol(String type){		
		IProtocol castToIProtocol = null;
		try {
			Class classDefinition = Class.forName("protocol." + type);			
			castToIProtocol = (IProtocol) classDefinition.newInstance();
		
		} catch (InstantiationException e) {
			if(debug) System.out.println(e);
		} catch (IllegalAccessException e) {
			if(debug) System.out.println(e);
		} catch (ClassNotFoundException e) {		
			if(debug) System.out.println(e);
		}
		return castToIProtocol;
	}
	
	private IAlgorithm createObjectAlgorithm(String type){
		
		IAlgorithm castToIAlgorithm = null;
		try {
			Class classDefinition = Class.forName("algorithm." + type);			
			castToIAlgorithm = (IAlgorithm) classDefinition.newInstance();
		
		} catch (InstantiationException e) {
			if(debug) System.out.println(e);
		} catch (IllegalAccessException e) {
			if(debug) System.out.println(e);
		} catch (ClassNotFoundException e) {
			if(debug) System.out.println(e);
		}
		return castToIAlgorithm;
	}


}
