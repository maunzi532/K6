package system2.animation;

import arrow.*;
import entity.*;
import java.util.function.*;
import javafx.scene.paint.*;
import system2.*;
import system2.analysis.*;

public class GetExpAnim implements AnimTimer, Supplier<boolean[]>, Runnable
{
	private static final int SPEED = 2;
	private static final int DELAY = 20;

	public static final int LEVELUP_EXP = 100;

	private XCharacter entity;
	private XCharacter entityT;
	private Stats stats;
	private Stats statsT;
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
			levelup = stats.getExp() >= LEVELUP_EXP;
			expAmount = 20;
			expBar = new InfoArrow(entity.location(), Color.PURPLE, Color.BLACK, Color.WHITE, stats.getExp(), LEVELUP_EXP);
			arrows.addArrow(expBar);
		}
		if(entityT.team() == CharacterTeam.HERO)
		{
			levelupT = statsT.getExp() >= LEVELUP_EXP;
			expAmountT = 20;
			expBarT = new InfoArrow(entityT.location(), Color.PURPLE, Color.BLACK, Color.WHITE, statsT.getExp(), LEVELUP_EXP);
			arrows.addArrow(expBarT);
		}
		if(expAmount <= 0 && expAmountT <= 0)
			finished = true;
	}

	@Override
	public void run()
	{
		if(entity.team() == CharacterTeam.HERO)
		{
			startExp = stats.getExp();
			expBar.statBar().setData(startExp);
			stats.addExp(expAmount);
			endExp = stats.getExp();
		}
		if(entityT.team() == CharacterTeam.HERO)
		{
			startExpT = statsT.getExp();
			expBarT.statBar().setData(startExpT);
			statsT.addExp(expAmountT);
			endExpT = statsT.getExp();
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
				expBar.statBar().setData(expBar.statBar().getData() + 1);
			if(counter / SPEED <= endExpT - startExpT)
				expBarT.statBar().setData(expBarT.statBar().getData() + 1);
		}
		if(counter / SPEED >= expAmount + DELAY && counter / SPEED >= expAmountT + DELAY)
			finished = true;
	}

	@Override
	public boolean[] get()
	{
		return new boolean[]{levelup, levelupT};
	}
}