package system2.animation;

import arrow.*;
import entity.*;
import levelMap.*;
import system2.*;

public class AnimPartHit implements AnimPart
{
	private static final int DURATION = 40;
	private static final int BLINKTIME = 10;
	private static final int SPEED = 4;

	private final XEntity target;
	private final StatBar statBar;
	private final LevelMap levelMap;
	private int reduction;
	private BlinkArrow arrow;
	private int counter;

	public AnimPartHit(XEntity target, Stats2 statsT, int damage, StatBar statBar, LevelMap levelMap)
	{
		this.target = target;
		this.statBar = statBar;
		this.levelMap = levelMap;
		arrow = new BlinkArrow(target.location(), DURATION, false, target.getImage(), BLINKTIME);
		levelMap.addArrow(arrow);
		target.setReplacementArrow(arrow);
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
		if(counter % SPEED == 0 && counter / SPEED <= reduction)
			statBar.setData(statBar.getData() - 1);
		return counter / SPEED >= reduction && arrow.finished();
	}
}