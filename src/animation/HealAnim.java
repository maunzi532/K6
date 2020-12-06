package animation;

import arrow.*;
import entity.*;

public final class HealAnim implements AnimTimer
{
	private static final int SPEED = 2;

	private final InfoArrow healthBar;
	private final int healAmount;
	private boolean finished;
	private int counter;

	public HealAnim(XCharacter entity, Arrows arrows, int heal)
	{
		healthBar = new InfoArrow(entity.location(), entity.hpBar());
		arrows.addArrow(healthBar);
		healAmount = Math.min(heal, entity.maxHP() - entity.currentHP());
		entity.setHP(Math.min(entity.currentHP() + heal, entity.maxHP()));
	}

	@Override
	public boolean finished()
	{
		if(!finished)
			return false;
		healthBar.remove();
		return true;
	}

	@Override
	public void tick()
	{
		counter++;
		if(counter % SPEED == 0)
			healthBar.statBar().alterCurrent(1);
		if(counter / SPEED >= healAmount)
			finished = true;
	}
}