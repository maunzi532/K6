package vis;

import geom.*;
import geom.advtile.*;
import geom.tile.*;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.*;
import logic.xstate.*;

public final class VisualMenu
{
	private final XGraphics graphics;
	private double lastZoom;
	private double lastShift;

	public VisualMenu(XGraphics graphics)
	{
		this.graphics = graphics;
	}

	public int coordinatesToOption(TileCamera camera, double x, double y, XStateHolder stateHolder)
	{
		DoubleType y2 = camera.getDoubleType();
		Tile offset = y2.toOffset(y2.cast(camera.clickLocation(x, y)));
		if(offset.v()[0] != 0 || offset.v()[1] < 0 || offset.v()[1] >= stateHolder.getMenu().size())
			return -1;
		return offset.v()[1];
	}

	public void draw(TileCamera camera, double tLimit, double bLimit, XStateHolder stateHolder, XKeyMap keyMap, Scheme scheme)
	{
		List<NState> menuEntries = stateHolder.getMenu();
		if(!menuEntries.isEmpty())
		{
			double tLimitC = tLimit / camera.ySizeNZ();
			double bLimitC = bLimit / camera.ySizeNZ();
			double size1 = 0.75 * menuEntries.size() + 0.25;
			double zoom = 1.0;
			double yShift = 0.0;
			if(size1 > tLimitC || size1 > bLimitC)
			{
				if(size1 * 2.0 > tLimitC + bLimitC)
				{
					zoom = (tLimitC + bLimitC) / (size1 * 2.0);
					size1 = (tLimitC + bLimitC) / 2.0;
				}
				if(tLimitC < bLimitC)
				{
					yShift -= size1 - tLimitC;
				}
				else
				{
					yShift += size1 - bLimitC;
				}
			}
			if(lastZoom <= 0.0)
			{
				lastZoom = zoom;
				lastShift = yShift;
			}
			else
			{
				lastZoom = adapt(lastZoom, zoom, 0.02);
				lastShift = adapt(lastShift, yShift, 0.15);
			}
			camera.setZoom(lastZoom);
			camera.setYShift(lastShift + (menuEntries.size() - 1) * 0.75);
			TileType y1 = camera.getDoubleType();
			for(int i = 0; i < menuEntries.size(); i++)
			{
				draw0(y1, camera.layout(0), y1.fromOffset(0, i), menuEntries.get(i),
						menuEntries.get(i).text().equals(stateHolder.getState().text()), keyMap, scheme);
			}
		}
		else
		{
			lastZoom = 0.0;
			lastShift = 0.0;
		}
	}

	private static double adapt(double prev, double target, double speed)
	{
		return Math.min(Math.max(target, (prev * 0.9 + target * 0.1) - speed), (prev * 0.9 + target * 0.1) + speed);
	}

	private void draw0(TileType y1, TileLayout layout, Tile h1, NState menuEntry, boolean active, XKeyMap keyMap, Scheme scheme)
	{
		GraphicsContext gd = graphics.gd();
		double[][] points = layout.tileCorners(h1);
		int dch = y1.directionCount() / 2;
		if(active)
		{
			gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][dch], points[1][dch],
					false, null, new Stop(0.0, scheme.color("menu.background.active.1")),
					new Stop(1.0, scheme.color("menu.background.active.2"))));
		}
		else
		{
			gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][dch], points[1][dch],
					false, null, new Stop(0.0, scheme.color("menu.background.1")),
					new Stop(1.0, scheme.color("menu.background.2"))));
		}
		gd.fillPolygon(points[0], points[1], y1.directionCount());
		PointD midPoint = layout.tileToPixel(h1);
		gd.setFill(scheme.color("menu.text"));
		gd.setFont(new Font(layout.size().v()[1] * 0.5));
		if(menuEntry.keybind() != null)
		{
			gd.fillText(menuEntry.text(),
					midPoint.v()[0], midPoint.v()[1], layout.size().v()[0] * 1.4);
			gd.setFill(scheme.color("menu.text.keybind"));
			gd.setFont(new Font(layout.size().v()[1] * 0.3));
			gd.fillText(keyMap.info(menuEntry.keybind()), midPoint.v()[0], midPoint.v()[1] - layout.size().v()[1] * 0.5, layout.size().v()[0] * 1.4);
		}
		else
		{
			gd.fillText(menuEntry.text(), midPoint.v()[0], midPoint.v()[1], layout.size().v()[0] * 1.4);
		}
	}
}