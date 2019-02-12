import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafxTools.ConfirmBox;
import database.Accounts;
import database.ActivationKeyTable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.controlsfx.control.MaskerPane;
import org.controlsfx.control.table.TableRowExpanderColumn;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.json.*;
import org.apache.commons.io.IOUtils;

import account.Account;
import javaMail.JavaMail;

public class Main extends Application {

    Button button, button2;
    FlowPane filterBox;
    Stage window;
    Scene searchForm, loginScene, accActivation, accCreation, finalScene;
    MenuBar menuBar;
    BorderPane layout;
    MaskerPane masker;
    TableView<Developer> tableView;
    JSONArray query_results; 
    JSONObject filter_fields = new JSONObject();
    private AutoCompletionBinding<String> autoCompletionBinding;
    private String[] _possibleSuggestions = { "Frontend", "Backend", "SQL", "Database", "Jira", ".NET", "Mobile",
	    "Android", "Machine Learning", "Load Runner", "MongoDB", "Scrum", "Apache Hadoop", "UML", "PostgreSQL",
	    "PERL", "Crypography" };
    private Set<String> possibleSuggestions = new HashSet<>(Arrays.asList(_possibleSuggestions));
    ArrayList<String> searchInfo = new ArrayList<String>();
    String curr_user;

    public static void main(String[] args) {
	launch(args);
    }

    private GridPane generateSearchGrid() {
	GridPane grid = new GridPane();
	grid.setAlignment(Pos.CENTER);
	grid.setPadding(new Insets(40, 40, 40, 40));
	grid.setHgap(10);
	grid.setVgap(10);
	ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
	columnOneConstraints.setHalignment(HPos.RIGHT);
	ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
	columnTwoConstrains.setHgrow(Priority.ALWAYS);

	grid.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

	return grid;
    }

