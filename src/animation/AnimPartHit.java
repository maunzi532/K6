package animation;

import arrow.*;

public final class AnimPartHit implements AnimPart
{
	private static final int DURATION = 20;
	private static final int BLINKTIME = 5;
	private static final int SPEED = 2;

	private final AnimCharacter target;
	private final int damage;
	private final StatBar statBar;
	private final boolean crit;
	private final boolean dodge;
	private final Arrows arrows;
	private final BlinkArrow arrow;
	private int counter;

	public AnimPartHit(AnimCharacter target, int damage, StatBar statBar, boolean crit, boolean dodge, Arrows arrows)
	{
		this.target = target;
		this.damage = damage;
		this.statBar = statBar;
		this.crit = crit;
		this.dodge = dodge;
		this.arrows = arrows;
		arrow = new BlinkArrow(target.location(), DURATION, false, target.mapImageName(), BLINKTIME);
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
			target.replaceVisual(arrow);
		}
		if(counter <= AnimPartAttack.DODGETIME)
			return false;
		int counter2 = counter - AnimPartAttack.DODGETIME;
		if(counter2 % SPEED == 0 && counter2 / SPEED <= damage)
			statBar.alterCurrent(-1);
		return counter2 / SPEED >= damage && arrow.finished();
	}
}