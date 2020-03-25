package system2.animation;

import arrow.*;
import entity.*;
import logic.*;
import system2.*;

public class RegenerationAnim implements AnimTimer
{
	private static final int SPEED = 2;

	private InfoArrow healthBar;
	private int regenerateAmount;
	private boolean finished;
	private int counter;

	public RegenerationAnim(XCharacter entity, Arrows arrows, ColorScheme colorScheme)
	{
		Stats stats = entity.stats();
		healthBar = new InfoArrow(entity.location(),
				colorScheme.color(entity.team().healthBarColor), colorScheme.color("arrow.healthbar.background"),
				colorScheme.color("arrow.healthbar.text"),
				stats.currentHealth(), stats.maxHealth());
		arrows.addArrow(healthBar);
		regenerateAmount = stats.maxHealth() - stats.currentHealth();
		stats.regenerating();
		stats.setCurrentHealth(stats.maxHealth());
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
			healthBar.statBar().setData(healthBar.statBar().getData() + 1);
		if(counter / SPEED >= regenerateAmount)
			finished = true;
	}
}