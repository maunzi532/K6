package system2.animation;

import arrow.*;
import system2.*;

public class AnimPartHealthCost implements AnimPart
{
	private static final int SPEED = 2;

	private final int healthCost;
	private final StatBar statBar;
	private int counter;

	public AnimPartHealthCost(int healthCost, Stats stats, StatBar statBar)
	{
		this.healthCost = healthCost;
		this.statBar = statBar;
		stats.setCurrentHealth(stats.getCurrentHealth() - healthCost);
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
			statBar.setData(statBar.getData() - 1);
		return counter / SPEED >= healthCost;
	}
}