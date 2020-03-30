package statsystem.animation;

import arrow.*;
import entity.*;

public final class AnimPartVanish implements AnimPart
{
	private static final int DURATION = 20;
	private static final int BLINKTIME = 3;

	private final BlinkArrow arrow;

	public AnimPartVanish(XCharacter target, Arrows arrows)
	{
		arrow = new BlinkArrow(target.location(), DURATION, false, target.mapImageName(), BLINKTIME);
		arrows.addArrow(arrow);
	}

	@Override
	public boolean finished1()
	{
		return false;
	}

	@Override
	public boolean finished2()
	{
		return false;
	}

	@Override
	public boolean tick()
	{
		return arrow.finished();
	}
}