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
	private final TileType y1;
	private final VisualXArrow av;
	private LevelMap levelMap;
	private List<VisMark> visMarked;
	private GraphicsContext gd;

	public VisualTile(TileType y1, VisualXArrow av, LevelMap levelMap, List<VisMark> visMarked, GraphicsContext gd)
	{
		this.y1 = y1;
		this.av = av;
		this.levelMap = levelMap;
		this.visMarked = visMarked;
		this.gd = gd;
	}

	public void draw(TileCamera camera, int screenshake, Scheme scheme)
	{
		gd.setImageSmoothing(true);
		int range = camera.getRange();
		TileLayout layout = camera.layout(screenshake);
		Tile mid = camera.mid(layout);
		for(int i = -range; i <= range; i++)
		{
			int i1 = i * camera.startMultiplier();
			for(int j = Math.min(0, i1) + range; j >= Math.max(0, i1) - range; j--)
			{
				draw0(layout, y1.add(mid, y1.create2(j, i)), scheme);
			}
		}
		drawArrows0(layout, mid, range, scheme);
		drawMarked0(layout, scheme);
		for(int i = -range; i <= range; i++)
		{
			int i1 = i * camera.startMultiplier();
			for(int j = Math.min(0, i1) + range; j >= Math.max(0, i1) - range; j--)
			{
				draw1(layout, y1.add(mid, y1.create2(j, i)), scheme);
			}
		}
		drawArrows1(layout, mid, range, scheme);
	}

	private void draw0(TileLayout layout, Tile t1, Scheme scheme)
	{
		AdvTile advTile = levelMap.advTile(t1);
		double[][] points = layout.tileCorners(t1);
		PointD mid = layout.tileToPixel(t1);
		PointD offset = layout.imageOffset();
		Image image = scheme.image(advTile.visible() ? advTile.floorTile().type.image : "floortile.wall");
		gd.setFill(new ImagePattern(image, mid.v()[0] - offset.v()[0],
				mid.v()[1] - offset.v()[1], offset.v()[0] * 2, offset.v()[1] * 2, false));
		gd.fillPolygon(points[0], points[1], y1.directionCount());
		if(advTile.visible())
		{
			if(advTile.building() != null)
			{
				PointD midPoint = layout.tileToPixel(t1);
				gd.drawImage(scheme.image("building.default"), midPoint.v()[0] - layout.size().v()[0], midPoint.v()[1] - layout.size().v()[1],
						layout.size().v()[0] * 2, layout.size().v()[1] * 2);
			}
		}
	}

	private void drawArrows0(TileLayout layout, Tile mid, int range, Scheme scheme)
	{
		levelMap.getArrows().stream().filter(arrow -> arrow instanceof ShineArrow && av.isVisible(arrow, mid, range)).forEach(arrow ->
				{
					ShineArrow arrow1 = (ShineArrow) arrow;
					gd.setFill(av.shineFill(arrow1, layout, scheme));
					double[][] points = layout.polygonCorners(av.arrowPoints(arrow1));
					gd.fillPolygon(points[0], points[1], points[0].length);
				});
	}

	private void drawMarked0(TileLayout layout, Scheme scheme)
	{
		for(VisMark vm : visMarked)
		{
			double[][] points = layout.tileCorners(vm.location(), vm.midDistance());
			gd.setStroke(scheme.color(vm.color()));
			gd.strokePolygon(points[0], points[1], y1.directionCount());
		}
	}

	private void draw1(TileLayout layout, Tile t1, Scheme scheme)
	{
		AdvTile advTile = levelMap.advTile(t1);
		if(advTile.visible())
		{
			if(advTile.entity() != null && advTile.entity().isVisible())
			{
				PointD midPoint = layout.tileToPixel(t1);
				gd.drawImage(scheme.image(advTile.entity().mapImageName()),
						midPoint.v()[0] - layout.size().v()[0], midPoint.v()[1] - layout.size().v()[1],
						layout.size().v()[0] * 2, layout.size().v()[1] * 2);
			}
		}
	}

	private void drawArrows1(TileLayout layout, Tile mid, int range, Scheme scheme)
	{
		levelMap.getArrows().stream().filter(arrow -> arrow.imageName() != null && av.isVisible(arrow, mid, range)).forEach(arrow ->
				{
					PointD midPoint = layout.tileToPixel(av.imageLocation(arrow));
					gd.drawImage(scheme.image(arrow.imageName()), midPoint.v()[0] - layout.size().v()[0], midPoint.v()[1] - layout.size().v()[1],
							layout.size().v()[0] * 2, layout.size().v()[1] * 2);
				});
		levelMap.getArrows().stream().filter(arrow -> arrow instanceof InfoArrow && av.isVisible(arrow, mid, range)).forEach(arrow ->
		{
			StatBar statBar = ((InfoArrow) arrow).statBar();
			PointD midPoint = layout.tileToPixel(av.dataLocation((InfoArrow) arrow, layout));
			double xw = layout.size().v()[0] * VisualXArrow.DATA_WIDTH;
			double yw = layout.size().v()[1] * VisualXArrow.DATA_HEIGHT;
			double xs = midPoint.v()[0] - xw / 2;
			double ys = midPoint.v()[1] - yw / 2;
			gd.setFill(scheme.color(statBar.getBg()));
			gd.fillRect(xs, ys, xw, yw);
			gd.setFill(scheme.color(statBar.getFg()));
			gd.fillRect(xs, ys, xw * statBar.filledPart(), yw);
			gd.setStroke(scheme.color(statBar.getBg()));
			gd.strokeRect(xs, ys, xw, yw);
			gd.setFont(new Font(yw * 0.8));
			gd.setFill(scheme.color(statBar.getTc()));
			gd.fillText(statBar.getText(), xs + xw / 2, ys + yw / 2, xw);
		});
	}
}