package logic.gui.guis;

import entity.*;
import logic.*;
import logic.xstate.*;

public class CharacterInvGUI extends Inv1GUI
{
	private XEntity character;

	public CharacterInvGUI(XEntity character)
	{
		super(character.inputInv(), character.name(), character.getStats().info());
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfoXH(character.standardSideInfo(), character);
		super.onEnter(mainState);
	}

	@Override
	public String text()
	{
		return "Inv.";
	}

	@Override
	public String keybind()
	{
		return "Character Inv";
	}

	@Override
	public XMenu menu()
	{
		if(character instanceof XHero)
			return XMenu.characterGUIMenu((XHero) character);
		else
			return XMenu.enemyGUIMenu(character);
	}
}