package logic.gui.guis;

import building.blueprint.*;
import doubleinv.*;
import entity.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public final class SelectBuildingGUI extends XGUIState
{
	private static final CTile header = new CTile(2, 0, new GuiTile("gui.selectbuilding.header"), 2, 1);

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
		elements.add(new CElement(header));
		update();
	}

	@Override
	public CharSequence text()
	{
		return "menu.selectbuilding";
	}

	@Override
	public String keybind()
	{
		return "state.selectbuilding";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		if(mainState.levelMap().getBuilding(builder.location()) != null)
			return false;
		if(builder instanceof XCharacter character)
		{
			return mainState.levelMap().canBuild() && character.resources().ready(1);
		}
		else
		{
			return true;
		}
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