package visual1;

import geom.*;
import geom.d1.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.*;
import logic.xstate.*;

public class VisualMenu
{
	private final DoubleType y2;
	private final XGraphics graphics;
	private final TileCamera camera;
	private final XStateHolder stateHolder;
	private final XKeyMap keyMap;
	private double lastZoom;
	private double lastShift;

	public VisualMenu(XGraphics graphics, XStateHolder stateHolder, TileCamera camera, XKeyMap keyMap)
	{
		this.graphics = graphics;
		this.camera = camera;
		y2 = camera.getDoubleType();
		this.stateHolder = stateHolder;
		this.keyMap = keyMap;
	}

	public int coordinatesToOption(double x, double y)
	{
		Tile offset = y2.toOffset(y2.cast(camera.clickLocation(x, y)));
		if(offset.v()[0] != 0 || offset.v()[1] < 0 || offset.v()[1] >= stateHolder.getMenu().size())
			return -1;
		return offset.v()[1];
	}

	public void draw(double tLimit, double bLimit)
	{
		List<NState> menuEntries = stateHolder.getMenu();
		if(menuEntries.size() > 0)
		{
			double tLimitC = tLimit / camera.ySizeNZ();
			double bLimitC = bLimit / camera.ySizeNZ();
			double size1 = 0.75 * menuEntries.size() + 0.25;
			double zoom = 1;
			double yShift = 0;
			if(size1 > tLimitC || size1 > bLimitC)
			{
				if(size1 * 2 > tLimitC + bLimitC)
				{
					zoom = (tLimitC + bLimitC) / (size1 * 2);
					size1 = (tLimitC + bLimitC) / 2;
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
			if(lastZoom <= 0)
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
			for(int i = 0; i < menuEntries.size(); i++)
			{
				draw0(camera.layout(0), y2.fromOffset(0, i), menuEntries.get(i),
						menuEntries.get(i).text().equals(stateHolder.getState().text()));
			}
		}
		else
		{
			lastZoom = 0;
			lastShift = 0;
		}
	}

	private static double adapt(double prev, double target, double speed)
	{
		return Math.min(Math.max(target, (prev * 0.9 + target * 0.1) - speed), (prev * 0.9 + target * 0.1) + speed);
	}

	private void draw0(TileLayout layout, Tile h1, NState menuEntry, boolean active)
	{
		GraphicsContext gd = graphics.gd();
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
		gd.setFont(new Font(layout.size().v()[1] * 0.5));
		if(menuEntry.keybind() != null)
		{
			gd.fillText(menuEntry.text(),
					midPoint.v()[0], midPoint.v()[1], layout.size().v()[0] * 1.4);
			gd.setFill(Color.DARKSLATEGRAY);
			gd.setFont(new Font(layout.size().v()[1] * 0.3));
			gd.fillText(keyMap.info(menuEntry.keybind()), midPoint.v()[0], midPoint.v()[1] - layout.size().v()[1] * 0.5, layout.size().v()[0] * 1.4);
		}
		else
		{
			gd.fillText(menuEntry.text(), midPoint.v()[0], midPoint.v()[1], layout.size().v()[0] * 1.4);
		}
	}
}