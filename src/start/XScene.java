package start;

import geom.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
		XGraphics graphics = new XGraphics(canvas.getGraphicsContext2D(), WIDTH, HEIGHT);
		XTimer xTimer = new XTimer(graphics);
		xTimer.start();
		s.setOnMouseClicked(xTimer::onMouseClick);
		s.setOnMouseMoved(xTimer::onMouseMove);
		s.setOnKeyPressed(xTimer::onKeyEvent);
		stage.widthProperty().addListener((e, f, g) ->
		{
			canvas.setWidth(g.doubleValue());
			graphics.setxW(g.doubleValue());
		});
		stage.heightProperty().addListener((e, f, g) ->
		{
			canvas.setHeight(g.doubleValue());
			graphics.setyW(g.doubleValue());
		});
		stage.show();
	}
}