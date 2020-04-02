package start;

import building.blueprint.*;
import geom.*;
import geom.advtile.*;
import item.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import statsystem.*;
import vis.*;

public final class XScene extends Application
{
	private static final double MAP_SIZE_FACTOR = 0.088;
	private static final double MENU_SIZE_FACTOR = 0.1;
	private static final double GUI_SIZE_FACTOR = 0.1;
	private static final double EDITOR_SCALE_X = 0.125;
	private static final double EDITOR_SCALE_Y = 0.1;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		SchemeFile scheme = new SchemeFile(loadTextResource("SchemeFile"), XScene::loadTextResource);
		int width = scheme.intSetting("window.width");
		int height = scheme.intSetting("window.height");
		Group root = new Group();
		Scene scene = new Scene(root, width, height, Color.BLACK);
		Canvas canvas = new Canvas(width, height);
		root.getChildren().add(canvas);
		stage.setScene(scene);
		stage.setTitle(scheme.setting("window.title"));
		stage.getIcons().add(scheme.image("window.icon"));
		XGraphics graphics = new XGraphics(canvas.getGraphicsContext2D(), width, height);
		ItemLoader itemLoader = new ItemLoader2();
		BlueprintFile blueprintFile = new BlueprintFile(loadTextResource(scheme.setting("file.buildingblueprint")), itemLoader);
		MainVisual mainVisual = new MainVisual(graphics, scheme.keybindFile(), scheme, mapCamera(scheme, graphics),
				menuCamera(scheme, graphics), guiCamera(scheme, graphics), a1 -> editorSlotCamera(scheme, graphics, a1),
				itemLoader, blueprintFile,
				scheme.setting("load.map"),
				scheme.setting("load.team"));
		XTimer xTimer = new XTimer(mainVisual, scheme.keybindFile());
		scene.setOnMousePressed(xTimer::onMouseDown);
		scene.setOnDragDetected(xTimer::onDragDetected);
		scene.setOnMouseReleased(xTimer::onMouseUp);
		scene.setOnMouseMoved(xTimer::onMouseMove);
		scene.setOnMouseDragged(xTimer::onMouseMove);
		scene.setOnMouseExited(xTimer::onMouseExit);
		scene.setOnKeyPressed(xTimer::onKeyEvent);
		scene.setOnKeyReleased(xTimer::onKeyUp);
		scene.widthProperty().addListener((e1, e2, number) ->
		{
			canvas.setWidth(number.doubleValue());
			graphics.setxW(number.doubleValue());
		});
		scene.heightProperty().addListener((e1, e2, number) ->
		{
			canvas.setHeight(number.doubleValue());
			graphics.setyW(number.doubleValue());
		});
		xTimer.start();
		stage.show();
	}

	private TileCamera mapCamera(Scheme scheme, XGraphics graphics)
	{
		return switch(scheme.setting("camera.map"))
				{
					case "hex" -> new HexCamera(graphics, 1.0, 1.0, MAP_SIZE_FACTOR, MAP_SIZE_FACTOR, 0.0, 0.0, new HexMatrix(0.5));
					case "quad" -> new QuadCamera(graphics, 1.0, 1.0, MAP_SIZE_FACTOR, MAP_SIZE_FACTOR, 0.0, 0.0);
					default -> throw new RuntimeException("camera.map must be one of these: \"hex\", \"quad\"");
				};
	}

	private TileCamera menuCamera(Scheme scheme, XGraphics graphics)
	{
		return switch(scheme.setting("camera.menu"))
				{
					case "hex" -> new HexCamera(graphics, 2.0, 1.0, MENU_SIZE_FACTOR, MENU_SIZE_FACTOR,
							1.25 * HexMatrix.Q3, 0.0, new HexMatrix(0.5));
					case "quad" -> new QuadCamera(graphics, 2.0, 1.0, MENU_SIZE_FACTOR, MENU_SIZE_FACTOR, 2.0, 0.0);
					default -> throw new RuntimeException("camera.menu must be one of these: \"hex\", \"quad\"");
				};
	}

	private TileCamera guiCamera(Scheme scheme, XGraphics graphics)
	{
		return switch(scheme.setting("camera.gui"))
				{
					case "hex" -> new HexCamera(graphics, 1.0, 1.0, GUI_SIZE_FACTOR, GUI_SIZE_FACTOR, 0.0, 0.0, new HexMatrix(0.5));
					case "quad" -> new QuadCamera(graphics, 1.0, 1.0, GUI_SIZE_FACTOR, GUI_SIZE_FACTOR, 0.0, 0.0);
					default -> throw new RuntimeException("camera.gui must be one of these: \"hex\", \"quad\"");
				};
	}

	private TileCamera editorSlotCamera(Scheme scheme, XGraphics graphics, Double a1)
	{
		return switch(scheme.setting("camera.editorslot"))
				{
					case "hex" -> new HexCamera(graphics, a1, 1.75, EDITOR_SCALE_X, EDITOR_SCALE_Y, 0.0, 0.0, new HexMatrix(0.5));
					case "quad" -> new QuadCamera(graphics, a1, 1.75, EDITOR_SCALE_X, EDITOR_SCALE_Y, 0.0, 0.0);
					default -> throw new RuntimeException("camera.editorslot must be one of these: \"hex\", \"quad\"");
				};
	}

	private static String loadTextResource(String location)
	{
		URL resource = Thread.currentThread().getContextClassLoader().getResource(location);
		if(resource == null)
		{
			throw new RuntimeException("Resource not found: \"" + location + "\"");
		}
		try(InputStream inputStream = resource.openStream())
		{
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}