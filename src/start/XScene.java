package start;

import geom.*;
import geom.d1.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import logic.*;

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
		MainVisual mainVisual = new MainVisual(graphics,
				mapCamera(args0, graphics),
				menuCamera(args0, graphics),
				guiCamera(args0, graphics),
				args0.length > 3 ? args0[3] : null);
		XTimer xTimer = new XTimer(mainVisual);
		s.setOnMousePressed(xTimer::onMouseDown);
		s.setOnDragDetected(xTimer::onDragDetected);
		s.setOnMouseReleased(xTimer::onMouseUp);
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
		xTimer.start();
		stage.show();
	}

	private TileCamera mapCamera(String[] args, XGraphics graphics)
	{
		if(args.length > 0 && args[0].equals("H"))
			return new HexCamera(graphics, 1, 1, 44, 44, 0, 0, new HexMatrix(0.5));
		else
			return new QuadCamera(graphics, 1, 1, 44, 44, 0, 0);
	}

	private TileCamera menuCamera(String[] args, XGraphics graphics)
	{
		if(args.length > 1 && args[1].equals("H"))
			return new HexCamera(graphics, 2, 1, graphics.yHW() / 8, graphics.yHW() / 8, 1.25 * HexMatrix.Q3, 0, HexMatrix.LP);
		else
			return new QuadCamera(graphics, 2, 1, graphics.yHW() / 8, graphics.yHW() / 8, 1.25 * HexMatrix.Q3, 0);
	}

	private TileCamera guiCamera(String[] args, XGraphics graphics)
	{
		if(args.length > 2 && args[2].equals("H"))
			return new HexCamera(graphics, 1, 1, graphics.yHW() / 8, graphics.yHW() / 8, 0, 0, HexMatrix.LP);
		else
			return new QuadCamera(graphics, 1, 1, graphics.yHW() / 8, graphics.yHW() / 8, 0, 0);
	}
}