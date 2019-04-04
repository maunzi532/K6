package logic.xstate;

import entity.hero.*;
import gui.*;
import gui.guis.*;
import logic.*;

public class CharacterInvState implements NGUIState
{
	private XHero character;

	public CharacterInvState(XHero character)
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
		return XMenu.characterMenu(character);
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new Inv1GUI(character.outputInv());
	}
}