package logic.xstate;

import building.blueprint.*;
import entity.hero.*;
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
	public String text()
	{
		return "Error";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterMenu(builder);
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new BuildGUI(builder, blueprint);
	}
}