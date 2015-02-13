package uk.co.stikman.serkit.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import uk.co.stikman.serkit.gui.event.QuitEvent;
import uk.co.stikman.serkit.gui.event.StartStopEvent;

import com.google.common.eventbus.Subscribe;

public class Serkit extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private Stage				mainStage;
	private SimulationThread	runner;

	@Override
	public void start(final Stage primaryStage) throws Exception {
		mainStage = primaryStage;
		primaryStage.setTitle("Serkit");
		final MainWindow wnd = new MainWindow("MainWnd.fxml");
		wnd.go(primaryStage);

		runner = new SimulationThread() {

			@Override
			protected void log(final String msg) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						wnd.addLogMessage(msg);
					}
				});
			}
		};
		runner.setDaemon(true);
		runner.start();
		
		bind();
	}

	private void bind() {
		AppEventBus.get().register(this);
	}

	@Subscribe
	public void quitEvent(QuitEvent e) {
		mainStage.close();
	}

	@Subscribe
	public void startStopEvent(StartStopEvent e) {
		runner.setPaused(!e.isStart());
	}
}
