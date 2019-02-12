package javafxTools;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ConfirmBox {

    static boolean answer;
    public static Boolean display(String title, String message) {
	Stage window = new Stage();
	window.initModality(Modality.APPLICATION_MODAL);
	window.setTitle(title);
	window.setMinWidth(250);
	Label label = new Label();
	label.setText(message);

	Button button1 = new Button("Yes");
	Button button2 = new Button("No");

	button1.setOnAction(e -> {
	   answer = true;
	   window.close();
	});
	
	button2.setOnAction(e -> {
	    answer = false;
	    window.close();
	});
	VBox layout = new VBox();
	layout.getChildren().addAll(label,button1,button2);
	layout.setAlignment(Pos.CENTER);
	Scene scene = new Scene(layout);
	window.setScene(scene);
	window.showAndWait();
	
	return answer;
    }
}
