package start;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.stage.*;

public class XScene extends Application
{
	private static final int WIDTH = 1000, HEIGHT = 800;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		Group root = new Group();
		Scene s = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);
		stage.setScene(s);
		stage.setTitle("K6");
		//stage.initStyle(StageStyle.UNDECORATED);
		GraphicsContext gd = canvas.getGraphicsContext2D();

		XTimer xTimer = new XTimer(gd, WIDTH, HEIGHT);
		xTimer.start();
		s.setOnMouseClicked(xTimer::onMouseEvent);
		s.setOnKeyPressed(xTimer::onKeyEvent);

		stage.show();
	}
}