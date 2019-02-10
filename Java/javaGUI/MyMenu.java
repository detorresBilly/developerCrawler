import java.util.Collection;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MyMenu extends LayoutBlueprint {
    Menu fileMenu, editMenu, searchMenu, runMenu, helpMenu, settingMenu;
    MenuBar menuBar;
    BorderPane myBP;
    Collection<Menu> myMenus;
    Boolean showingLineNumbers = true;

    public MyMenu(Stage stage) {
	super(stage);
	fileMenu = new Menu("_File");
	editMenu = new Menu("_Edit");
	searchMenu = new Menu("Search");
	settingMenu = new Menu("Setting");
	runMenu = new Menu("Run");
	helpMenu = new Menu("Help");
	myBP = new BorderPane();

	MenuItem newFile = new MenuItem("New");
	newFile.setOnAction(e -> {
	    System.out.println("New");
	});

	MenuItem existingProject = new MenuItem("Open Existing Project...");
	existingProject.setOnAction(e -> {
	    System.out.println("Existing Project");
	});

	MenuItem saveMenu = new MenuItem("_Save");
	saveMenu.setOnAction(e -> {
	    System.out.println("Save");
	});

	MenuItem saveAsMenu = new MenuItem("Save as");
	saveAsMenu.setOnAction(e -> {
	    System.out.println("Save as");
	});

	MenuItem exitMenu = new MenuItem("Exit");
	exitMenu.setOnAction(e -> {
	    System.out.println("Close window");
	});

	MenuItem emFindN = new MenuItem("Find Next");
	emFindN.setOnAction(e -> {
	    System.out.println("find next");
	});
	emFindN.setDisable(true);

	MenuItem emFindP = new MenuItem("Find Previous");
	emFindP.setOnAction(e -> {
	    System.out.println("find prev");
	});
	emFindP.setDisable(true);

	CheckMenuItem showLineNums = new CheckMenuItem("Show Line Numbers");
	showLineNums.setSelected(true);
	showLineNums.setOnAction(e -> {
	    if (showLineNums.isSelected()) {
		showingLineNumbers = false;
	    } else {
		showingLineNumbers = true;
	    }
	});

	ToggleGroup automationToggle = new ToggleGroup();
	RadioMenuItem weak = new RadioMenuItem("Weak");
	weak.setOnAction(e -> {
	   //TODO - set automator strictness to weak  
	});
	RadioMenuItem moderate = new RadioMenuItem("Moderate");
	moderate.setOnAction(e -> {
		   //TODO - set automator strictness to moderate 
		});
	RadioMenuItem strict = new RadioMenuItem("Strict");
	strict.setOnAction(e -> {
		   //TODO - set automator strictness to strict  
		});
	weak.setToggleGroup(automationToggle);
	moderate.setToggleGroup(automationToggle);
	strict.setToggleGroup(automationToggle);

	fileMenu.getItems().add(newFile);
	fileMenu.getItems().add(existingProject);
	fileMenu.getItems().add(saveMenu);
	fileMenu.getItems().add(saveAsMenu);
	fileMenu.getItems().add(new SeparatorMenuItem());
	fileMenu.getItems().add(exitMenu);
	editMenu.getItems().addAll(emFindN, emFindP);
	helpMenu.getItems().addAll(showLineNums);
	settingMenu.getItems().addAll(weak, moderate, strict);
	menuBar = new MenuBar();
	menuBar.getMenus().addAll(fileMenu, editMenu, searchMenu, runMenu, settingMenu, helpMenu);
	myBP.setTop(menuBar);
    }

    public BorderPane getBP() {
	return myBP;
    }
    
}
