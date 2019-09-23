package visual;

import geom.*;
import geom.d1.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.xstate.*;

public class VisualMenu
{
	private final DoubleType y2;
	private final GraphicsContext gd;
	private final TileCamera camera;
	private final XStateHolder stateHolder;

	public VisualMenu(XGraphics graphics, XStateHolder stateHolder, TileCamera camera)
	{
		gd = graphics.gd();
		this.camera = camera;
		y2 = camera.getDoubleType();
		this.stateHolder = stateHolder;
	}

	public int coordinatesToOption(double x, double y)
	{
		Tile offset = y2.toOffset(y2.cast(camera.clickLocation(x, y)));
		if(offset.v[0] != 0 || offset.v[1] < 0 || offset.v[1] >= stateHolder.getMenu().size())
			return -1;
		return offset.v[1];
	}

	public void draw()
	{
		List<NState> menuEntries = stateHolder.getMenu();
		camera.setYShift((menuEntries.size() - 1) * 1.5 / 2d);
		for(int i = 0; i < menuEntries.size(); i++)
		{
			draw0(camera.layout(), y2.fromOffset(0, i), menuEntries.get(i), menuEntries.get(i) == stateHolder.getState());
		}
	}

	private void draw0(TileLayout layout, Tile h1, NState menuEntry, boolean active)
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
		if(menuEntry.keybind() != null)
		{
			gd.fillText(menuEntry.text(),
					midPoint.v[0], midPoint.v[1], layout.size().v[0] * 1.4);
			gd.setFill(Color.DARKSLATEGRAY);
			gd.setFont(new Font(layout.size().v[1] * 0.3));
			gd.fillText(menuEntry.keybind().getName(),
					midPoint.v[0], midPoint.v[1] - layout.size().v[1] * 0.5, layout.size().v[0] * 1.4);
		}
		else
		{
			gd.fillText(menuEntry.text(), midPoint.v[0], midPoint.v[1], layout.size().v[0] * 1.4);
		}
	}
}