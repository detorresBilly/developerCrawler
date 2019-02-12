import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LayoutBlueprint {
    
    VBox layout;
    private GridPane grid;
    
    public LayoutBlueprint(Stage stage){
	grid = new GridPane();
	grid.setPadding(new Insets(20,20,20,20));
	grid.setVgap(8);
	grid.setHgap(10);
    }
    
    public VBox getView(){
	return layout;
    }
    
    public GridPane getGrid(){
	return grid;
    }
}
