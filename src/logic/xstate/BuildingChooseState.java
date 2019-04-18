package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import logic.*;

public class BuildingChooseState implements NGUIState
{
	private XHero builder;

	public BuildingChooseState(XHero builder)
	{
		this.builder = builder;
	}

	@Override
	public String text()
	{
		return "Build";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterMenu(builder);
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return builder.isReady() && builder.getAp() >= 1 && mainState.levelMap.getBuilding(builder.location()) == null;
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new BuildingsGUI(builder, mainState.buildingBlueprintCache);
	}
}