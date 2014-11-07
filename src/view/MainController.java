package view;

import javax.swing.JFileChooser;

import model.Network;
import controller.InputParser;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class MainController {

	@FXML
	Label tfMessage;
	Button bRead;
	Button bGenerate;	
	Parent root;
	

    private Stage primaryStage;
	private InputParser iP = InputParser.getInstance();
	private long totalTime = 0;
	/**
	 * There can only be one
	 */
	private static Network network = Network.getInstance();
	
	@FXML
	private void readFile() {
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		tfMessage.setText("Read File");
		String file = null;

		try {

			int returnVal = fc.showDialog(null, "Choose a file");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				tfMessage.setText("You chose to open this file: "
						+ fc.getSelectedFile().getAbsolutePath());
				file = fc.getSelectedFile().getAbsolutePath();

			
				network = iP.readFile(file);
				if (network == null) {
					tfMessage.setText("something is wrong with your file");
				} else {			
					tfMessage.setText("file read and network set");
					goToSimulationGUI();
				}
			} else {
				tfMessage.setText("choose a file or generate a nework");
			}
			
			

		} catch (Exception ex) {
			ex.printStackTrace();
			tfMessage.setText("file read failed");
		}

	}
	
	/**
	 * For closing the menu item
	 */
	@FXML
	private void closeWindow(){
		this.primaryStage.close();	 
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
	
	@FXML
	private void goToGeneratingGUI(){
	try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Generation.fxml"));
			GridPane root = (GridPane) loader.load();
	        ColumnConstraints columnConstraints = new ColumnConstraints();
	        columnConstraints.setFillWidth(true);
	        columnConstraints.setHgrow(Priority.ALWAYS);
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			
			 // Get the Stage for Handle window operations in the controller
	        
	        GenerationController controller = loader.getController();
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

	/** Called from main to set the stage and handle events in controller
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
        this.primaryStage = stage;		
	}
	

}