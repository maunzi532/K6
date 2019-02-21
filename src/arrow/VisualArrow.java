package arrow;

import geom.hex.*;
import javafx.scene.image.*;
import levelMap.*;

public class VisualArrow implements MArrow
{
	private static final double SHINE_WIDTH = 0.65;
	private static final double ZERO_SHINE_MULTIPLIER = 1.5;

	private final Hex start;
	private final Hex end;
	private final ArrowMode arrowMode;
	private final int timerEnd;
	private final Image image;
	private int timer;
	private boolean finished;
	private DoubleHex[] arrowPoints;
	private final boolean zero;
	private final double approxDistance;

	public VisualArrow(Hex start, Hex end, ArrowMode arrowMode, int timerEnd, Image image)
	{
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
			approxDistance = end.distance(start);
	}

	@Override
	public boolean isVisible(Hex mid, int range)
	{
		return start.distance(mid) <= range && end.distance(mid) <= range;
	}

	@Override
	public boolean showArrow()
	{
		return arrowMode.showArrow && (arrowMode.showZero || !zero);
	}

	@Override
	public boolean showShine()
	{
		return arrowMode.showShine;
	}

	@Override
	public boolean showTransport()
	{
		return arrowMode.showTransport;
	}

	@Override
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

	@Override
	public DoubleHex[] getArrowPoints()
	{
		return arrowPoints;
	}

	@Override
	public DoubleHex visualStart()
	{
		return new DoubleHex(start);
	}

	@Override
	public DoubleHex visualEnd()
	{
		if(zero)
			return new DoubleHex(end).add(normalUp());
		else
			return new DoubleHex(end);
	}

	private DoubleHex normal()
	{
		return DoubleHex.normalizeHex(end.subtract(start));
	}

	private DoubleHex normalUp()
	{
		return DoubleHex.normalizeHex(new Hex(1, 1, -2));
	}

	@Override
	public DoubleHex currentTLocation()
	{
		return DoubleHex.hexLerp(visualStart(), visualEnd(), timer / (double) timerEnd);
	}

	@Override
	public Image transported()
	{
		return image;
	}

	private void calculateArrowPoints()
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

	@Override
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

	@Override
	public boolean finished()
	{
		return !arrowMode.loop && finished;
	}
}