package logic.xstate;

import building.blueprint.*;
import entity.*;
import gui.*;
import gui.guis.*;
import logic.*;

public class BuildState implements NGUIState
{
	private XHero builder;
	private BuildingBlueprint blueprint;

	public BuildState(XHero builder, BuildingBlueprint blueprint)
	{
		this.builder = builder;
		this.blueprint = blueprint;
	}

	@Override
	public void onEnter(MainState mainState){}

	@Override
	public XMenu menu()
	{
		return XMenu.characterGUIMenu(builder);
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new BuildGUI(builder, blueprint);
	}
}