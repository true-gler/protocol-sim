package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Network;
import model.Node;
import model.Paket;
import controller.InputParser;
import controller.Simulator;

public class GUIController implements Initializable {
	
	@FXML
	Button bStart;
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

	private static InputParser ip = InputParser.getInstance();
    private Stage primaryStage;
	
	public void initialize(URL location, ResourceBundle resources) {
		
		taNetwork.setText(ip.getNetworkOutput());		
	}
	
	/**
	 * For closing the menu item
	 */
	@FXML
	private void closeWindow(){
		System.out.println("close");
		this.primaryStage.close();	 
	}
	
	@FXML
	private void startSimulation() {
		System.out.println(primaryStage.getTitle());
		if (checkInputs()) {
			Network.setProbabilityForward(Float.parseFloat(tfForward.getText().toString()));
			tfMessage.setText("Simulation Started");

			Network n = Network.getInstance();
			int receiverNr = Integer.parseInt(tfReceiver.getText().toString());
			int initNodeNr = Integer.parseInt(tfSender.getText().toString());

			if (n.getNetwork() != null) {
				int size = 0;
				try {
					size = n.getAllNodes().size() - 1;
				} catch (NullPointerException e1) {
					tfMessage.setText("First read a File");
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
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else
				tfMessage.setText("Specify the correct Target/Send node");
		} else {
			tfMessage.setText("Please check your inputs");
		}
	}
	@FXML
	private void showNetwork() {

	}

	private boolean checkInputs() {
		
		return true;
	}
	
	/** Called from mainController to set the stage and handle events in controller
	 * @param stage
	 */
	public void setStage(Stage stage) {
        this.primaryStage = stage;		
	}
	
}
