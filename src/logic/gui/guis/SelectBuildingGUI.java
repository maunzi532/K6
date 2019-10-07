package logic.gui.guis;

import building.*;
import building.blueprint.*;
import entity.*;
import file.*;
import javafx.scene.input.*;
import levelMap.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class SelectBuildingGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, new GuiTile("Buildings"), 2, 1);

	private final XBuilder builder;
	private ScrollList<BuildingBlueprint> buildingsView;

	public SelectBuildingGUI(XBuilder builder)
	{
		this.builder = builder;
	}

	@Override
	public boolean editMode()
	{
		return !(builder instanceof XHero);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		if(builder instanceof XHero)
		{
			mainState.sideInfoFrame.setSideInfo(((XHero) builder).standardSideInfo(), null);
		}
		BlueprintCache<BuildingBlueprint> blueprintCache = mainState.buildingBlueprintCache;
		buildingsView = new ScrollList<>(0, 1, 6, 5, 2, 1);
		buildingsView.elements = blueprintCache.allBlueprints();
		update();
	}

	@Override
	public String text()
	{
		return "Build";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.B;
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return !(builder instanceof XHero) || (((XHero) builder).ready(1) && mainState.levelMap.getBuilding(builder.location()) == null);
	}

	@Override
	public XMenu menu()
	{
		if(builder instanceof XHero)
		{
			return XMenu.characterGUIMenu((XHero) builder);
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

	@Override
	private void update()
	{
		initTiles();
		buildingsView.update();
		buildingsView.draw(tiles, this::itemView);
		setFilledTile(textInv);
	}

	public GuiTile[] itemView(BuildingBlueprint blueprint)
	{
		return new GuiTile[]
				{
						new GuiTile(blueprint.name),
						new GuiTile(null, MBuilding.IMAGE, false, null)
				};
	}

	@Override
	public void target(int x, int y)
	{
		var result0 = buildingsView.target(x, y, false);
		targeted = result0.targetTile;
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = buildingsView.target(x, y, true);
		if(result0.target != null)
		{
			stateHolder.setState(new BuildGUI(builder, result0.target));
		}
		else if(result0.requiresUpdate)
			update();
	}
}