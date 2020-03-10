package visual.gui;

import geom.*;
import geom.d1.*;
import geom.f1.*;
import logic.gui.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import visual.*;

public abstract class VisualGUI
{
	private static final int FADEIN = 6;
	private static final int FADEOUT = 3;

	public final DoubleType y2;
	protected final GraphicsContext gd;
	protected final TileCamera camera;
	private XGUIState last;
	private XGUIState last2;
	private int counter;

	public VisualGUI(GraphicsContext gd, TileCamera camera)
	{
		this.gd = gd;
		this.camera = camera;
		y2 = camera.getDoubleType();
	}

	public Tile offsetClickLocation(double x, double y)
	{
		return y2.toOffset(y2.cast(camera.clickLocation(x, y)));
	}

	public boolean inside(double x, double y, XGUIState xgui)
	{
		return xgui != null && inside(camera.clickLocation(x, y), xgui);
	}

	public abstract boolean inside(DoubleTile h1, XGUIState xgui);

	public void draw2(XGUIState xgui)
	{
		if(xgui != last)
		{
			last2 = last;
			last = xgui;
			counter = 0;
		}
		if(counter < FADEIN)
			counter++;
		if(counter < FADEOUT && last2 != null)
		{
			camera.setZoom((double) (FADEOUT - counter) / FADEOUT);
			draw(last2);
		}
		if(xgui != null)
		{
			camera.setZoom((double) counter / FADEIN);
			draw(xgui);
		}
		camera.setZoom(1);
	}

	public abstract void draw(XGUIState xgui);

	public void draw1(XGUIState xgui, double cxs, double cys, DoubleTile lu, DoubleTile rl, double imgSize, double fontSize, double textWidth)
	{
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return;
		camera.setXShift(cxs);
		camera.setYShift(cys);
		TileLayout layout = camera.layout(0);
		gd.setFill(xgui.background());
		gd.setStroke(xgui.background());
		Color bg2 = xgui.background().brighter();
		PointD p0 = layout.tileToPixel(lu);
		PointD p1 = layout.tileToPixel(rl);
		gd.fillRect(p0.v()[0], p0.v()[1], p1.v()[0] - p0.v()[0], p1.v()[1] - p0.v()[1]);
		gd.strokeRect(p0.v()[0], p0.v()[1], p1.v()[0] - p0.v()[0], p1.v()[1] - p0.v()[1]);
		GuiTile[][] guiTiles = xgui.tiles;
		for(int ix = 0; ix < xgui.xw(); ix++)
		{
			for(int iy = 0; iy < xgui.yw(); iy++)
			{
				drawElement(layout, y2.fromOffset(ix, iy), guiTiles[ix][iy], xgui.getTargeted().contains(ix, iy), bg2, imgSize, fontSize, textWidth);
			}
		}
	}

	public void drawElement(TileLayout layout, Tile t1, GuiTile guiTile, boolean targeted, Color bg2, double imgSize, double fontSize, double textWidth)
	{
		if(guiTile.color != null)
		{
			double[][] points = layout.tileCorners(t1);
			gd.setFill(targeted ? guiTile.color.brighter() : guiTile.color);
			gd.setStroke(targeted ? guiTile.color.brighter() : guiTile.color);
			gd.fillPolygon(points[0], points[1], y2.directionCount());
			gd.strokePolygon(points[0], points[1], y2.directionCount());
		}
		else if(targeted)
		{
			double[][] points = layout.tileCorners(t1);
			gd.setFill(bg2);
			gd.setStroke(bg2);
			gd.fillPolygon(points[0], points[1], y2.directionCount());
			gd.strokePolygon(points[0], points[1], y2.directionCount());
		}
		if(guiTile.image != null)
		{
			PointD midPoint = layout.tileToPixel(t1);
			if(guiTile.flipped)
				gd.drawImage(guiTile.image, guiTile.image.widthProperty().get(), 0,
						-guiTile.image.widthProperty().get(), guiTile.image.heightProperty().get(),
						midPoint.v()[0] - layout.size().v()[0] * imgSize, midPoint.v()[1] - layout.size().v()[1] * imgSize,
						layout.size().v()[0] * 2 * imgSize, layout.size().v()[1] * 2 * imgSize);
			else
				gd.drawImage(guiTile.image, midPoint.v()[0] - layout.size().v()[0] * imgSize,
						midPoint.v()[1] - layout.size().v()[1] * imgSize,
						layout.size().v()[0] * 2 * imgSize, layout.size().v()[1] * 2 * imgSize);
		}
		if(guiTile.text != null)
		{
			PointD midPoint = layout.tileToPixel(y2.add(y2.fromTile(t1), y2.fromOffsetD(guiTile.l * -0.5, guiTile.u * -0.5)));
			PointD rEnd = layout.tileToPixel(t1);
			double ld = Math.max(0.5, guiTile.text.chars().filter(e -> e == '\n').count()) + 0.5;
			gd.setFont(new Font(layout.size().v()[1] * fontSize / ld));
			if(guiTile.image != null)
			{
				gd.setStroke(Color.WHITE);
				gd.strokeText(guiTile.text, midPoint.v()[0], midPoint.v()[1], rEnd.v()[0] - midPoint.v()[0] + layout.size().v()[0] * textWidth);
			}
			gd.setFill(Color.BLACK);
			gd.fillText(guiTile.text, midPoint.v()[0], midPoint.v()[1], rEnd.v()[0] - midPoint.v()[0] + layout.size().v()[0] * textWidth);
		}
	}

	public static VisualGUI forCamera(XGraphics graphics, TileCamera camera)
	{
		if(camera instanceof HexCamera)
			return new VisualGUIHex(graphics, (HexCamera) camera);
		else if(camera instanceof QuadCamera)
			return new VisualGUIQuad(graphics, (QuadCamera) camera);
		else
			throw new RuntimeException();
	}
}