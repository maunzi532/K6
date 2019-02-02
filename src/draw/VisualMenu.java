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
	private final XMenu XMenu;

	public VisualMenu(GraphicsContext gd, double xHalfWidth, double yHalfWidth, XMenu XMenu)
	{
		this.gd = gd;
		camera = new XCamera(xHalfWidth * 3 / 2, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 0,  0, MatrixH.LP);
		this.XMenu = XMenu;
	}

	private Hex optionToHex(int i, int optionCount)
	{
		return new Hex(optionCount / 4 - i / 2, i - optionCount / 2);
	}

	public int hexToOption(Hex hex)
	{
		int optionCount = XMenu.getEntries().size();
		for(int i = 0; i < optionCount; i++)
		{
			if(hex.equals(optionToHex(i, optionCount)))
				return i;
		}
		return -1;
	}

	public void draw()
	{
		List<XMenuEntry> menuEntries = this.XMenu.getEntries();
		camera.yShift = menuEntries.size() % 2 == 0 ? -0.5 : 0d;
		for(int i = 0; i < menuEntries.size(); i++)
		{
			draw0(camera.layout(), optionToHex(i, menuEntries.size()), menuEntries.get(i).text, menuEntries.get(i) == this.XMenu
					.getCurrent());
			//draw0(camera.layout(), new Hex(0, i - menuEntries.size() / 2), menuEntries.get(i));
			//draw0(camera.layout(), new Hex(menuEntries.size() / 2 - i, i * 2 - menuEntries.size()), menuEntries.get(i));
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
		//gd.setStroke(Color.BLACK);
		gd.setFont(new Font(layout.size.v[1] * 0.5));
		//gd.strokeText(text, midPoint.v[0] - layout.size.v[0] * 0.7, midPoint.v[1] + layout.size.v[1] * 0.2, layout.size.v[0] * 1.4);
		gd.fillText(text, midPoint.v[0] - layout.size.v[0] * 0.7, midPoint.v[1] + layout.size.v[1] * 0.2, layout.size.v[0] * 1.4);
	}
}