package logic.xstate;

import building.*;
import entity.*;
import gui.*;
import gui.guis.*;
import logic.*;

public class RemoveBuildingState implements NGUIState
{
	private XHero builder;

	public RemoveBuildingState(XHero builder)
	{
		this.builder = builder;
	}

	@Override
	public String text()
	{
		return "Remove";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterMenu(builder);
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return builder.isReady() && builder.getAp() >= 1 && mainState.levelMap.getBuilding(builder.location()) instanceof Buildable;
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new RemoveGUI(builder, (Buildable) mainState.levelMap.getBuilding(builder.location()));
	}
}