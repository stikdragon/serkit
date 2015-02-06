package uk.co.stikman.serkit.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Serkit extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		primaryStage.setTitle("Serkit");

		BorderPane border = new BorderPane();

		Text title = new Text("Serkit");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		border.setTop(title);

		Button btn = new Button();
		btn.setText("Quit");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					primaryStage.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		border.setBottom(btn);

		primaryStage.setScene(new Scene(border, 300, 250));
		primaryStage.show();
	}

}
