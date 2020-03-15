package visual1;

import arrow.*;
import geom.*;
import geom.d1.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import levelMap.*;

public class VisualTile
{
	private static final Image BUILDING = new Image("H.png");

	private final TileType y1;
	private final ArrowViewer av;
	private LevelMap levelMap;
	private List<VisMark> visMarked;
	private GraphicsContext gd;

	public VisualTile(TileType y1, ArrowViewer av, LevelMap levelMap, List<VisMark> visMarked, GraphicsContext gd)
	{
		this.y1 = y1;
		this.av = av;
		this.levelMap = levelMap;
		this.visMarked = visMarked;
		this.gd = gd;
	}

	public void draw(TileCamera camera, int screenshake)
	{
		int range = camera.getRange();
		TileLayout layout = camera.layout(screenshake);
		Tile mid = camera.mid(layout);
		for(int i = -range; i <= range; i++)
		{
			int i1 = i * camera.startMultiplier();
			for(int j = Math.min(0, i1) + range; j >= Math.max(0, i1) - range; j--)
			{
				draw0(layout, y1.add(mid, y1.create2(j, i)));
			}
		}
		drawArrows0(layout, mid, range);
		drawMarked0(layout);
		for(int i = -range; i <= range; i++)
		{
			int i1 = i * camera.startMultiplier();
			for(int j = Math.min(0, i1) + range; j >= Math.max(0, i1) - range; j--)
			{
				draw1(layout, y1.add(mid, y1.create2(j, i)));
			}
		}
		drawArrows1(layout, mid, range);
	}

	private void draw0(TileLayout layout, Tile t1)
	{
		AdvTile advTile = levelMap.advTile(t1);
		double[][] points = layout.tileCorners(t1);
		if(advTile.visible())
		{
			PointD mid = layout.tileToPixel(t1);
			PointD offset = layout.imageOffset();
			gd.setFill(new ImagePattern(advTile.floorTile().type.image,
					mid.v()[0] - offset.v()[0], mid.v()[1] - offset.v()[1], offset.v()[0] * 2, offset.v()[1] * 2, false));
		}
		else
		{
			gd.setFill(Color.BLACK);
		}
		gd.fillPolygon(points[0], points[1], y1.directionCount());
		if(advTile.visible())
		{
			if(advTile.building() != null)
			{
				PointD midPoint = layout.tileToPixel(t1);
				gd.drawImage(BUILDING, midPoint.v()[0] - layout.size().v()[0], midPoint.v()[1] - layout.size().v()[1],
						layout.size().v()[0] * 2, layout.size().v()[1] * 2);
			}
		}
	}

	private void drawArrows0(TileLayout layout, Tile mid, int range)
	{
		levelMap.getArrows().stream().filter(arrow -> arrow instanceof ShineArrow && av.isVisible(arrow, mid, range)).forEach(arrow ->
				{
					ShineArrow arrow1 = (ShineArrow) arrow;
					gd.setFill(av.shineFill(arrow1, layout));
					double[][] points = layout.polygonCorners(av.arrowPoints(arrow1));
					gd.fillPolygon(points[0], points[1], points[0].length);
				});
	}

	private void drawMarked0(TileLayout layout)
	{
		for(VisMark vm : visMarked)
		{
			double[][] points = layout.tileCorners(vm.location(), vm.midDistance());
			gd.setStroke(vm.color());
			gd.strokePolygon(points[0], points[1], y1.directionCount());
		}
	}

	private void draw1(TileLayout layout, Tile t1)
	{
		AdvTile advTile = levelMap.advTile(t1);
		if(advTile.visible())
		{
			if(advTile.entity() != null && advTile.entity().isVisible())
			{
				PointD midPoint = layout.tileToPixel(t1);
				gd.drawImage(advTile.entity().mapImage(),
						midPoint.v()[0] - layout.size().v()[0], midPoint.v()[1] - layout.size().v()[1],
						layout.size().v()[0] * 2, layout.size().v()[1] * 2);
			}
		}
	}

	private void drawArrows1(TileLayout layout, Tile mid, int range)
	{
		levelMap.getArrows().stream().filter(arrow -> arrow.image() != null && av.isVisible(arrow, mid, range)).forEach(arrow ->
				{
					PointD midPoint = layout.tileToPixel(av.imageLocation(arrow));
					gd.drawImage(arrow.image(), midPoint.v()[0] - layout.size().v()[0], midPoint.v()[1] - layout.size().v()[1],
							layout.size().v()[0] * 2, layout.size().v()[1] * 2);
				});
		levelMap.getArrows().stream().filter(arrow -> arrow instanceof InfoArrow && av.isVisible(arrow, mid, range)).forEach(arrow ->
		{
			StatBar statBar = ((InfoArrow) arrow).statBar();
			PointD midPoint = layout.tileToPixel(av.dataLocation((InfoArrow) arrow, layout));
			double xw = layout.size().v()[0] * ArrowViewer.DATA_WIDTH;
			double yw = layout.size().v()[1] * ArrowViewer.DATA_HEIGHT;
			double xs = midPoint.v()[0] - xw / 2;
			double ys = midPoint.v()[1] - yw / 2;
			gd.setFill(statBar.getBg());
			gd.fillRect(xs, ys, xw, yw);
			gd.setFill(statBar.getFg());
			gd.fillRect(xs, ys, xw * statBar.getData() / statBar.getMaxData(), yw);
			gd.setStroke(statBar.getBg());
			gd.strokeRect(xs, ys, xw, yw);
			gd.setFont(new Font(yw * 0.8));
			gd.setFill(statBar.getTc());
			gd.fillText(statBar.getData() + "/" + statBar.getMaxData(), xs + xw / 2, ys + yw / 2, xw);
		});
	}
}