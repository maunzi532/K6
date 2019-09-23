package logic.xstate;

import arrow.*;
import entity.*;
import javafx.scene.input.*;
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
		mainState.sideInfoFrame.setSideInfo(entity.standardSideInfo(), null);
		arrow = mainState.combatSystem.createRegenerationAnimation(entity, mainState);
	}

	@Override
	public String text()
	{
		return "Regenerate";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.R;
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