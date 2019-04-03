package arrow;

import geom.d1.*;
import geom.f1.*;
import javafx.scene.image.*;

public class VisualArrow
{
	private static final double SHINE_WIDTH = 0.65;
	private static final double ZERO_SHINE_MULTIPLIER = 1.5;

	public final TileType y1;
	public final DoubleType y2;
	private final Tile start;
	private final Tile end;
	private final ArrowMode arrowMode;
	private final int timerEnd;
	private final Image image;
	private int timer;
	private boolean finished;
	private DoubleTile[] arrowPoints;
	private final boolean zero;
	private final double approxDistance;

	public VisualArrow(TileType y1, DoubleType y2, Tile start, Tile end, ArrowMode arrowMode, int timerEnd, Image image)
	{
		this.y1 = y1;
		this.y2 = y2;
		this.start = start;
		this.end = end;
		this.arrowMode = arrowMode;
		this.timerEnd = timerEnd;
		this.image = image;
		zero = end.equals(start);
		if(showArrow())
			calculateArrowPoints();
		if(zero)
			approxDistance = ZERO_SHINE_MULTIPLIER;
		else
			approxDistance = y1.distance(start, end);
	}

	public boolean isVisible(Tile mid, int range)
	{
		return y1.distance(start, mid) <= range && y1.distance(end, mid) <= range;
	}

	public boolean showArrow()
	{
		return arrowMode.showArrow && (arrowMode.showZero || !zero);
	}

	public boolean showShine()
	{
		return arrowMode.showShine;
	}

	public boolean showTransport()
	{
		return arrowMode.showTransport;
	}

	public boolean tick()
	{
		timer++;
		if(timer >= timerEnd)
		{
			finished = true;
			timer = 0;
		}
		return finished();
	}

	public DoubleTile[] getArrowPoints()
	{
		return arrowPoints;
	}

	public DoubleTile visualStart()
	{
		return y2.fromTile(start);
	}

	public DoubleTile visualEnd()
	{
		if(zero)
			return y2.add(y2.fromTile(end), normalUp());
		else
			return y2.fromTile(end);
	}

	private DoubleTile normal()
	{
		return y2.normalize(y1.subtract(end, start));
	}

	private DoubleTile normalUp()
	{
		return y2.normalize(y1.create3(1, 1, -2));
	}

	public DoubleTile currentTLocation()
	{
		return y2.tileLerp(visualStart(), visualEnd(), timer / (double) timerEnd);
	}

	public Image transported()
	{
		return image;
	}

	private void calculateArrowPoints()
	{
		DoubleTile normal = zero ? normalUp() : normal();
		DoubleTile sideNormalX2 = y2.subtract(y2.rotate(normal, false), y2.rotate(normal, false));
		DoubleTile side0 = y2.multiply(sideNormalX2, 0.15);
		DoubleTile side1 = y2.multiply(sideNormalX2, 0.25);
		DoubleTile end0 = visualEnd();
		DoubleTile end1 = y2.subtract(end0, y2.multiply(normal, 0.5));
		arrowPoints = new DoubleTile[7];
		arrowPoints[0] = y2.add(visualStart(), side0);
		arrowPoints[6] = y2.subtract(visualStart(), side0);
		arrowPoints[1] = y2.add(end1, side0);
		arrowPoints[5] = y2.subtract(end1, side0);
		arrowPoints[2] = y2.add(end1, side1);
		arrowPoints[4] = y2.subtract(end1, side1);
		arrowPoints[3] = end0;
	}

	public double[] getShine()
	{
		double dM = timer / (double) timerEnd;
		double dL = dM - SHINE_WIDTH / approxDistance;
		double dH = dM + SHINE_WIDTH / approxDistance;
		if(arrowMode.loop)
		{
			if(dM < 0.5)
			{
				return new double[]{dL, dM, dH, dL + 1, dM + 1, dH + 1};
			}
			else
			{
				return new double[]{dL - 1, dM - 1, dH - 1, dL, dM, dH};
			}
		}
		else
		{
			return new double[]{dL, dM, dH};
		}
	}

	public boolean finished()
	{
		return !arrowMode.loop && finished;
	}
}