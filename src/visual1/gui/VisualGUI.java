package visual1.gui;

import geom.*;
import geom.d1.*;
import geom.f1.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.gui.*;
import visual1.*;

public abstract class VisualGUI
{
	private static final int FADEIN = 6;
	private static final int FADEOUT = 3;

	public final DoubleType y2;
	protected final XGraphics graphics;
	protected final TileCamera camera;
	private XGUIState last;
	private XGUIState last2;
	private int counter;

	public VisualGUI(XGraphics graphics, TileCamera camera)
	{
		this.graphics = graphics;
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

	public void zoomAndDraw(XGUIState xgui, Scheme scheme)
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
			locateAndDraw(last2, scheme);
		}
		if(xgui != null)
		{
			camera.setZoom((double) counter / FADEIN);
			locateAndDraw(xgui, scheme);
		}
		camera.setZoom(1);
	}

	public abstract void locateAndDraw(XGUIState xgui, Scheme scheme);

	public void drawGUI(XGUIState xgui, Scheme scheme, double cxs, double cys, DoubleTile lu, DoubleTile rl, double imgSize, double fontSize, double textWidth)
	{
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return;
		camera.setXShift(cxs);
		camera.setYShift(cys);
		TileLayout layout = camera.layout(0);
		GraphicsContext gd = graphics.gd();
		gd.setImageSmoothing(true);
		Color background = scheme.color("gui.background");
		Color hover = scheme.color("gui.background.hover");
		Color text = scheme.color("gui.text");
		Color outline = scheme.color("gui.text.outline");
		gd.setFill(background);
		gd.setStroke(background);
		PointD p0 = layout.tileToPixel(lu);
		PointD p1 = layout.tileToPixel(rl);
		gd.fillRect(p0.v()[0], p0.v()[1], p1.v()[0] - p0.v()[0], p1.v()[1] - p0.v()[1]);
		gd.strokeRect(p0.v()[0], p0.v()[1], p1.v()[0] - p0.v()[0], p1.v()[1] - p0.v()[1]);
		GuiTile[][] guiTiles = xgui.tiles;
		for(int ix = 0; ix < xgui.xw(); ix++)
		{
			for(int iy = 0; iy < xgui.yw(); iy++)
			{
				drawElement(layout, y2.fromOffset(ix, iy), guiTiles[ix][iy], xgui.getTargeted().contains(ix, iy),
						hover, text, outline, imgSize, fontSize, textWidth, scheme);
			}
		}
	}

	public void drawElement(TileLayout layout, Tile t1, GuiTile guiTile, boolean targeted, Color hover, Color text, Color outline,
			double imgSize, double fontSize, double textWidth, Scheme scheme)
	{
		GraphicsContext gd = graphics.gd();
		if(guiTile.color != null)
		{
			double[][] points = layout.tileCorners(t1);
			Color color = scheme.color(guiTile.color);
			gd.setFill(targeted ? color.brighter() : color);
			gd.setStroke(targeted ? color.brighter() : color);
			gd.fillPolygon(points[0], points[1], y2.directionCount());
			gd.strokePolygon(points[0], points[1], y2.directionCount());
		}
		else if(targeted)
		{
			double[][] points = layout.tileCorners(t1);
			gd.setFill(hover);
			gd.setStroke(hover);
			gd.fillPolygon(points[0], points[1], y2.directionCount());
			gd.strokePolygon(points[0], points[1], y2.directionCount());
		}
		if(guiTile.imageName != null)
		{
			PointD midPoint = layout.tileToPixel(t1);
			Image image = scheme.image(guiTile.imageName);
			if(guiTile.flipped)
			{
				double width = image.widthProperty().get();
				double height = image.heightProperty().get();
				gd.drawImage(image, width, 0, -width, height,
						midPoint.v()[0] - layout.size().v()[0] * imgSize,
						midPoint.v()[1] - layout.size().v()[1] * imgSize,
						layout.size().v()[0] * 2 * imgSize, layout.size().v()[1] * 2 * imgSize);
			}
			else
			{
				gd.drawImage(image, midPoint.v()[0] - layout.size().v()[0] * imgSize,
						midPoint.v()[1] - layout.size().v()[1] * imgSize,
						layout.size().v()[0] * 2 * imgSize, layout.size().v()[1] * 2 * imgSize);
			}
		}
		if(guiTile.text != null)
		{
			PointD midPoint = layout.tileToPixel(y2.add(y2.fromTile(t1), y2.fromOffsetD(guiTile.l * -0.5, guiTile.u * -0.5)));
			PointD rEnd = layout.tileToPixel(t1);
			double ld = Math.max(0.5, guiTile.text.chars().filter(e -> e == '\n').count()) + 0.5;
			gd.setFont(new Font(layout.size().v()[1] * fontSize / ld));
			if(guiTile.imageName != null)
			{
				gd.setStroke(outline);
				gd.strokeText(guiTile.text, midPoint.v()[0], midPoint.v()[1], rEnd.v()[0] - midPoint.v()[0] + layout.size().v()[0] * textWidth);
			}
			gd.setFill(text);
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