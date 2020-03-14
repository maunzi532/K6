package system2.animation;

import arrow.*;
import entity.*;
import system2.*;

public class AnimPartHit implements AnimPart
{
	private static final int DURATION = 20;
	private static final int BLINKTIME = 5;
	private static final int SPEED = 2;

	private final XEntity target;
	private final StatBar statBar;
	private final Arrows arrows;
	private final int reduction;
	private final boolean crit;
	private final boolean melt;
	private final BlinkArrow arrow;
	private int counter;

	public AnimPartHit(XEntity target, Stats statsT, int damage, StatBar statBar,
			boolean crit, boolean melt, Arrows arrows)
	{
		this.target = target;
		this.statBar = statBar;
		this.crit = crit;
		this.melt = melt;
		this.arrows = arrows;
		arrow = new BlinkArrow(target.location(), DURATION, false, target.getImage(), BLINKTIME);
		reduction = Math.min(statsT.getCurrentHealth(), damage);
		statsT.setCurrentHealth(Math.max(0, statsT.getCurrentHealth() - damage));
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
		if(counter == AnimPartAttack.DODGETIME)
		{
			if(crit)
			{
				arrows.addScreenshake(20);
			}
			arrows.addArrow(arrow);
			target.setReplacementArrow(arrow);
		}
		if(counter <= AnimPartAttack.DODGETIME)
			return false;
		int counter2 = counter - AnimPartAttack.DODGETIME;
		if(counter2 % SPEED == 0 && counter2 / SPEED <= reduction)
			statBar.setData(statBar.getData() - 1);
		return counter2 / SPEED >= reduction && arrow.finished();
	}
}