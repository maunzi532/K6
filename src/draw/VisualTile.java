package draw;

import geom.*;
import geom.hex.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import levelMap.*;

public class VisualTile
{
	private LevelMap levelMap;
	private GraphicsContext gd;

	public VisualTile(LevelMap levelMap, GraphicsContext gd)
	{
		this.levelMap = levelMap;
		this.gd = gd;
	}

	public void draw(HexCamera camera)
	{
		int range = camera.getRange();
		HexLayout layout = camera.layout();
		Hex mid = camera.mid(layout);
		for(int i = -range; i <= range; i++)
		{
			for(int j = Math.min(0, -i) + range; j >= Math.max(0, -i) - range; j--)
			{
				draw0(layout, mid.add(new Hex(j, i)));
			}
		}
		drawArrows0(layout, mid, range);
		for(int i = -range; i <= range; i++)
		{
			for(int j = Math.min(0, -i) + range; j >= Math.max(0, -i) - range; j--)
			{
				draw1(layout, mid.add(new Hex(j, i)));
			}
		}
		drawArrows1(layout, mid, range);
	}

	@SuppressWarnings("ConstantConditions")
	public void draw0(HexLayout layout, Hex h1)
	{
		FullTile fullTile = levelMap.tile(h1);
		double[][] points = layout.hexCorners(h1);
		if(fullTile.visible())
		{
			gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][3], points[1][3],
					false, null, fullTile.marked ? fullTile.floorTile.type.marked : fullTile.floorTile.type.normal));
		}
		else
		{
			gd.setFill(Color.BLACK);
		}
		gd.fillPolygon(points[0], points[1], 6);
		if(fullTile.visible())
		{
			if(fullTile.building != null)
			{
				PointD midPoint = layout.hexToPixel(h1);
				gd.drawImage(MBuilding.IMAGE, midPoint.v[0] - layout.size.v[0], midPoint.v[1] - layout.size.v[1],
						layout.size.v[0] * 2, layout.size.v[1] * 2);
			}
		}
	}

	public void drawArrows0(HexLayout layout, Hex mid, int range)
	{
		for(MArrow arrow : levelMap.getArrows())
		{
			if(arrow.isVisible(mid, range))
			{
				if(arrow.showArrow())
				{
					if(arrow.showShine())
					{
						double[][] gradientPoints = layout.multiHexToPixel(arrow.visualStart(), arrow.visualEnd());
						double[] shine = arrow.getShine();
						Stop[] stops = new Stop[shine.length];
						for(int i = 0; i < shine.length; i++)
						{
							stops[i] = new Stop(shine[i], i % 3 == 1 ? Color.AZURE : Color.BLUE);
						}
						gd.setFill(new LinearGradient(gradientPoints[0][0], gradientPoints[1][0],
								gradientPoints[0][1], gradientPoints[1][1], false, null, stops));
					}
					else
					{
						gd.setFill(Color.BLUE);
					}
					double[][] points = layout.multiHexToPixel(arrow.getArrowPoints());
					gd.fillPolygon(points[0], points[1], points[0].length);
				}
			}
		}
	}

	public void draw1(HexLayout layout, Hex h1)
	{
		FullTile fullTile = levelMap.tile(h1);
		if(fullTile.visible())
		{
			if(fullTile.entity != null && fullTile.entity.isVisible())
			{
				PointD midPoint = layout.hexToPixel(h1);
				/*gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][3], points[1][3], false, null,
						new Stop(0, Color.AZURE), new Stop(1, Color.BLACK)));*/
				gd.drawImage(fullTile.entity.getImage(), midPoint.v[0] - layout.size.v[0], midPoint.v[1] - layout.size.v[1],
						layout.size.v[0] * 2, layout.size.v[1] * 2);
			}
		}
	}

	public void drawArrows1(HexLayout layout, Hex mid, int range)
	{
		for(MArrow arrow : levelMap.getArrows())
		{
			if(arrow.isVisible(mid, range))
			{
				if(arrow.showTransport())
				{
					PointD midPoint = layout.hexToPixel(arrow.currentTLocation());
					gd.drawImage(arrow.transported(), midPoint.v[0] - layout.size.v[0], midPoint.v[1] - layout.size.v[1],
							layout.size.v[0] * 2, layout.size.v[1] * 2);
				}
			}
		}
	}
}