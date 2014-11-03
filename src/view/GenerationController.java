package view;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GenerationController implements Initializable {

		private static HashMap<String, Integer> hm;
		
		@FXML
		TextField tfAmount;
		TextArea taType;
		TextArea taCount;
		
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			
		}
		
		@FXML
		public void generate(){
			int amountOfNodes = Integer.parseInt(tfAmount.getText());
			for (String line : taType.getText().split("\\n")){
				
			}
				
		//	NetworkGenerator ng = new NetworkGenerator();
		//	ng.generateNetwork(hm, amountOfNodes);
		}
		

		
}
