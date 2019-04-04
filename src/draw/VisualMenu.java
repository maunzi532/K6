package draw;

import geom.*;
import geom.d1.*;
import geom.f1.*;
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

	public VisualMenu(GraphicsContext gd, double xHalfWidth, double yHalfWidth, XStateControl stateControl)
	{
		this.gd = gd;
		camera = new HexCamera(xHalfWidth * 2, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 1.25 * HexMatrix.Q3,  0, HexMatrix.LP);
		//camera = new QuadCamera(xHalfWidth * 2, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 1.25 * HexMatrix.Q3,  0);
		y2 = camera.getDoubleType();
		this.stateControl = stateControl;
	}

	public int coordinatesToOption(double x, double y)
	{
		Tile offset = y2.toOffset(y2.cast(camera.clickLocation(x, y)));
		if(offset.v[0] != 0 || offset.v[1] < 0 || offset.v[1] >= stateControl.getMenu().size())
			return -1;
		return offset.v[1];
	}

	public void draw()
	{
		List<XState> menuEntries = stateControl.getMenu();
		camera.setYShift((menuEntries.size() - 1) * 1.5 / 2d);
		for(int i = 0; i < menuEntries.size(); i++)
		{
			draw0(camera.layout(), y2.fromOffset(0, i), menuEntries.get(i).text, menuEntries.get(i) == stateControl.getState());
		}
	}

	private void draw0(TileLayout layout, Tile h1, String text, boolean active)
	{
		double[][] points = layout.tileCorners(h1);
		int dch = y2.directionCount() / 2;
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
		gd.fillPolygon(points[0], points[1], y2.directionCount());
		PointD midPoint = layout.tileToPixel(h1);
		gd.setFill(Color.BLACK);
		gd.setFont(new Font(layout.size().v[1] * 0.5));
		gd.fillText(text, midPoint.v[0], midPoint.v[1], layout.size().v[0] * 1.4);
	}
}