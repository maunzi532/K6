package logic.gui.guis;

import building.blueprint.*;
import entity.*;
import file.*;
import logic.gui.*;
import javafx.scene.input.*;
import levelMap.*;
import logic.*;
import logic.xstate.*;

public class SelectBuildingGUI extends NGUIState implements InvGUI
{
	private static final CTile textInv = new CTile(2, 0, new GuiTile("Buildings"), 2, 1);

	private final XHero builder;
	private BlueprintCache<BuildingBlueprint> blueprintCache;
	private String[] names;
	private InvGUIPart buildingsView;
	private BuildingBlueprint chosen = null;

	public SelectBuildingGUI(XHero builder)
	{
		this.builder = builder;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfo(builder.standardSideInfo(), null);
		blueprintCache = mainState.buildingBlueprintCache;
		names = blueprintCache.allNames().toArray(String[]::new);
		buildingsView = new InvGUIPart(0, 0, 1, 3, 5, 2, 1);
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
		return builder.ready(1) && mainState.levelMap.getBuilding(builder.location()) == null;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterGUIMenu(builder);
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

	private void update()
	{
		initTiles();
		buildingsView.addToGUI(names.length, this);
		setTile(textInv);
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		/*ItemView itemView = itemsView.get(index);
		tiles[x][y1] = new GuiTile(itemView.currentWithLimit());
		tiles[x + 1][y1] = new GuiTile(null, itemView.mode.image(), null);*/
		BuildingBlueprint blueprint = blueprintCache.get(names[index]);
		tiles[x][y1] = new GuiTile(blueprint.name);
		tiles[x + 1][y1] = new GuiTile(null, MBuilding.IMAGE, false, null);
	}

	@Override
	public void target(int x, int y)
	{
		if(buildingsView.target(x, y, names.length, this))
			return;
		setTargeted(CTile.NONE);
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		buildingsView.checkClick(x, y, names.length, this);
		if(chosen != null)
		{
			stateHolder.setState(new BuildGUI(builder, chosen));
		}
		else if(buildingsView.updateGUIFlag())
		{
			update();
		}
	}

	@Override
	public void onClickItem(int invID, int num, int xi, int yi)
	{
		chosen = blueprintCache.get(names[num]);
	}
}