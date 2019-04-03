package draw;

import geom.*;
import geom.d1.*;
import geom.f1.*;
import geom.hex.*;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.*;

public class VisualMenu
{
	private final DoubleType y2;
	private final GraphicsContext gd;
	private final TileCamera camera;
	private final XStateControl stateControl;

	public VisualMenu(DoubleType y2, GraphicsContext gd, double xHalfWidth, double yHalfWidth,
			XStateControl stateControl)
	{
		this.y2 = y2;
		this.gd = gd;
		camera = new HexCamera(y2, xHalfWidth * 2, yHalfWidth,
				yHalfWidth / 8, yHalfWidth / 8, 1.25 * HexMatrix.Q3,  0, HexMatrix.LP);
		this.stateControl = stateControl;
	}

	private Tile optionToTile(int i)
	{
		return asHex(0, i); //TODO
	}

	private Tile asHex(int n1, int n2)
	{
		return y2.y1().create2(n1 - n2 / 2, n2);
	}

	public int coordinatesToOption(double x, double y)
	{
		return tileToOption(y2.cast(camera.clickLocation(x, y)));
	}

	public int tileToOption(Tile t1)
	{
		int optionCount = stateControl.getMenu().size();
		for(int i = 0; i < optionCount; i++)
		{
			if(t1.equals(optionToTile(i)))
				return i;
		}
		return -1;
	}

	public void draw()
	{
		List<XState> menuEntries = stateControl.getMenu();
		camera.setYShift((menuEntries.size() - 1) * 1.5 / 2d);
		for(int i = 0; i < menuEntries.size(); i++)
		{
			draw0(camera.layout(), optionToTile(i), menuEntries.get(i).text,
					menuEntries.get(i) == stateControl.getState());
		}
	}

	public void draw0(TileLayout layout, Tile h1, String text, boolean active)
	{
		double[][] points = layout.tileCorners(h1);
		int dch = y2.y1().directionCount() / 2;
		if(active)
		{
			gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][dch], points[1][dch],
					false, null, new Stop(0, Color.DARKGREEN), new Stop(1, Color.DARKRED)));
		}
		else
		{
			gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][dch], points[1][dch],
					false, null, new Stop(0, Color.YELLOWGREEN), new Stop(1, Color.ORANGERED)));
		}
		gd.fillPolygon(points[0], points[1], y2.y1().directionCount());
		PointD midPoint = layout.tileToPixel(h1);
		gd.setFill(Color.BLACK);
		gd.setFont(new Font(layout.size().v[1] * 0.5));
		gd.fillText(text, midPoint.v[0], midPoint.v[1], layout.size().v[0] * 1.4);
	}
}