package view;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import controller.NetworkGenerator;
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
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class GenerationController implements Initializable {

	private static HashMap<String, Integer> hm;

	private Stage primaryStage;

	@FXML
	TextField tfAmount;
	@FXML
	TextArea taType;
	@FXML
	TextArea taCount;
	@FXML
	Label tfMessage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void generateNetwork() {
		try {
			int amountOfNodes = Integer.parseInt(tfAmount.getText());
			String[] key = taType.getText().split("\n");
			String[] value = taCount.getText().split("\n");
			hm = new HashMap<String, Integer>();
			for (int i = 0; i < value.length; i++) {
				if (createObject(key[i]) == null) {
					throw new NodeTypeNotFoundException();
				} else {
					hm.put(key[i], Integer.parseInt(value[i]));
				}
			}
			NetworkGenerator ng = new NetworkGenerator();
			
			if (!ng.generateNetwork(hm, amountOfNodes)) {
				tfMessage.setText("network generation failed");
			} else {
				tfMessage.setText("network generated");
			}
			goToSimulationGUI();
			

		} catch (NumberFormatException e) {
			e.printStackTrace();
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

	private Node createObject(String type) {
		Object object = null;
		try {
			Class classDefinition = Class.forName(type);
			object = classDefinition.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
		return (Node) object;
	}
}
