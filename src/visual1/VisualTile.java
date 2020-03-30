package visual1;

import arrow.*;
import building.adv.*;
import doubleinv.*;
import entity.*;
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
	private final VisualXArrow av;
	private final XGraphics graphics;

	public VisualTile(VisualXArrow av, XGraphics graphics)
	{
		this.av = av;
		this.graphics = graphics;
	}

	public void draw(TileCamera camera, LevelMap levelMap, List<VisMark> visMarked, int screenshake, Scheme scheme)
	{
		graphics.gd().setImageSmoothing(true);
		TileType y1 = camera.getDoubleType();
		int range = camera.getRange();
		TileLayout layout = camera.layout(screenshake);
		Tile mid = camera.mid(layout);
		for(int i = -range; i <= range; i++)
		{
			int i1 = i * camera.startMultiplier();
			for(int j = Math.min(0, i1) + range; j >= Math.max(0, i1) - range; j--)
			{
				Tile loc = y1.add(mid, y1.create2(j, i));
				AdvTile advTile = levelMap.advTile(loc);
				if(advTile.visible())
				{
					drawFloorTile(y1, layout, loc, advTile.floorTile(), scheme);
					XBuilding building = advTile.building();
					if(building != null && building.active())
						drawBuilding(layout, loc, building, scheme);
				}
				else
				{
					drawFloorTile(y1, layout, loc, null, scheme);
				}
			}
		}
		levelMap.getArrows().stream().filter(arrow -> arrow instanceof ShineArrow && av.isVisible(arrow, mid, range))
				.forEach(arrow -> drawShineArrow(layout, (ShineArrow) arrow, scheme));
		for(VisMark visMark : visMarked)
		{
			drawVisMark(y1, layout, visMark, scheme);
		}
		for(int i = -range; i <= range; i++)
		{
			int i1 = i * camera.startMultiplier();
			for(int j = Math.min(0, i1) + range; j >= Math.max(0, i1) - range; j--)
			{
				Tile loc = y1.add(mid, y1.create2(j, i));
				AdvTile advTile = levelMap.advTile(loc);
				if(advTile.visible())
				{
					XCharacter character = advTile.entity();
					if(character != null && character.isVisible())
						drawCharacter(layout, loc, character, scheme);
				}
			}
		}
		levelMap.getArrows().stream().filter(arrow -> arrow.imageName() != null && av.isVisible(arrow, mid, range))
				.forEach(arrow -> drawCharacterArrow(layout, arrow, scheme));
		levelMap.getArrows().stream().filter(arrow -> arrow instanceof InfoArrow && av.isVisible(arrow, mid, range))
				.forEach(arrow -> drawInfoArrow(layout, (InfoArrow) arrow, scheme));
	}

	private void drawFloorTile(TileType y1, TileLayout layout, Tile loc, FloorTile floorTile, Scheme scheme)
	{
		double[][] points = layout.tileCorners(loc);
		PointD mid = layout.tileToPixel(loc);
		PointD offset = layout.imageOffset();
		Image image = scheme.image(floorTile != null ? floorTile.type.image : "floortile.wall");
		GraphicsContext gd = graphics.gd();
		gd.setFill(new ImagePattern(image, mid.v()[0] - offset.v()[0],
				mid.v()[1] - offset.v()[1], offset.v()[0] * 2, offset.v()[1] * 2, false));
		gd.fillPolygon(points[0], points[1], y1.directionCount());
	}

	private void drawBuilding(TileLayout layout, Tile loc, XBuilding building, Scheme scheme)
	{
		PointD midPoint = layout.tileToPixel(loc);
		graphics.gd().drawImage(scheme.image("building.default"), midPoint.v()[0] - layout.size().v()[0], midPoint.v()[1] - layout.size().v()[1],
				layout.size().v()[0] * 2, layout.size().v()[1] * 2);
	}

	private void drawShineArrow(TileLayout layout, ShineArrow shineArrow, Scheme scheme)
	{
		GraphicsContext gd = graphics.gd();
		gd.setFill(av.shineFill(shineArrow, layout, scheme));
		double[][] points = layout.polygonCorners(av.arrowPoints(shineArrow));
		gd.fillPolygon(points[0], points[1], points[0].length);
	}

	private void drawVisMark(TileType y1, TileLayout layout, VisMark visMark, Scheme scheme)
	{
		double[][] points = layout.tileCorners(visMark.location(), visMark.midDistance());
		graphics.gd().setStroke(scheme.color(visMark.color()));
		graphics.gd().strokePolygon(points[0], points[1], y1.directionCount());
	}

	private void drawCharacter(TileLayout layout, Tile loc, XCharacter character, Scheme scheme)
	{
		PointD midPoint = layout.tileToPixel(loc);
		graphics.gd().drawImage(scheme.image(character.mapImageName()),
				midPoint.v()[0] - layout.size().v()[0], midPoint.v()[1] - layout.size().v()[1],
				layout.size().v()[0] * 2, layout.size().v()[1] * 2);
	}

	private void drawCharacterArrow(TileLayout layout, XArrow arrow, Scheme scheme)
	{
		PointD midPoint = layout.tileToPixel(av.imageLocation(arrow));
		graphics.gd().drawImage(scheme.image(arrow.imageName()), midPoint.v()[0] - layout.size().v()[0], midPoint.v()[1] - layout.size().v()[1],
				layout.size().v()[0] * 2, layout.size().v()[1] * 2);
	}

	private void drawInfoArrow(TileLayout layout, InfoArrow arrow, Scheme scheme)
	{
		StatBar statBar = arrow.statBar();
		PointD midPoint = layout.tileToPixel(av.dataLocation(arrow, layout));
		double xw = layout.size().v()[0] * VisualXArrow.DATA_WIDTH;
		double yw = layout.size().v()[1] * VisualXArrow.DATA_HEIGHT;
		double xs = midPoint.v()[0] - xw / 2;
		double ys = midPoint.v()[1] - yw / 2;
		GraphicsContext gd = graphics.gd();
		gd.setFill(scheme.color(statBar.getBg()));
		gd.fillRect(xs, ys, xw, yw);
		gd.setFill(scheme.color(statBar.getFg()));
		gd.fillRect(xs, ys, xw * statBar.filledPart(), yw);
		gd.setStroke(scheme.color(statBar.getBg()));
		gd.strokeRect(xs, ys, xw, yw);
		gd.setFont(new Font(yw * 0.8));
		gd.setFill(scheme.color(statBar.getTc()));
		gd.fillText(statBar.getText(), xs + xw / 2, ys + yw / 2, xw);
	}
}