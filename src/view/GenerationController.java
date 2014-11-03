package view;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import model.Node;
import exceptions.NodeTypeNotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	TextField tfMessage;

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
			for(int i =0; i < value.length; i++){
				if(createObject(key[i]) == null){				
					throw new NodeTypeNotFoundException();
				}
				else{
					hm.put(key[i],  Integer.parseInt(value[i]));
				}
			}
			NetworkGenerator ng = new NetworkGenerator();
			ng.generateNetwork(hm,amountOfNodes);
			
			closeWindow();
			
		} catch (NumberFormatException e) {
			tfMessage.setText("check your inputs");
		} catch (NodeTypeNotFoundException e){
			tfMessage.setText("node type not found");
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
	private Node createObject(String type){
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
