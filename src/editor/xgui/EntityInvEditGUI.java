package editor.xgui;

import entity.*;
import java.util.*;
import xstate.*;

public final class EntityInvEditGUI extends InvEditGUI
{
	private final XCharacter character;

	public EntityInvEditGUI(XCharacter character)
	{
		super(character.inv(), character.name(), List.of()/*invEntity.stats().infoEdit()*/);
		this.character = character;
	}

	@Override
	public CharSequence text()
	{
		return "menu.edit.inv.character";
	}

	@Override
	public String keybind()
	{
		return "state.edit.inv.character";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(character);
	}
}