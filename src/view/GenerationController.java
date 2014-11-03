package view;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void generateNetwork() {
		int amountOfNodes = Integer.parseInt(tfAmount.getText());
		String[] key = taType.getText().split("\n");
		String[] value = taCount.getText().split("\n");
		hm = new HashMap<String, Integer>();
		for(int i =0; i < value.length; i++){
			hm.put(key[i],  Integer.parseInt(value[i]));
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
}