    public void populateQueryUI(GridPane grid) {
	FlowPane filterBox = new FlowPane();
	filterBox.setHgap(10);
	filterBox.setVgap(15);
	GridPane.setConstraints(filterBox, 1, 1);
	TextArea filter_criteria = new TextArea();
	filter_criteria.setText("");

	Label languagesLabel = new Label("Keywords:");
	GridPane.setConstraints(languagesLabel, 0, 0);

	TextField codeInput = new TextField();

	TextFields.bindAutoCompletion(codeInput, "Frontend", "Backend", "SQL", "Database", "Jira", ".NET", "Mobile",
		"Android", "Machine Learning", "Load Runner", "MongoDB", "Scrum", "Apache Hadoop", "UML", "PostgreSQL",
		"PERL", "Crypography", "JQuery", "Slack API", "BugZilla");
	codeInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent ke) {
		switch (ke.getCode()) {
		case ENTER:
		    if (!searchInfo.contains(codeInput.getText())) {
			filter_criteria.appendText(codeInput.getText() + " ");
			searchInfo.add(codeInput.getText());
			filterBox.getChildren().add(new Tag(codeInput.getText()));
		    }
		    codeInput.setText("");
		default:
		    break;
		}
	    }
	});

	codeInput.setPromptText("Keywords");
	GridPane.setConstraints(codeInput, 1, 0);

	Button activateButton = new Button("Search");
	GridPane.setConstraints(activateButton, 2, 2);

	ListView<String> language_list = new ListView<>();
	ObservableList<String> items = FXCollections.observableArrayList("Java", "C++", "Python", "Go", "Rust",
		"Kotlin", "Clojure", "Lisp", "WebAssembly", "JavaScript", "Ruby", "C#", "OWL", "Perl");

	language_list.setItems(items);
	language_list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	GridPane.setConstraints(language_list, 0, 1);

	Task<Void> task = new Task<Void>() {
	    @Override
	    protected Void call() throws Exception {
		Thread.sleep(200);
		int max = 100000000;
		for (int i = 0, j = 0; i < max; i++) {
		    if (i % 1000 == 0) {
			j++;

		    }
		    updateMessage("Developer: " + j);
		    updateProgress(i, max);
		}

		updateProgress(0, 0);
		masker.setVisible(false);
		done();
		return null;
	    }
	};
	
	task.setOnSucceeded(e -> {
	    showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Developer Query", "Search Complete");
	     try {
	        InputStream is = new FileInputStream("dev_data.json");
		String jsonTxt = IOUtils.toString(is, "UTF-8");
		System.out.println(jsonTxt);
		tableView.setItems(getDevelopers(jsonTxt));
	     } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	     }
	    
	    
	     window.setScene(finalScene);
	});

	Task<Void> sleeper = new Task<Void>() {
	    @Override
	    protected Void call() throws Exception {
		try {
		    Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		return null;
	    }
	};
	sleeper.setOnSucceeded(e -> {
	    new Thread(task).start();
	    masker.textProperty().bind(task.messageProperty());
	    masker.progressProperty().bind(task.progressProperty());
	});

	activateButton.setOnAction(e -> {
	    System.out.println(language_list.getSelectionModel().getSelectedItems());
	    System.out.println(filter_criteria.getText());
	    filter_fields.put("keywords", filter_criteria.getText());
	    filter_fields.put("languages", language_list.getSelectionModel().getSelectedItems().toString());
	    System.out.println(filter_fields.toString());
	    try (Writer writer = new BufferedWriter(
		    new OutputStreamWriter(new FileOutputStream("to_search.json"), "utf-8"))) {
		writer.write(filter_fields.toString());
	    } catch (Exception ei) {

		ei.printStackTrace();
	    }
	    
	    try {
		Process p = Runtime.getRuntime().exec("python driver.py");
	    } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	    
	    if (masker.isVisible()) {
		masker.setVisible(false);
	    } else {

		masker.setProgressVisible(true);
		masker.setVisible(true);
		masker.setText("Searching for Developers");
		new Thread(sleeper).start();
		
	    }
	    /*
	     * showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(),
	     * "Developer Query", "Searching Developers!");
	     */
	    window.setScene(searchForm);
	});
	grid.getChildren().addAll(languagesLabel, codeInput, activateButton, language_list, filterBox);
	grid.alignmentProperty().set(Pos.CENTER);
    }

    public void start(Stage primaryStage) throws Exception {
	window = primaryStage;
	window.setTitle("Developer Crawler");
	primaryStage.setMaximized(true);
	// Account login screen
	GridPane loginGrid = generateLoginGrid();
	addAccLoginUI(loginGrid);
	loginScene = new Scene(loginGrid, 600, 600);
	loginScene.getStylesheets().add("Viper.css");
	primaryStage.setMaximized(true);
	// Account registration screen
	GridPane accountCreationGrid = generateAccountCreationGrid();
	addAccCreationUI(accountCreationGrid);
	accCreation = new Scene(accountCreationGrid, 600, 600);
	accCreation.getStylesheets().add("Viper.css");
	primaryStage.setMaximized(true);
	// Account activation
	GridPane accActivationGrid = generateAccActivation();
	addAccActivationUI(accActivationGrid);
	accActivation = new Scene(accActivationGrid, 600, 600);
	accActivation.getStylesheets().add("Viper.css");

	MyMenu myMenu = new MyMenu(primaryStage);
	GridPane search_gp = generateSearchGrid();
	populateQueryUI(search_gp);
	BorderPane search_bp = myMenu.getBP();
	search_bp.setCenter(search_gp);

	masker = new MaskerPane();
	masker.setVisible(false);
	StackPane body = new StackPane();
	body.setPadding(new Insets(10, 0, 0, 0));
	body.getChildren().addAll(masker, search_bp);
	searchForm = new Scene(body, 600, 500);
	searchForm.getStylesheets().add("Viper.css");

	tableView = new TableView<>();

	TableColumn<Developer, Integer> idColumn = new TableColumn<>("ID");
	idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

	TableColumn<Developer, String> nameColumn = new TableColumn<>("Name");
	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

	TableColumn<Developer, String> emailColumn = new TableColumn<>("Email");
	emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

	TableColumn<Developer, String> hireColumn = new TableColumn<>("Job Searching");
	hireColumn.setCellValueFactory(new PropertyValueFactory<>("hireable"));
	
	TableColumn<Developer, String> locationColumn = new TableColumn<>("Location");
	locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

	
	tableView.getColumns().addAll(idColumn, nameColumn, emailColumn, hireColumn, locationColumn);
	
	finalScene = new Scene(tableView, 600, 500);
	
	
	window.setScene(searchForm);
	window.show();
    }
    
    ArrayList<Developer> developerList; 
    private ObservableList<Developer> getDevelopers(String jsonText) {
	developerList = new ArrayList<>();
	query_results = new JSONArray(jsonText);
	
	for(int j = 0; j < query_results.length(); j++) {
	    System.out.println(query_results.getJSONObject(j).get("name"));
	    developerList.add(new Developer(j+1, query_results.getJSONObject(j).get("name").toString(), query_results.getJSONObject(j).get("email").toString(), query_results.getJSONObject(j).get("hireable").toString(), query_results.getJSONObject(j).get("location").toString()));
	    
	}
        return FXCollections.observableArrayList(
        	developerList
        );
    }
    private class Tag extends Label {

	public Tag() {
	    super();
	    initTag();
	}

	private Tag(String arg0, Node arg1) {
	    super(arg0, arg1);
	}

	public Tag(String arg0) {
	    super(arg0);
	    initTag();
	}

	private final void initTag() {
	    Path path = new Path();
	    path.getElements().addAll(new MoveTo(0, 0), new LineTo(5, 5), new MoveTo(5, 0), new LineTo(0, 5));
	    path.setStyle("-fx-stroke: red;");
	    path.setOnMouseClicked(new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent paramT) {
		    Node n = Tag.this.getParent();
		    if (n instanceof Pane) {// of course it is
			((Pane) n).getChildren().remove(Tag.this);
		    }
		}
	    });
	    setPadding(new Insets(5, 7, 5, 7));
	    setGraphic(path);
	    setContentDisplay(ContentDisplay.RIGHT);
	    setGraphicTextGap(8);
	    graphicProperty().addListener(new ChangeListener<Node>() {
		@Override
		public void changed(ObservableValue<? extends Node> paramObservableValue, Node paramT1, Node paramT2) {
		    if (paramT2 != path) {
			setGraphic(path);
		    }
		}
	    });

	    setStyle("-fx-background-color: gold; " + "-fx-border-radius: 3;" + "-fx-border-color: red;");
	}

    }

    private GridPane generateAccountCreationGrid() {
	GridPane grid = new GridPane();
	grid.setAlignment(Pos.CENTER);
	grid.setPadding(new Insets(40, 40, 40, 40));
	grid.setHgap(10);
	grid.setVgap(10);
	ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
	columnOneConstraints.setHalignment(HPos.RIGHT);
	ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
	columnTwoConstrains.setHgrow(Priority.ALWAYS);

	grid.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

	return grid;
    }

    private GridPane generateLoginGrid() {
	GridPane grid = new GridPane();
	grid.setPadding(new Insets(10, 10, 10, 10));
	grid.setVgap(8);
	grid.setHgap(5);
	ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
	columnOneConstraints.setHalignment(HPos.RIGHT);
	ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
	columnTwoConstrains.setHgrow(Priority.ALWAYS);
	return grid;

    }

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
	Alert alert = new Alert(alertType);
	alert.setTitle(title);
	alert.setHeaderText(null);
	alert.setContentText(message);
	alert.initOwner(owner);
	DialogPane dialogPane = alert.getDialogPane();
	dialogPane.getStylesheets().add("Viper.css");

	alert.show();
    }

    private void addAccCreationUI(GridPane grid) {
	// Add Header
	Label titleLabel = new Label("Registration Form");
	titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	grid.add(titleLabel, 0, 0, 2, 1);
	GridPane.setHalignment(titleLabel, HPos.CENTER);
	GridPane.setMargin(titleLabel, new Insets(20, 0, 20, 0));

	// Add Name Label
	Label nameLabel = new Label("Username: ");
	grid.add(nameLabel, 0, 1);

	// Add Name Text Field
	TextField userNameField = new TextField();
	userNameField.setPrefHeight(40);
	grid.add(userNameField, 1, 1);

	// Add Email Label
	Label emailLabel = new Label("Email: ");
	grid.add(emailLabel, 0, 2);

	// Add Email Text Field
	TextField emailField = new TextField();
	emailField.setPrefHeight(40);
	grid.add(emailField, 1, 2);

	// Add Password Label
	Label passwordLabel = new Label("Password : ");
	grid.add(passwordLabel, 0, 3);

	// Add Password Field
	PasswordField passwordField = new PasswordField();
	passwordField.setPrefHeight(40);
	grid.add(passwordField, 1, 3);

	// Add Submit Button
	Button submitButton = new Button("Submit");
	submitButton.setPrefHeight(40);
	submitButton.setDefaultButton(true);
	submitButton.setPrefWidth(100);
	grid.add(submitButton, 0, 4, 2, 1);
	GridPane.setHalignment(submitButton, HPos.CENTER);
	GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

	submitButton.setOnAction(e -> {
	    if (userNameField.getText().isEmpty()) {
		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Incomplete field!",
			"Please input your desired username");
		return;
	    }
	    if (emailField.getText().isEmpty()) {
		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Incomplete field!",
			"Please input your mailing address");
		return;
	    }
	    if (passwordField.getText().isEmpty()) {
		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Incomplete field!",
			"Please input a password");
		return;
	    }
	    if (!Accounts.accAlreadyExists(emailField.getText())) {
		Accounts.addAccount(
			new Account(userNameField.getText(), passwordField.getText(), emailField.getText()));
		showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Successful Registration!",
			"Prepare to activate your account " + userNameField.getText());
		window.setScene(loginScene);
	    } else {
		showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Invalid Entry",
			"The email you have entered is already in use.\n");
	    }
	});
    }

    private void addAccLoginUI(GridPane grid) {
	Label nameLabel = new Label("Email:");
	GridPane.setConstraints(nameLabel, 0, 0);

	TextField nameInput = new TextField();
	nameInput.setPromptText("Email Address");
	GridPane.setConstraints(nameInput, 1, 0);

	Label passwordLabel = new Label("Password:");
	GridPane.setConstraints(passwordLabel, 0, 1);

	PasswordField passwordInput = new PasswordField();
	passwordInput.setPromptText("Password");
	GridPane.setConstraints(passwordInput, 1, 1);

	Button loginButton = new Button("Log in");
	GridPane.setConstraints(loginButton, 1, 2);

	Button registerButton = new Button("Create Account");
	GridPane.setConstraints(registerButton, 1, 2);
	GridPane.setHalignment(registerButton, HPos.RIGHT);

	// move to registration scene
	registerButton.setOnAction(e -> window.setScene(accCreation));

	loginButton.setOnAction(e -> {
	    if (Accounts.isAccount(nameInput.getText(), passwordInput.getText()) == 0
		    && Accounts.isValidated(nameInput.getText())) {
		showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Login Successful!",
			"Welcome to DevCrawler " + nameInput.getText());
		curr_user = Accounts.getAccount(nameInput.getText()).getUsername();
		window.setScene(searchForm);
	    } else if (Accounts.isAccount(nameInput.getText(), passwordInput.getText()) == 0
		    && !Accounts.isValidated(nameInput.getText())) {
		boolean openActivateScene = ConfirmBox.display("Activate Account, ",
			"You must activate your account before logging in.\n Would you like to activate your account now?");
		if (openActivateScene) {
		    JavaMail.sendValidationEmail(nameInput.getText());
		    showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Code Sent!",
			    "The activation code has been sent to the address " + nameInput.getText());
		    // account activation.
		    window.setScene(accActivation);
		}
	    } else if (Accounts.isAccount(nameInput.getText(), passwordInput.getText()) == 1) {
		showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Invalid Login Credentials",
			"You have entered the wrong password for the account " + nameInput.getText());
		Accounts.failedLoginAttempt(nameInput.getText());
	    } else if (Accounts.isAccount(nameInput.getText(), passwordInput.getText()) == 2) {
		boolean openRegistrationScene = ConfirmBox.display("Invalid Account",
			"Our records indicate that no account with that email exists. \n Would you like to create a new account?");
		if (openRegistrationScene) {
		    // account creation
		    window.setScene(accCreation);
		}
	    }
	});

	grid.getChildren().addAll(nameLabel, nameInput, passwordLabel, passwordInput, loginButton, registerButton);
	grid.alignmentProperty().set(Pos.CENTER);
    }

    public GridPane generateAccActivation() {
	GridPane grid = new GridPane();
	grid.setPadding(new Insets(10, 10, 10, 10));
	grid.setVgap(8);
	grid.setHgap(10);
	ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
	columnOneConstraints.setHalignment(HPos.RIGHT);
	ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
	columnTwoConstrains.setHgrow(Priority.ALWAYS);
	return grid;
    }

    public void addAccActivationUI(GridPane grid) {
	Label nameLabel = new Label("Activation Code:");
	GridPane.setConstraints(nameLabel, 0, 0);

	TextField codeInput = new TextField();
	codeInput.setPromptText("Code ");
	GridPane.setConstraints(codeInput, 1, 0);

	Button activateButton = new Button("Activate Account");
	GridPane.setConstraints(activateButton, 1, 2);

	activateButton.setOnAction(e -> {
	    if (ActivationKeyTable.validateKey(codeInput.getText())) {
		showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Activation Complete",
			"Your account has been activated!");
		window.setScene(loginScene);
	    } else {

	    }
	});
	grid.getChildren().addAll(nameLabel, codeInput, activateButton);
	grid.alignmentProperty().set(Pos.CENTER);
    }
    
    public static class Developer {
        public SimpleIntegerProperty idProperty = new SimpleIntegerProperty(this, "id");
        public SimpleStringProperty nameProperty = new SimpleStringProperty(this, "name");
        public SimpleStringProperty emailProperty = new SimpleStringProperty(this, "email");
        public SimpleStringProperty hireableProperty = new SimpleStringProperty(this, "hireable");
        public SimpleStringProperty locationProperty = new SimpleStringProperty(this, "location");
        //public SimpleStringProperty reposProperty = new SimpleStringProperty(this, "repos");


        
        public Developer(Integer id, String name, String email, String hireable, String location) {
            setId(id);
            setName(name);
            setEmail(email);
            this.hireableProperty.set(hireable);
            this.locationProperty.set(location);
            //this.reposProperty.set(repos);
        }
        
        public String getHireable() {
            return hireableProperty.get();
        }
        

        public SimpleStringProperty hireableProperty() {
            return hireableProperty;
        }

        public void setLocation(String hireable) {
            this.locationProperty.set(hireable);
        }
        
        public String getLocation() {
            return locationProperty.get();
        }

        public SimpleStringProperty locationProperty() {
            return locationProperty;
        }

        
        public Integer getId() {
            return idProperty.get();
        }

        public SimpleIntegerProperty idProperty() {
            return idProperty;
        }

        public void setId(int id) {
            this.idProperty.set(id);
        }

        public String getName() {
            return nameProperty.get();
        }

        public SimpleStringProperty nameProperty() {
            return nameProperty;
        }

        public void setName(String name) {
            this.nameProperty.set(name);
        }

        public String getEmail() {
            return emailProperty.get();
        }

        public SimpleStringProperty emailProperty() {
            return emailProperty;
        }

        public void setEmail(String email) {
            this.emailProperty.set(email);
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Developer customer = (Developer) o;

            return getId() != null ? getId().equals(customer.getId()) : customer.getId() == null;
        }

        @Override
        public int hashCode() {
            return getId() != null ? getId().hashCode() : 0;
        }
    }
}
