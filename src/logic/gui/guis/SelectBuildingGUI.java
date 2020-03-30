package logic.gui.guis;

import building.blueprint.*;
import doubleinv.*;
import entity.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public final class SelectBuildingGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, new GuiTile("Buildings"), 2, 1);

	private final XBuilder builder;

	public SelectBuildingGUI(XBuilder builder)
	{
		this.builder = builder;
	}

	@Override
	public boolean editMode()
	{
		return !(builder instanceof XCharacter);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		if(builder instanceof XCharacter character)
		{
			mainState.side().setStandardSideInfo(character);
		}
		ScrollList<BuildingBlueprint> buildingsView = new ScrollList<>(0, 1, 6, 5, 2, 1,
				mainState.blueprintFile().allBlueprints(), SelectBuildingGUI::itemView,
				target -> mainState.stateHolder().setState(new BuildGUI(builder, target)));
		elements.add(buildingsView);
		elements.add(new CElement(textInv));
		update();
	}

	@Override
	public String text()
	{
		return "Build";
	}

	@Override
	public String keybind()
	{
		return "Select Building";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return !(builder instanceof XCharacter character) || (character.resources().ready(1) && mainState.levelMap().getBuilding(builder.location()) == null);
	}

	@Override
	public XMenu menu()
	{
		if(builder instanceof XCharacter character)
		{
			return XMenu.characterGUIMenu(character);
		}
		else
		{
			return XMenu.NOMENU;
		}
	}

	@Override
	public int xw()
	{
		return 6;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	private static GuiTile[] itemView(BuildingBlueprint blueprint)
	{
		return new GuiTile[]
				{
						new GuiTile(blueprint.name()),
						new GuiTile(null, "building.default", false, null)
				};
	}
}