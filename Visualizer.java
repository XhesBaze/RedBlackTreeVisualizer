package application;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Visualizer extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane pane = new BorderPane();
        VBox welcomePage = createWelcomePage(primaryStage, pane);
        pane.setCenter(welcomePage);
        Scene scene = new Scene(pane);
        String css = this.getClass().getResource("style.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
    }

    private VBox createWelcomePage(Stage primaryStage, BorderPane pane) {
        VBox welcomePage = new VBox();
        welcomePage.getStyleClass().add("welcome-page");
        Label welcomeLabel = new Label("Welcome to the Red-Black Tree Visualizer!");
        welcomeLabel.getStyleClass().add("welcome-label");
        Button continueButton = new Button("Continue to Visualizer");
        continueButton.getStyleClass().add("continue-button");
        continueButton.setOnAction(e -> {
            pane.setCenter(createVisualizer(primaryStage, pane));
        });
        welcomePage.getChildren().addAll(welcomeLabel, continueButton);
        return welcomePage;
    }

    private VBox createVisualizer(Stage primaryStage, BorderPane pane) {
        VBox visualizer = new VBox();
        HBox header = new HBox();
        HBox footer = new HBox();
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        TreeView<Integer> view = new TreeView<>(tree); // Create TreeView instance
        // Header
        Label title = new Label();
        Label status = new Label("The tree is empty");
        header.getChildren().addAll(title, status);
        // Footer
        Label enterText = new Label("Enter a key ");
        TextField textField = new TextField();
        textField.setPrefColumnCount(3);
        textField.setAlignment(Pos.BASELINE_RIGHT);
        Button btnInsert = new Button("Insert");
        Button btnDelete = new Button("Delete");
        Button btnClose = new Button("Close");
        footer.getChildren().addAll(enterText, textField, btnInsert, btnDelete, btnClose);
        footer.setAlignment(Pos.CENTER);
        // Return to welcome page button
        btnClose.setOnAction(e -> {
            primaryStage.close();
        });
        // Integer button
        btnInsert.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try {
                    int key = Integer.parseInt(textField.getText());
                    if (tree.isEmpty()) {
                        tree.insert(key);
                        view.displayTree();
                        status.setText(key + " is inserted into the tree");
                    } else if (tree.find(key)) {
                        view.displayTree();
                        status.setText(key + " is already in the tree");
                    } else {
                        tree.insert(key);
                        view.displayTree();
                        status.setText(key + " is inserted into the tree");
                    }
                } catch (NumberFormatException e) {
                    status.setText("You must enter an integer");
                }
            }
        });
        btnDelete.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try {
                    int key = Integer.parseInt(textField.getText());
                    if (tree.isEmpty()) {
                        view.displayTree();
                        status.setText("The tree is empty");
                    } else if (!tree.find(key)) {
                        view.displayTree();
                        status.setText(key + " is not in the tree");
                    } else {
                        tree.delete(key);
                        view.displayTree();
                        status.setText(key + " is deleted from the tree");
                    }
                } catch (NumberFormatException e) {
                    status.setText("You must enter an integer");
                }
            }
        });
        // Visualizer styling
        visualizer.getStyleClass().add("visualizer");
        status.getStyleClass().add("status");
        header.getStyleClass().add("header");
        enterText.getStyleClass().add("enter-text");

        // Add the TreeView to the visualizer VBox
        visualizer.getChildren().addAll(header, view, footer);
        return visualizer;
    }
}