package view;


import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Shows the initial view 
 * @author Thomas
 *
 */
public class Main extends Application { 
	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			GridPane root = (GridPane) loader.load();
	        ColumnConstraints columnConstraints = new ColumnConstraints();
	        columnConstraints.setFillWidth(true);
	        columnConstraints.setHgrow(Priority.ALWAYS);
	        root.getColumnConstraints().add(columnConstraints);
			Scene scene = new Scene(root);
			
			 // Get the Stage for Handle window operations in the controller
	        
	        MainController controller = loader.getController();
	        controller.setStage(stage);
	        
			// CSS
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
