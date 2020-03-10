package system2.animation;

import arrow.*;
import entity.*;
import javafx.scene.paint.*;
import levelMap.*;
import system2.*;

public class RegenerationAnim implements AnimTimer
{
	private static final int SPEED = 2;

	private XEntity entity;
	private LevelMap levelMap;
	private InfoArrow healthBar;
	private int regenerateAmount;
	private boolean finished;
	private int counter;

	public RegenerationAnim(XEntity entity, LevelMap levelMap)
	{
		this.entity = entity;
		this.levelMap = levelMap;
		Stats2 stats = (Stats2) entity.getStats();
		healthBar = new InfoArrow(entity.location(),
				entity instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK, Color.WHITE,
				stats.getCurrentHealth(), stats.maxHealth());
		levelMap.addArrow(healthBar);
		regenerateAmount = stats.maxHealth() - stats.getCurrentHealth();
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