package logic.xstate;

import arrow.*;
import entity.*;
import java.util.*;
import logic.*;

public class EnemyStartState implements NAutoState
{
	private int counter;
	private List<EntityArrowC> ac;

	@Override
	public void onEnter(MainState mainState)
	{
		ac = new ArrayList<>();
		for(XEnemy xEnemy : mainState.levelMap.getEntitiesE())
		{
			xEnemy.startTurn();
			ac.add(new EntityArrowC(mainState, xEnemy, null, 0, 0, 0,
					0, 3, xEnemy.getStats().getStat(0),
					xEnemy.getStats().getMaxStat(0), xEnemy.getStats().getStartTurnChange(), 0));
		}
	}

	@Override
	public void tick(MainState mainState)
	{
		ac.forEach(e -> e.getEntity().getStats().change(e.tick(mainState)));
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= 10 && ac.stream().mapToInt(EntityArrowC::finished).min().orElse(0) >= 0;
	}

	@Override
	public NState nextState()
	{
		return new EnemyPhaseState();
	}

	@Override
	public String text()
	{
		return "Enemy Phase";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}