package draw;

import arrow.*;
import geom.*;
import geom.d1.*;
import geom.f1.*;
import java.util.stream.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import levelMap.*;

public class VisualTile
{
	private final TileType y1;
	private LevelMap levelMap;
	private GraphicsContext gd;

	public VisualTile(TileType y1, LevelMap levelMap, GraphicsContext gd)
	{
		this.y1 = y1;
		this.levelMap = levelMap;
		this.gd = gd;
	}

	public void draw(TileCamera camera)
	{
		int range = camera.getRange();
		TileLayout layout = camera.layout();
		Tile mid = camera.mid(layout);
		for(int i = -range; i <= range; i++)
		{
			int i1 = camera instanceof HexCamera ? i : 0;
			for(int j = Math.min(0, -i1) + range; j >= Math.max(0, -i1) - range; j--)
			{
				draw0(layout, y1.add(mid, y1.create2(j, i)));
			}
		}
		drawArrows0(layout, mid, range);
		for(int i = -range; i <= range; i++)
		{
			int i1 = camera instanceof HexCamera ? i : 0;
			for(int j = Math.min(0, -i1) + range; j >= Math.max(0, -i1) - range; j--)
			{
				draw1(layout, y1.add(mid, y1.create2(j, i)));
			}
		}
		drawArrows1(layout, mid, range);
	}

	public void draw0(TileLayout layout, Tile t1)
	{
		AdvTile advTile = levelMap.advTile(t1);
		double[][] points = layout.tileCorners(t1);
		if(advTile.visible())
		{
			gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][y1.directionCount() / 2], points[1][y1.directionCount() / 2],
					false, null, advTile.getFloorTile().type.normal.stream()
					.map(e -> new Stop(e.getOffset(), mark(e.getColor(), levelMap.getMarked().getOrDefault(t1, MarkType.NOT)))).collect(Collectors.toList())));
		}
		else
		{
			gd.setFill(Color.BLACK);
		}
		gd.fillPolygon(points[0], points[1], y1.directionCount());
		if(advTile.visible())
		{
			if(advTile.getBuilding() != null)
			{
				PointD midPoint = layout.tileToPixel(t1);
				gd.drawImage(MBuilding.IMAGE, midPoint.v[0] - layout.size().v[0], midPoint.v[1] - layout.size().v[1],
						layout.size().v[0] * 2, layout.size().v[1] * 2);
			}
		}
	}

	public Color mark(Color color, MarkType markType)
	{
		return switch(markType)
				{
					case NOT -> color;
					case TARGET, OFF -> color.darker();
					case ON -> color.interpolate(Color.YELLOW, 0.5);
					case BLOCKED -> color.interpolate(Color.RED, 0.5);
				};
	}

	public void drawArrows0(TileLayout layout, Tile mid, int range)
	{
		for(VisualArrow arrow : levelMap.getArrows())
		{
			if(arrow.isVisible(mid, range))
			{
				if(arrow.showArrow())
				{
					if(arrow.showShine())
					{
						double[][] gradientPoints = layout.polygonCorners(arrow.visualStart(), arrow.visualEnd());
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
					double[][] points = layout.polygonCorners(arrow.getArrowPoints());
					gd.fillPolygon(points[0], points[1], points[0].length);
				}
			}
		}
	}

	public void draw1(TileLayout layout, Tile t1)
	{
		AdvTile advTile = levelMap.advTile(t1);
		if(advTile.visible())
		{
			if(advTile.getEntity() != null && advTile.getEntity().isVisible())
			{
				PointD midPoint = layout.tileToPixel(t1);
				/*gd.setFill(new LinearGradient(points[0][0], points[1][0], points[0][3], points[1][3], false, null,
						new Stop(0, Color.AZURE), new Stop(1, Color.BLACK)));*/
				gd.drawImage(advTile.getEntity().getImage(), midPoint.v[0] - layout.size().v[0], midPoint.v[1] - layout.size().v[1],
						layout.size().v[0] * 2, layout.size().v[1] * 2);
			}
		}
	}

	public void drawArrows1(TileLayout layout, Tile mid, int range)
	{
		for(VisualArrow arrow : levelMap.getArrows())
		{
			if(arrow.isVisible(mid, range))
			{
				if(arrow.showTransport())
				{
					PointD midPoint = layout.tileToPixel(arrow.currentTLocation());
					gd.drawImage(arrow.transported(), midPoint.v[0] - layout.size().v[0], midPoint.v[1] - layout.size().v[1],
							layout.size().v[0] * 2, layout.size().v[1] * 2);
				}
			}
		}
	}
}