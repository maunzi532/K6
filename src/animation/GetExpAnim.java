package animation;

import arrow.*;
import entity.*;
import system.*;

public final class GetExpAnim implements AnimTimer
{
	private static final int SPEED = 2;
	private static final int DELAY = 20;

	public static final int LEVELUP_EXP = 100;

	private final XCharacter entity;
	private final XCharacter entityT;
	private final ClassAndLevelSystem cls;
	private final ClassAndLevelSystem clsT;
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

	public GetExpAnim(AttackCalc4 aI, ACResult4 result, Arrows arrows)
	{
		entity = aI.aI.initiator();
		entityT = aI.aI.target();
		cls = entity.systemChar().cls();
		clsT = entityT.systemChar().cls();
		if(cls.maxExp() > 0)
		{
			levelup = cls.exp() >= LEVELUP_EXP;
			expAmount = 20;
			expBar = new InfoArrow(entity.location(), new StatBar("arrow.healthbar.exp",
					"arrow.healthbar.background", "arrow.healthbar.text", cls.exp(), LEVELUP_EXP));
			arrows.addArrow(expBar);
		}
		if(clsT.maxExp() > 0)
		{
			levelupT = clsT.exp() >= LEVELUP_EXP;
			expAmountT = 20;
			expBarT = new InfoArrow(entityT.location(), new StatBar("arrow.healthbar.exp",
					"arrow.healthbar.background", "arrow.healthbar.text", clsT.exp(), LEVELUP_EXP));
			arrows.addArrow(expBarT);
		}
		if(expAmount <= 0 && expAmountT <= 0)
			finished = true;
	}

	public void start()
	{
		if(cls.maxExp() > 0)
		{
			startExp = cls.exp();
			expBar.statBar().setCurrent(startExp);
			cls.addExp(expAmount);
			endExp = cls.exp();
		}
		if(clsT.maxExp() > 0)
		{
			startExpT = clsT.exp();
			expBarT.statBar().setCurrent(startExpT);
			clsT.addExp(expAmountT);
			endExpT = clsT.exp();
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