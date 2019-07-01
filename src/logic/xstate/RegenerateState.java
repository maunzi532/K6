package logic.xstate;

import arrow.*;
import entity.*;
import logic.*;

public class RegenerateState implements NAutoState
{
	private XEntity entity;
	private NState nextState;
	private EntityArrowC acE;

	public RegenerateState(XEntity entity, NState nextState)
	{
		this.entity = entity;
		this.nextState = nextState;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.setSideInfo(entity.standardSideInfo(), null);
		acE = new EntityArrowC(mainState, entity, null, 0, 0, 0,
				0, 3, entity.getStats().getStat(0),
				entity.getStats().getMaxStat(0), entity.getStats().getRegenerateChange(), 0);
		entity.getStats().regenerating();
	}

	@Override
	public void tick(MainState mainState)
	{
		entity.getStats().change(acE.tick(mainState));
	}

	@Override
	public boolean finished()
	{
		return acE.finished() >= 0;
	}

	@Override
	public NState nextState()
	{
		return nextState;
	}

	@Override
	public String text()
	{
		return "Regenerate";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		if(entity instanceof XHero)
			return ((XHero) entity).ready(0) && entity.getStats().getRegenerateChange() > 0;
		return false;
	}
}