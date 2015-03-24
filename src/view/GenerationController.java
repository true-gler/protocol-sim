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
import exceptions.AlgorithmNotFoundException;
import exceptions.NodeTypeNotFoundException;
import exceptions.ProtocolNotFoundException;
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

/**
 * The GenerationController handles the communication between the view and the controller
 * and builds the network out of the inputs of the view
 * 
 * @author Thomas
 *
 */
public class GenerationController {

	private static HashMap<String, Integer> hm;
	private static boolean debug = true;
	private Stage primaryStage;
	private IAlgorithm ialgorithm;
	private IProtocol iprotocol; 

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


	/**
	 * Generates a new network by reading the information out of the View (TextAreas)
	 */
	@FXML
	public void generateNetwork() {
		try {
			NetworkGenerator ng = new NetworkGenerator();
			int amountOfNodes = Integer.parseInt(tfAmount.getText());
			String[] key = taType.getText().split("\n");
			String[] value = taCount.getText().split("\n");
			String[] parameter = taParameter.getText().split("\n"); 
			
			// Get the Parameter and set Algorithm and Protocol
			String protocol = parameter[0].split(":")[1].trim();
			String algorithm = parameter[1].split(":")[1].trim();
			
			ialgorithm = ng.createClassAlgorithm(algorithm);
			iprotocol = ng.createClassProtocol(protocol);
			
			if (iprotocol == null){							
				throw new ProtocolNotFoundException();
			} else {
				Network.setProtocol(iprotocol);						
			}
			
			if (ialgorithm == null){							
				throw new AlgorithmNotFoundException();
			} else {
				Network.setAlgorithm(ialgorithm);						
			}		
			
			readAndAddParameter(parameter);
			 
			
			hm = new HashMap<String, Integer>();
			
			/**
			 * Generating and adding the Nodes of the TextArea to the network 
			 */
			int totalOtherNodeTypes = 0;
			for(int i = 0; i < key.length; i++){
				totalOtherNodeTypes += Integer.parseInt(value[i]);
			}
			if(key.length == value.length  && amountOfNodes > totalOtherNodeTypes) {// keine eingabe von gleichen werten
			
			
			for (int i = 0; i < value.length; i++) {
				if (ng.createObjectNode("model."  +  key[i]) == null) {
					throw new NodeTypeNotFoundException();
				} else {
					hm.put("model."  +  key[i], Integer.parseInt(value[i]));
				}
			}			
			Network.setTypesOfNodes(hm);			
			
			
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
		} catch (ProtocolNotFoundException e){
			tfMessage.setText("protocol not found");
		} catch (AlgorithmNotFoundException e){
			tfMessage.setText("algorithm not found");
		}
	}

	private void readAndAddParameter(String[] parameter) {
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
					if(debug) e.printStackTrace();
				}
			}						
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
	
	
}
