package visual;

import arrow.*;
import geom.d1.*;
import geom.f1.*;
import javafx.scene.paint.*;

public class ArrowViewer
{
	private static final double SHINE_WIDTH = 0.65;
	private static final double ZERO_SHINE = 0.45;
	public static final double WIDTH = 0.15;
	public static final double HEAD_WIDTH = 0.25;
	public static final double HEAD_IN = 0.5;
	public static final double DATA_DISTANCE = 0.75;
	public static final double DATA_WIDTH = 2;
	public static final double DATA_HEIGHT = 0.3;

	private final DoubleType y2;

	public ArrowViewer(DoubleType y2)
	{
		this.y2 = y2;
	}

	public boolean isVisible(XArrow arrow, Tile mid, int range)
	{
		return arrow.locations().stream().anyMatch(e -> y2.distance(e, mid) <= range);
	}

	private DoubleTile normal(XArrow arrow)
	{
		if(arrow.locations().size() < 2)
			return y2.normalize(y2.upwards());
		else
			return y2.normalize(y2.subtract(arrow.locations().get(1), arrow.locations().get(0)));
	}

	private DoubleTile visualStart(XArrow arrow)
	{
		return y2.fromTile(arrow.locations().get(0));
	}

	private DoubleTile visualEnd(XArrow arrow)
	{
		if(arrow.locations().size() < 2)
		{
			if(arrow.zeroUpwards())
				return y2.add(y2.fromTile(arrow.locations().get(0)), y2.normalize(y2.upwards()));
			else
				return y2.fromTile(arrow.locations().get(0));
		}
		else
			return y2.fromTile(arrow.locations().get(1));
	}

	private double distance(XArrow arrow)
	{
		if(arrow.duration() == 0)
		{
			return 0;
		}
		return (arrow.counter() % arrow.duration()) / (double) arrow.duration();
	}

	public DoubleTile imageLocation(XArrow arrow)
	{
		return y2.tileLerp(visualStart(arrow), visualEnd(arrow), distance(arrow));
	}

	private DoubleTile[] calculateArrowPoints(ShineArrow arrow)
	{
		DoubleTile normal = normal(arrow);
		DoubleTile sideNormalX2 = y2.rotateR2(normal, false);
		DoubleTile side0 = y2.multiply(sideNormalX2, WIDTH);
		DoubleTile side1 = y2.multiply(sideNormalX2, HEAD_WIDTH);
		DoubleTile end0 = visualEnd(arrow);
		DoubleTile end1 = y2.subtract(end0, y2.multiply(normal, HEAD_IN));
		DoubleTile[] arrowPoints = new DoubleTile[7];
		arrowPoints[0] = y2.add(visualStart(arrow), side0);
		arrowPoints[6] = y2.subtract(visualStart(arrow), side0);
		arrowPoints[1] = y2.add(end1, side0);
		arrowPoints[5] = y2.subtract(end1, side0);
		arrowPoints[2] = y2.add(end1, side1);
		arrowPoints[4] = y2.subtract(end1, side1);
		arrowPoints[3] = end0;
		return arrowPoints;
	}

	public DoubleTile[] arrowPoints(ShineArrow arrow)
	{
		DoubleTile[] arrowPoints = arrow.storedArrowPoints();
		if(arrowPoints == null)
		{
			arrowPoints = calculateArrowPoints(arrow);
			arrow.storeArrowPoints(arrowPoints);
		}
		return arrowPoints;
	}

	private double shineW(ShineArrow arrow)
	{
		if(arrow.locations().size() < 2)
			return ZERO_SHINE;
		else
			return SHINE_WIDTH / y2.distance(arrow.locations().get(0), arrow.locations().get(1));
	}

	private double[] getShine(ShineArrow arrow, double dM, boolean loop)
	{
		double shineW = shineW(arrow);
		double dL = dM - shineW;
		double dH = dM + shineW;
		if(loop)
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

	public Paint shineFill(ShineArrow arrow, TileLayout layout)
	{
		if(arrow.hasShine())
		{
			double[][] gp = layout.polygonCorners(visualStart(arrow), visualEnd(arrow));
			double[] shine = getShine(arrow, distance(arrow), arrow.loop());
			Stop[] stops = new Stop[shine.length];
			for(int i = 0; i < shine.length; i++)
			{
				stops[i] = new Stop(shine[i], i % 3 == 1 ? Color.AZURE : Color.BLUE);
			}
			return new LinearGradient(gp[0][0], gp[1][0], gp[0][1], gp[1][1], false, CycleMethod.NO_CYCLE, stops);
		}
		else
		{
			return Color.BLUE;
		}
	}

	public DoubleTile dataLocation(InfoArrow arrow, TileLayout layout)
	{
		double[][] gp = layout.polygonCorners(visualStart(arrow), visualEnd(arrow));
		if(gp[1][0] < gp[1][1] || (gp[1][0] == gp[1][1] && gp[0][0] > gp[0][1]))
		{
			return y2.add(visualStart(arrow), y2.multiply(y2.normalize(y2.upwards()), DATA_DISTANCE));
		}
		else
		{
			return y2.subtract(visualStart(arrow), y2.multiply(y2.normalize(y2.upwards()), DATA_DISTANCE));
		}
	}
}