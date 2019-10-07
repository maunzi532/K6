package logic.editor.xgui;

import entity.*;
import javafx.scene.input.*;
import logic.xstate.*;

public class EntityInvEditGUI extends InvEditGUI
{
	private InvEntity invEntity;

	public EntityInvEditGUI(InvEntity invEntity)
	{
		super(invEntity.inputInv(), invEntity.name(), invEntity.getStats().infoEdit());
		this.invEntity = invEntity;
	}

	@Override
	public String text()
	{
		return "Edit Inv.";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.I;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(invEntity);
	}
}