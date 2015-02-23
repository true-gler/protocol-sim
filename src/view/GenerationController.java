package view;

import interfaces.IAlgorithm;
import interfaces.INode;
import interfaces.IProtocol;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import controller.NetworkGenerator;
import data.Network;
import model.Node;
import exceptions.NodeTypeNotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class GenerationController {

	private static HashMap<String, Integer> hm;
	private static boolean debug = false;
	private Stage primaryStage;

	@FXML
	TextField tfAmount;
	@FXML
	TextArea taType;
	@FXML
	TextArea taCount;
	@FXML
	Label tfMessage;
	@FXML
	TextArea taParameter;


	@FXML
	public void generateNetwork() {
		try {
			int amountOfNodes = Integer.parseInt(tfAmount.getText());
			String[] key = taType.getText().split("\n");
			String[] value = taCount.getText().split("\n");
			String[] parameter = taParameter.getText().split("\n"); 
			
			// Get the Parameter
			String protocol = parameter[0].split(":")[1].trim();
			String algorithm = parameter[1].split(":")[1].trim();
			IAlgorithm ialgorithm = createObjectAlgorithm(algorithm);
			IProtocol iprotocol = createObjectProtocol(protocol);
			Network.setAlgorithm(ialgorithm);
			Network.setProtocol(iprotocol);
			
			String[] str = null;
			String name = "";
			
			for(int i = 3; i < parameter.length; i++){
				str = parameter[i].split(" ");
				name = str[0];
				if(!name.isEmpty()){
					try {
						float valuef = Float.parseFloat(str[1]);
						Network.addParameter(name, valuef);	
					} catch (Exception e) {							
						// TODO Auto-generated catch block
						String valuestr = str[1];
						Network.addParameter(name, valuestr);	
						e.printStackTrace();
					}
				}						
			}
			 
			
			
			// get foe text area
			int totalFoes = 0;
			for(int i = 0; i < key.length; i++){
				totalFoes += Integer.parseInt(value[i]);
			}
			if(key.length == value.length  && amountOfNodes > totalFoes) {// keine eingabe von gleichen werten
			hm = new HashMap<String, Integer>();
			
			for (int i = 0; i < value.length; i++) {
				if (createObjectNode("model."  +  key[i]) == null) {
					throw new NodeTypeNotFoundException();
				} else {
					hm.put("model."  +  key[i], Integer.parseInt(value[i]));
				}
			}
			
			Network.setTypesOfNodes(hm);
			
			NetworkGenerator ng = new NetworkGenerator();
			
			if (!ng.generateNetwork(hm, amountOfNodes)) {
				tfMessage.setText("network generation failed");
			} else {
				tfMessage.setText("network generated");
			}
			goToSimulationGUI();
			}
			else {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {
			tfMessage.setText("check your inputs");
		} catch (NodeTypeNotFoundException e) {
			tfMessage.setText("node type not found");
		}
	}
	
	private void goToSimulationGUI(){
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Simulation.fxml"));
			BorderPane root = (BorderPane) loader.load();
	        ColumnConstraints columnConstraints = new ColumnConstraints();
	        columnConstraints.setFillWidth(true);
	        columnConstraints.setHgrow(Priority.ALWAYS);
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			
			 // Get the Stage for Handle window operations in the controller
	        
	        SimulationController controller = loader.getController();
	        controller.setStage(stage);
	        
			// CSS
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			
			closeWindow();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called from main to set the stage and handle events in controller
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}

	/**
	 * For closing the menu item
	 */
	@FXML
	private void closeWindow() {
		this.primaryStage.close();
	}
	
	/**
	 * Show Information of development
	 */
	@FXML
	private void showAbout(){
		try {
			
			System.out.println("show about");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("About.fxml"));
			GridPane root = (GridPane) loader.load();
	    
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			

			// CSS
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();		
			
		} catch(Exception e) {
			e.printStackTrace();
		}
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
