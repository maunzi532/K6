package arrow;

import entity.VisibleReplace;
import geom.hex.*;
import javafx.scene.image.Image;

public class VisualArrow implements VisibleReplace
{
	private static final double SHINE_WIDTH = 0.65;

	private final Hex start;
	private final Hex end;
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
		zero = end.equals(start);
		if(hasArrow())
			calculateArrowPoints();
		if(zero)
			approxDistance = 1.5;
		else
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

	public DoubleHex visualStart()
	{
		return new DoubleHex(start);
	}

	public DoubleHex visualEnd()
	{
		if(zero)
			return new DoubleHex(end).add(normalUp());
		else
			return new DoubleHex(end);
	}

	public DoubleHex normal()
	{
		return DoubleHex.normalizeHex(end.subtract(start));
	}

	public DoubleHex normalUp()
	{
		return DoubleHex.normalizeHex(new Hex(1, 1, -2));
	}

	public DoubleHex currentTLocation()
	{
		return DoubleHex.hexLerp(visualStart(), visualEnd(), timer / (double) timerEnd);
	}

	public void calculateArrowPoints()
	{
		DoubleHex normal = zero ? normalUp() : normal();
		DoubleHex sideNormalX2 = normal.rotate(false).subtract(normal.rotate(true));
		DoubleHex side0 = sideNormalX2.multiply(0.15);
		DoubleHex side1 = sideNormalX2.multiply(0.25);
		DoubleHex end0 = visualEnd();
		DoubleHex end1 = end0.subtract(normal.multiply(0.5));
		arrowPoints = new DoubleHex[7];
		arrowPoints[0] = visualStart().add(side0);
		arrowPoints[6] = visualStart().subtract(side0);
		arrowPoints[1] = end1.add(side0);
		arrowPoints[5] = end1.subtract(side0);
		arrowPoints[2] = end1.add(side1);
		arrowPoints[4] = end1.subtract(side1);
		arrowPoints[3] = end0;
	}

	public double[] getShine()
	{
		/*if(zero)
			return new double[]{0, 0.5, 1};*/
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