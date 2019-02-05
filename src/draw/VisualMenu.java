package draw;

import hex.*;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.*;

public class VisualMenu
{
	private final GraphicsContext gd;
	public final XCamera camera;
	private final XMenu xMenu;

	public VisualMenu(GraphicsContext gd, double xHalfWidth, double yHalfWidth, XMenu xMenu)
	{
		this.gd = gd;
		camera = new XCamera(xHalfWidth * 2, yHalfWidth,
				yHalfWidth / 8, yHalfWidth / 8, 1.25 * MatrixH.Q3,  0, MatrixH.LP);
		this.xMenu = xMenu;
	}

	private Hex optionToHex(int i)
	{
		return new OffsetHex(0, i).toHex();
	}

	public int hexToOption(Hex hex)
	{
		int optionCount = xMenu.getEntries().size();
		for(int i = 0; i < optionCount; i++)
		{
			if(hex.equals(optionToHex(i)))
				return i;
		}
		return -1;
	}

	public void draw()
	{
		List<XMenuEntry> menuEntries = xMenu.getEntries();
		camera.yShift = (menuEntries.size() - 1) * 1.5 / 2d;
		for(int i = 0; i < menuEntries.size(); i++)
		{
			draw0(camera.layout(), optionToHex(i), menuEntries.get(i).text,
					menuEntries.get(i) == xMenu.getCurrent());
		}
	}

	public void draw0(LayoutH layout, Hex h1, String text, boolean active)
	{
		double[][] points = layout.hexCorners(h1);
		if(active)
		{
			gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][3], points[1][3],
					false, null, new Stop(0, Color.DARKGREEN), new Stop(1, Color.DARKRED)));
		}
		else
		{
			gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][3], points[1][3],
					false, null, new Stop(0, Color.YELLOWGREEN), new Stop(1, Color.ORANGERED)));
		}
		gd.fillPolygon(points[0], points[1], 6);
		PointD midPoint = layout.hexToPixel(h1);
		gd.setFill(Color.BLACK);
		gd.setFont(new Font(layout.size.v[1] * 0.5));
		gd.fillText(text, midPoint.v[0] - layout.size.v[0] * 0.7, midPoint.v[1] + layout.size.v[1] * 0.2, layout.size.v[0] * 1.4);
	}
}