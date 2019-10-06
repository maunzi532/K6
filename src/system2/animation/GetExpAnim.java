package system2.animation;

import arrow.*;
import entity.*;
import javafx.scene.paint.*;
import logic.*;
import system2.*;
import system2.analysis.*;

public class GetExpAnim implements AnimTimer
{
	private static final int SPEED = 2;
	private static final int DELAY = 20;

	private XEntity entity;
	private XEntity entityT;
	private InfoArrow expBar;
	private InfoArrow expBarT;
	private int expAmount;
	private int expAmountT;
	private boolean finished;
	private int counter;

	public GetExpAnim(AttackInfo2 aI, RNGOutcome2 result, MainState mainState)
	{
		entity = aI.entity;
		entityT = aI.entityT;
		Stats2 stats = (Stats2) entity.getStats();
		Stats2 statsT = (Stats2) entityT.getStats();
		if(entity instanceof XHero)
		{
			expAmount = 20;
			expBar = new InfoArrow(entity.location(), Color.PURPLE, Color.BLACK, Color.WHITE, stats.getExp(), 100);
			mainState.levelMap.addArrow(expBar);
			stats.addExp(expAmount);
		}
		if(entityT instanceof XHero)
		{
			expAmountT = 20;
			expBarT = new InfoArrow(entityT.location(), Color.PURPLE, Color.BLACK, Color.WHITE, statsT.getExp(), 100);
			mainState.levelMap.addArrow(expBarT);
			statsT.addExp(expAmountT);
		}
	}

	@Override
	public boolean finished()
	{
		if(!finished && (expAmount > 0 || expAmountT > 0))
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
			if(counter / SPEED <= expAmount)
				expBar.statBar().setData(expBar.statBar().getData() + 1);
			if(counter / SPEED <= expAmountT)
				expBarT.statBar().setData(expBarT.statBar().getData() + 1);
		}
		if(counter / SPEED >= expAmount + DELAY && counter / SPEED >= expAmountT + DELAY)
			finished = true;
	}
}