package logic.gui.guis;

import entity.*;
import logic.*;
import logic.xstate.*;

public class CharacterLevelupGUI extends Inv1GUI
{
	private XEntity character;
	private NState nextState;

	public CharacterLevelupGUI(XEntity character, NState nextState)
	{
		super(character.inputInv(), character.name(), null);
		this.character = character;
		this.nextState = nextState;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfoXH(character.standardSideInfo(), character);
		baseInfo = character.getStats().levelup();
		super.onEnter(mainState);
	}

	@Override
	public void close(XStateHolder stateHolder)
	{
		stateHolder.setState(nextState);
	}
}