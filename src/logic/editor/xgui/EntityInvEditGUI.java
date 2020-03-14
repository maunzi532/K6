package logic.editor.xgui;

import entity.*;
import logic.xstate.*;

public class EntityInvEditGUI extends InvEditGUI
{
	private final XCharacter invEntity;

	public EntityInvEditGUI(XCharacter invEntity)
	{
		super(invEntity.inputInv(), invEntity.name(), invEntity.stats().infoEdit());
		this.invEntity = invEntity;
	}

	@Override
	public String text()
	{
		return "Edit Inv.";
	}

	@Override
	public String keybind()
	{
		return "Entity Inv Edit";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(invEntity);
	}
}