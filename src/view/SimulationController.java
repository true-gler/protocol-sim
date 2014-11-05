package view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Network;
import model.Node;
import model.Paket;
import controller.InputParser;
import controller.LogHandler;
import controller.Simulator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

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
	
	private static ObservableList<String> LogList = FXCollections.observableArrayList();      
	private static String path = System.getProperty("user.dir");
	private static InputParser ip = InputParser.getInstance();
	private Stage primaryStage;

	public void initialize(URL location, ResourceBundle resources) {

		taNetwork.setText(Network.getNetworkOutput());
		lvLog.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	openCSV(newValue);
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
			Network.setProbabilityForward(Float.parseFloat(tfForward.getText()
					.toString()));
			tfMessage.setText("Simulation started");

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
					Node initNode = n.getAllNodes().get(initNodeNr);
					Node receiver = n.getAllNodes().get(receiverNr);
					try {
						System.out
								.println("L7 | Want to send a Package form: "
										+ initNode.getId() + " to: "
										+ receiver.getId());
						Simulator sim = null;
						int threadCount = Integer.parseInt(tfSimulations
								.getText());

						for (int i = 0; i < threadCount; i++) {
							Paket p = new Paket(1, tfPacket.getText());
							sim = new Simulator(initNode, receiver, p);
							sim.startSimulation();
						}
						LogHandler lg = new LogHandler();
						
						File f = new File(path + "/Logs");
						ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));						
										
						LogList = FXCollections.observableArrayList();  
						for(int i = 0; i < names.size();i++){							
							LogList.add(names.get(i) + "\n");
						}
						
						lvLog.setItems(LogList);	
						tfMessage.setText("Simulation finished");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else
				tfMessage.setText("Specify the correct Target/Send node");
		} else {
			// tfMessage.setText("Please check your inputs");
		}
	}

	@FXML
	private void showNetwork() {
		tfMessage.setText("'show network' not yet implemented");
	}

	
	private boolean checkInputs() {
		try {
			Integer.parseInt(tfReceiver.getText().toString());
			Integer.parseInt(tfSender.getText().toString());
			Integer.parseInt(tfSimulations.getText().toString());
			Float.parseFloat(tfForward.getText().toString());
			if (tfForward.getText().trim() == ""
					|| tfPacket.getText().trim() == ""
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
	
	public void openCSV(String file){

		Desktop dt = Desktop.getDesktop();
		
		try { 
			String path = System.getProperty("user.dir");
			System.out.println("OPEN FILE TODO");
		//File f = new File(path + "/Logs/"+file);
			//dt.open(f);
		//	Desktop.getDesktop().open(new File("/home/thomas/test.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
