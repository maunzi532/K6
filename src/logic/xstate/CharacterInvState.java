package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import logic.*;

public class CharacterInvState implements NGUIState
{
	private InvEntity character;

	public CharacterInvState(InvEntity character)
	{
		this.character = character;
	}

	@Override
	public String text()
	{
		return "Inv.";
	}

	@Override
	public XMenu menu()
	{
		if(character instanceof XHero)
			return XMenu.characterGUIMenu((XHero) character);
		else
			return XMenu.enemyGUIMenu(character);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.setSideInfoXH(character.standardSideInfo(), character instanceof XHero);
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new Inv1GUI(character.outputInv(), character.name(), character.getStats().info());
	}
}