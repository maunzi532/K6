package logic.gui.guis;

import entity.*;
import logic.*;
import logic.xstate.*;

public final class CharacterLevelupGUI extends Inv1GUI
{
	private final XCharacter character;
	private final NState nextState;

	public CharacterLevelupGUI(XCharacter character, NState nextState)
	{
		super(character.inputInv(), character.name(), null);
		this.character = character;
		this.nextState = nextState;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		int[] levelup = character.stats().levelSystem().getLevelup(character.stats());
		baseInfo = character.stats().levelupText(levelup);
		character.stats().levelup(levelup);
		super.onEnter(mainState);
	}

	@Override
	public void close(XStateHolder stateHolder)
	{
		stateHolder.setState(nextState);
	}
}