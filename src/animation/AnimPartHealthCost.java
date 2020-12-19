package animation;

import arrow.*;

public final class AnimPartHealthCost implements AnimPart
{
	private static final int SPEED = 2;

	private final int healthCost;
	private final StatBar statBar;
	private int counter;

	public AnimPartHealthCost(int healthCost, StatBar statBar)
	{
		this.healthCost = healthCost;
		this.statBar = statBar;
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
		counter++;
		if(counter % SPEED == 0)
			statBar.alterCurrent(-1);
		return counter / SPEED >= healthCost;
	}
}