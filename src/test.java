import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class test extends Application {
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("BABA IS YOU");
		Canvas canvas = new Canvas(960, 960);
		root.getChildren().add(canvas);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image oneImage = new Image("file:ressources/baba.png", 100, 100, true, true);
		gc.drawImage(oneImage, 430, 430);
		
		primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}