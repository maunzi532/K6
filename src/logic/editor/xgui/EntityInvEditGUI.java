package logic.editor.xgui;

import entity.*;
import logic.xstate.*;

public final class EntityInvEditGUI extends InvEditGUI
{
	private final XCharacter invEntity;

	public EntityInvEditGUI(XCharacter invEntity)
	{
		super(invEntity.inputInv(), invEntity.name(), invEntity.stats().infoEdit());
		this.invEntity = invEntity;
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
		return XMenu.entityEditMenu(invEntity);
	}
}