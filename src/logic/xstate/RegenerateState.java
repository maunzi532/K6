package logic.xstate;

import entity.*;
import logic.*;

public class RegenerateState implements NAutoState
{
	private final XCharacter entity;
	private final NState nextState;
	private AnimTimer arrow;

	public RegenerateState(XCharacter entity, NState nextState)
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
		if(entity.team() == CharacterTeam.HERO)
			return entity.resources().ready(0) && entity.stats().getRegenerateChange() > 0;
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