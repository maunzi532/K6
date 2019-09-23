package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import javafx.scene.input.*;
import logic.*;

public class CharacterInvState implements NGUIState
{
	private InvEntity character;

	public CharacterInvState(InvEntity character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfoXH(character.standardSideInfo(), character instanceof XHero);
	}

	@Override
	public String text()
	{
		return "Inv.";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.I;
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
	public XGUI gui(MainState mainState)
	{
		return new Inv1GUI(character.outputInv(), character.name(), character.getStats().info());
	}
}