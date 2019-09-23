package system2.animation;

import arrow.*;
import entity.*;
import logic.*;

public class AnimPartVanish implements AnimPart
{
	private static final int DURATION = 20;
	private static final int BLINKTIME = 3;

	private final XEntity target;
	private BlinkArrow arrow;

	public AnimPartVanish(XEntity target, MainState mainState)
	{
		this.target = target;
		arrow = new BlinkArrow(target.location(), DURATION, false, target.getImage(), BLINKTIME);
		mainState.levelMap.addArrow(arrow);
		mainState.levelMap.removeEntity(target);
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