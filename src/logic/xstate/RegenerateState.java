package logic.xstate;

import entity.*;
import logic.*;

public class RegenerateState implements NAutoState
{
	private XEntity entity;
	private NState nextState;
	private AnimTimer arrow;

	public RegenerateState(XEntity entity, NState nextState)
	{
		this.entity = entity;
		this.nextState = nextState;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfoXH(entity.standardSideInfo(), entity);
		arrow = mainState.combatSystem.createRegenerationAnimation(entity);
	}

	@Override
	public String text()
	{
		return "Regenerate";
	}

	@Override
	public String keybind()
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

	@Override
	public void tick(MainState mainState)
	{
		arrow.tick();
	}

	@Override
	public boolean finished()
	{
		return arrow.finished();
	}

	@Override
	public NState nextState()
	{
		return nextState;
	}
}