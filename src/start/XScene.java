package start;

import building.blueprint.*;
import geom.*;
import geom.d1.*;
import item.*;
import java.io.*;
import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import system2.*;
import visual1.*;
import visual1.keybind.*;

public class XScene extends Application
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
		Scheme scheme = new SchemeFile(loadTextResource("SchemeFile"));
		int width = scheme.intSetting("window.width");
		int height = scheme.intSetting("window.height");
		Group root = new Group();
		Scene s = new Scene(root, width, height, Color.BLACK);
		Canvas canvas = new Canvas(width, height);
		root.getChildren().add(canvas);
		stage.setScene(s);
		stage.setTitle(scheme.setting("window.title"));
		stage.getIcons().add(scheme.image("window.icon"));
		XGraphics graphics = new XGraphics(canvas.getGraphicsContext2D(), width, height);
		KeybindFile keybindFile = new KeybindFile(loadTextResource(scheme.setting("file.keybinds")));
		ItemLoader itemLoader = new ItemLoader2();
		BlueprintFile blueprintFile = new BlueprintFile(loadTextResource(scheme.setting("file.buildingblueprints")), itemLoader);
		MainVisual mainVisual = new MainVisual(graphics, keybindFile, scheme, mapCamera(scheme, graphics),
				menuCamera(scheme, graphics), guiCamera(scheme, graphics), a1 -> editorSlotCamera(scheme, graphics, a1),
				itemLoader, blueprintFile,
				scheme.setting("load.map"),
				scheme.setting("load.team"));
		XTimer xTimer = new XTimer(mainVisual, keybindFile);
		s.setOnMousePressed(xTimer::onMouseDown);
		s.setOnDragDetected(xTimer::onDragDetected);
		s.setOnMouseReleased(xTimer::onMouseUp);
		s.setOnMouseMoved(xTimer::onMouseMove);
		s.setOnMouseDragged(xTimer::onMouseMove);
		s.setOnMouseExited(xTimer::onMouseExit);
		s.setOnKeyPressed(xTimer::onKeyEvent);
		s.setOnKeyReleased(xTimer::onKeyUp);
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

	private TileCamera mapCamera(Scheme scheme, XGraphics graphics)
	{
		return switch(scheme.setting("camera.map"))
				{
					case "Hex" -> new HexCamera(graphics, 1, 1, MAP_SIZE_FACTOR, MAP_SIZE_FACTOR, 0, 0, new HexMatrix(0.5));
					case "Quad" -> new QuadCamera(graphics, 1, 1, MAP_SIZE_FACTOR, MAP_SIZE_FACTOR, 0, 0);
					default -> throw new RuntimeException("camera.map must be one of these: \"Hex\", \"Quad\"");
				};
	}

	private TileCamera menuCamera(Scheme scheme, XGraphics graphics)
	{
		return switch(scheme.setting("camera.menu"))
				{
					case "Hex" -> new HexCamera(graphics, 2, 1, MENU_SIZE_FACTOR, MENU_SIZE_FACTOR,
							1.25 * HexMatrix.Q3, 0, new HexMatrix(0.5));
					case "Quad" -> new QuadCamera(graphics, 2, 1, MENU_SIZE_FACTOR, MENU_SIZE_FACTOR, 2, 0);
					default -> throw new RuntimeException("camera.menu must be one of these: \"Hex\", \"Quad\"");
				};
	}

	private TileCamera guiCamera(Scheme scheme, XGraphics graphics)
	{
		return switch(scheme.setting("camera.gui"))
				{
					case "Hex" -> new HexCamera(graphics, 1, 1, GUI_SIZE_FACTOR, GUI_SIZE_FACTOR, 0, 0, new HexMatrix(0.5));
					case "Quad" -> new QuadCamera(graphics, 1, 1, GUI_SIZE_FACTOR, GUI_SIZE_FACTOR, 0, 0);
					default -> throw new RuntimeException("camera.gui must be one of these: \"Hex\", \"Quad\"");
				};
	}

	private TileCamera editorSlotCamera(Scheme scheme, XGraphics graphics, Double a1)
	{
		return switch(scheme.setting("camera.editorslot"))
				{
					case "Hex" -> new HexCamera(graphics, a1, 1.75, EDITOR_SCALE_X, EDITOR_SCALE_Y, 0, 0, new HexMatrix(0.5));
					case "Quad" -> new QuadCamera(graphics, a1, 1.75, EDITOR_SCALE_X, EDITOR_SCALE_Y, 0, 0);
					default -> throw new RuntimeException("camera.editorslot must be one of these: \"Hex\", \"Quad\"");
				};
	}

	private String loadTextResource(String location)
	{
		URL resource = Thread.currentThread().getContextClassLoader().getResource(location);
		if(resource == null)
		{
			throw new RuntimeException("Resource not found: \"" + location + "\"");
		}
		try
		{
			return new String(resource.openStream().readAllBytes());
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}