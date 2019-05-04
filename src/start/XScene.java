package start;

import geom.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class XScene extends Application
{
	private static final int WIDTH = 1000, HEIGHT = 800;

	private static String[] args0;

	public static void main(String[] args)
	{
		args0 = args;
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
		stage.getIcons().add(new Image("Tech.png"));
		XGraphics graphics = new XGraphics(canvas.getGraphicsContext2D(), WIDTH, HEIGHT);
		XTimer xTimer = new XTimer(graphics, args0.length > 0 && args0[0].equals("H"),
				args0.length > 1 && args0[1].equals("H"), args0.length > 2 && args0[2].equals("H"));
		xTimer.start();
		s.setOnMouseClicked(xTimer::onMouseClick);
		s.setOnMouseMoved(xTimer::onMouseMove);
		s.setOnMouseExited(xTimer::onMouseExit);
		s.setOnKeyPressed(xTimer::onKeyEvent);
		s.widthProperty().addListener((e, f, g) ->
		{
			canvas.setWidth(g.doubleValue());
			graphics.setxW(g.doubleValue());
		});
		s.heightProperty().addListener((e, f, g) ->
		{
			canvas.setHeight(g.doubleValue());
			graphics.setyW(g.doubleValue());
		});
		stage.show();
	}
}