package logic.xstate;

import entity.*;
import logic.*;
import system2.animation.*;

public class RegenerateState implements NAutoState
{
	private final XCharacter character;
	private final NState nextState;
	private AnimTimer arrow;

	public RegenerateState(XCharacter character, NState nextState)
	{
		this.character = character;
		this.nextState = nextState;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setStandardSideInfo(character, mainState.colorScheme);
		arrow = new RegenerationAnim(character, mainState.levelMap, mainState.colorScheme);
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
		return character.resources().ready(0) && character.stats().getRegenerateChange() > 0;
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