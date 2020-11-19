package statsystem.animation;

import arrow.*;
import entity.*;
import statsystem.*;

public final class RegenerationAnim implements AnimTimer
{
	private static final int SPEED = 2;

	private final InfoArrow healthBar;
	private final int regenerateAmount;
	private boolean finished;
	private int counter;

	public RegenerationAnim(XCharacter entity, Arrows arrows)
	{
		Stats stats = entity.stats();
		healthBar = new InfoArrow(entity.location(), entity.team().healthBarColor, "arrow.healthbar.background",
				"arrow.healthbar.text", entity.currentHP(), entity.maxHP());
		arrows.addArrow(healthBar);
		regenerateAmount = entity.maxHP() - entity.currentHP();
		stats.regenerating();
		stats.setCurrentHealth(entity.maxHP());
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
		if(counter / SPEED >= regenerateAmount)
			finished = true;
	}
}