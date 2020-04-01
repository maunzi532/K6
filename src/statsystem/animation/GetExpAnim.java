package statsystem.animation;

import arrow.*;
import entity.*;
import statsystem.*;
import statsystem.analysis.*;

public final class GetExpAnim implements AnimTimer
{
	private static final int SPEED = 2;
	private static final int DELAY = 20;

	public static final int LEVELUP_EXP = 100;

	private final XCharacter entity;
	private final XCharacter entityT;
	private final Stats stats;
	private final Stats statsT;
	private InfoArrow expBar;
	private InfoArrow expBarT;
	private boolean levelup;
	private boolean levelupT;
	private int expAmount;
	private int expAmountT;
	private int startExp;
	private int startExpT;
	private int endExp;
	private int endExpT;
	private boolean finished;
	private int counter;

	public GetExpAnim(AttackInfo aI, RNGOutcome2 result, Arrows arrows)
	{
		entity = aI.entity;
		entityT = aI.entityT;
		stats = entity.stats();
		statsT = entityT.stats();
		if(entity.team() == CharacterTeam.HERO)
		{
			levelup = stats.exp() >= LEVELUP_EXP;
			expAmount = 20;
			expBar = new InfoArrow(entity.location(), "arrow.healthbar.exp",
					"arrow.healthbar.background", "arrow.healthbar.text", stats.exp(), LEVELUP_EXP);
			arrows.addArrow(expBar);
		}
		if(entityT.team() == CharacterTeam.HERO)
		{
			levelupT = statsT.exp() >= LEVELUP_EXP;
			expAmountT = 20;
			expBarT = new InfoArrow(entityT.location(), "arrow.healthbar.exp",
					"arrow.healthbar.background", "arrow.healthbar.text", statsT.exp(), LEVELUP_EXP);
			arrows.addArrow(expBarT);
		}
		if(expAmount <= 0 && expAmountT <= 0)
			finished = true;
	}

	public void start()
	{
		if(entity.team() == CharacterTeam.HERO)
		{
			startExp = stats.exp();
			expBar.statBar().setCurrent(startExp);
			stats.addExp(expAmount);
			endExp = stats.exp();
		}
		if(entityT.team() == CharacterTeam.HERO)
		{
			startExpT = statsT.exp();
			expBarT.statBar().setCurrent(startExpT);
			statsT.addExp(expAmountT);
			endExpT = statsT.exp();
		}
	}

	@Override
	public boolean finished()
	{
		if(!finished)
			return false;
		if(expBar != null)
			expBar.remove();
		if(expBarT != null)
			expBarT.remove();
		return true;
	}

	@Override
	public void tick()
	{
		counter++;
		if(counter % SPEED == 0)
		{
			if(counter / SPEED <= endExp - startExp)
				expBar.statBar().alterCurrent(1);
			if(counter / SPEED <= endExpT - startExpT)
				expBarT.statBar().alterCurrent(1);
		}
		if(counter / SPEED >= expAmount + DELAY && counter / SPEED >= expAmountT + DELAY)
			finished = true;
	}

	public boolean isLevelup()
	{
		return levelup;
	}

	public boolean isLevelupT()
	{
		return levelupT;
	}
}