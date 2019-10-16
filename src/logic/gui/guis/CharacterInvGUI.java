package logic.gui.guis;

import entity.*;
import javafx.scene.input.*;
import logic.*;
import logic.xstate.*;

public class CharacterInvGUI extends Inv1GUI
{
	private InvEntity character;

	public CharacterInvGUI(InvEntity character)
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
}