package logic.xstate;

import entity.*;
import logic.*;
import statsystem.animation.*;

public final class RegenerateState implements NAutoState
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
		mainState.side().setStandardSideInfo(character);
		arrow = new RegenerationAnim(character, mainState.levelMap());
	}

	@Override
	public CharSequence text()
	{
		return "menu.regenerate";
	}

	@Override
	public String keybind()
	{
		return "state.regenerate";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return character.resources().ready() && character.stats().getRegenerateChange() > 0;
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