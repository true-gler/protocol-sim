package view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Node;
import controller.EventHandler;
import controller.InputParser;
import controller.LogHandler;
import controller.Simulator;
import data.Network;
import data.Paket;
import event.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Simulation gui
 * @author Thomas
 *
 */
public class SimulationController implements Initializable {

	@FXML
	Button bStart;
	@FXML
	Button bLog;
	@FXML
	Button bShowNetwork;
	@FXML
	TextArea taNetwork;
	@FXML
	TextField tfSender;
	@FXML
	TextField tfReceiver;
	@FXML
	TextField tfPacket;
	@FXML
	TextField tfSimulations;
	@FXML
	TextField tfForward;
	@FXML
	Label tfMessage;
	@FXML
	Parent root;
	@FXML
	ListView lvLog;
	@FXML
	Label lLogDir;
	
	private static ObservableList<String> LogList = FXCollections.observableArrayList();      
	private static String path = System.getProperty("user.dir");
	private static InputParser ip = InputParser.getInstance();
	private Stage primaryStage;
	private LogHandler lg;
	private int simulationCount = 0;
	public void initialize(URL location, ResourceBundle resources) {
		lLogDir.setText("output in " + System.getProperty("user.dir") +  "/Logs");
		taNetwork.setText(Network.getNetworkOutput());
		lg =  LogHandler.getInstance();
		
		try {
				File f = new File(path + "/Logs");			
				ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));														
				LogList = FXCollections.observableArrayList();  
				Collections.sort(names, new Comparator<String>() {
			        public int compare(String  st1, String  str2)
			        {
			            return  st1.compareTo(str2);
			        }
			    });
				LogList.addAll(names);				
				lvLog.setItems(LogList);
		} catch (Exception e) {
			
		}
	
		lvLog.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	
		    }
		});
		
	}

	/**
	 * For closing the menu item
	 */
	@FXML
	private void closeWindow() {
		System.out.println("close");
		this.primaryStage.close();
	}

	@FXML
	private void startSimulation() {
		if (checkInputs()) {
			

			Network n = Network.getInstance();
			int receiverNr = Integer.parseInt(tfReceiver.getText().toString());
			int initNodeNr = Integer.parseInt(tfSender.getText().toString());
			
			if (n.getNetwork() != null) {
				int size = 0;
				try {
					size = n.getAllNodes().size() - 1;
				} catch (NullPointerException e1) {
					tfMessage.setText("First read a file");
				}
				if (receiverNr <= size && initNodeNr < size && initNodeNr >= 0) {
					tfMessage.setText("Simulation started");
					Node initNode = n.getAllNodes().get(initNodeNr);
					Node receiver = n.getAllNodes().get(receiverNr);
					EventHandler.setEndNode(n.getAllNodes().get(receiverNr));
					EventHandler.setStartNode(n.getAllNodes().get(initNodeNr));
				
					try {
						System.out
								.println("L7 | Want to send a Package form: "
										+ initNode.getId() + " to: "
										+ receiver.getId());
						Simulator sim = null;
						int threadCount = Integer.parseInt(tfSimulations
								.getText());

						for (int i = 0; i < threadCount; i++) {
							Paket p = new Paket(i, tfPacket.getText());
							EventHandler.setPaket(p);
							sim = new Simulator(initNode, receiver, p);
							sim.startSimulation();
							simulationCount++;
						}
						
						
						File f = new File(path + "/Logs");
						ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));										
						LogList = FXCollections.observableArrayList();	
						//Sorting
						Collections.sort(names, new Comparator<String>() {
						        public int compare(String  st1, String  str2)
						        {
						            return  st1.compareTo(str2);
						        }
						    });
						LogList.addAll(names);
						lvLog.setItems(LogList);	
						tfMessage.setText("Simulation "  + simulationCount + " finished");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else
					tfMessage.setText("Specify the correct Target/Send node");
			}
			}
		else {
			tfMessage.setText("Please check your inputs");
		}
	}

	@FXML
	private void showNetwork() {
		ShowNetworkController snc = new ShowNetworkController();
		snc.start(new Stage());
	}

	
	private boolean checkInputs() {
		try {
			Integer.parseInt(tfReceiver.getText().toString());
			Integer.parseInt(tfSender.getText().toString());
			Integer.parseInt(tfSimulations.getText().toString());		
			if (tfPacket.getText().trim() == ""
					|| tfReceiver.getText().trim() == ""
					|| tfSender.getText().trim() == ""
					|| tfSimulations.getText().trim() == "") {
				tfMessage.setText("complete your inputs");
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			tfMessage.setText("complete your inputs");
			return false;
		}
	}

	/**
	 * Called from mainController to set the stage and handle events in
	 * controller
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.primaryStage = stage;
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
