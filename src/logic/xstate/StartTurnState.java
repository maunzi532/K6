package logic.xstate;

import entity.*;
import logic.*;

public class StartTurnState implements NAutoState
{
	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.clearSideInfo();
		for(XHero xHero : mainState.levelMap.getEntitiesH())
		{
			xHero.startTurn();
			/*ac.add(new EntityArrowC(mainState, xHero, null, 0, 0, 0,
					0, 3, xHero.getStats().getStat(0),
					xHero.getStats().getMaxStat(0), xHero.getStats().getStartTurnChange(), 0));*/
		}
	}

	@Override
	public void tick(MainState mainState)
	{
		//ac.forEach(e -> e.getEntity().getStats().change(e.tick(mainState)));
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= 10;// && ac.stream().mapToInt(EntityArrowC::finished).min().orElse(0) >= 0;
	}

	@Override
	public NState nextState()
	{
		return NoneState.INSTANCE;
	}

	@Override
	public String text()
	{
		return "Start Turn";
	}
}