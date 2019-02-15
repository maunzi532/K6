package arrow;

import entity.*;
import geom.hex.*;
import javafx.scene.image.*;

public class VisualArrow implements VisibleReplace
{
	private static final double SHINE_WIDTH = 0.65;

	public final Hex start;
	public final Hex end;
	public final ArrowMode arrowMode;
	private final int timerEnd;
	public final Image image;
	private int timer;
	private boolean finished;
	private DoubleHex[] arrowPoints;
	private final boolean zero;
	private final double approxDistance;

	public VisualArrow(Hex start, Hex end, ArrowMode arrowMode, int timerEnd)
	{
		this(start, end, arrowMode, timerEnd, null);
	}

	public VisualArrow(Hex start, Hex end, ArrowMode arrowMode, int timerEnd, Image image)
	{
		this.start = start;
		this.end = end;
		this.arrowMode = arrowMode;
		this.timerEnd = timerEnd;
		this.image = image;
		if(hasArrow())
			calculateArrowPoints();
		zero = end.equals(start);
		approxDistance = end.distance(start);
	}

	public boolean hasArrow()
	{
		return arrowMode.showArrow && (arrowMode.showZero || !zero);
	}

	public boolean isVisible(Hex mid, int range)
	{
		return start.distance(mid) <= range && end.distance(mid) <= range;
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

	public DoubleHex[] getArrowPoints()
	{
		return arrowPoints;
	}

	public DoubleHex normal()
	{
		return DoubleHex.normalizeHex(end.subtract(start));
	}

	public DoubleHex currentTLocation()
	{
		return DoubleHex.hexLerp(start, end, timer / (double) timerEnd);
	}

	public void calculateArrowPoints()
	{
		if(start.equals(end))
		{
			arrowPoints = new DoubleHex[0];
		}
		else
		{
			DoubleHex normal = normal();
			DoubleHex sideNormalX2 = normal.rotate(false).subtract(normal.rotate(true));
			DoubleHex side0 = sideNormalX2.multiply(0.15);
			DoubleHex side1 = sideNormalX2.multiply(0.25);
			DoubleHex end1 = new DoubleHex(end).subtract(normal.multiply(0.5));
			arrowPoints = new DoubleHex[7];
			arrowPoints[0] = new DoubleHex(start).add(side0);
			arrowPoints[6] = new DoubleHex(start).subtract(side0);
			arrowPoints[1] = end1.add(side0);
			arrowPoints[5] = end1.subtract(side0);
			arrowPoints[2] = end1.add(side1);
			arrowPoints[4] = end1.subtract(side1);
			arrowPoints[3] = new DoubleHex(end);
		}
	}

	public double[] getShine()
	{
		if(zero)
			return new double[0];
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

	@Override
	public boolean finished()
	{
		return !arrowMode.loop && finished;
	}
}