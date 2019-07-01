package gui.guis;

import entity.*;
import levelMap.MBuilding;
import building.blueprint.BuildingBlueprint;
import file.BlueprintCache;
import gui.*;
import logic.xstate.*;

public class BuildingsGUI extends XGUI implements InvGUI
{
	private static final CTile textInv = new CTile(2, 0, new GuiTile("Buildings"), 2, 1);

	private final XHero builder;
	private final BlueprintCache<BuildingBlueprint> blueprintCache;
	private final String[] names;
	private final InvGUIPart buildingsView;
	private BuildingBlueprint chosen = null;

	public BuildingsGUI(XHero builder, BlueprintCache<BuildingBlueprint> blueprintCache)
	{
		this.builder = builder;
		this.blueprintCache = blueprintCache;
		names = blueprintCache.allNames().toArray(String[]::new);
		buildingsView = new InvGUIPart(0, 0, 1, 3, 5, 2, 1);
		update();
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
		BuildingBlueprint blueprint = BuildingBlueprint.get(blueprintCache, names[index]);
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
			stateHolder.setState(new BuildState(builder, chosen));
		}
		else if(buildingsView.updateGUIFlag())
		{
			update();
		}
	}

	@Override
	public void onClickItem(int invID, int num, int xi, int yi)
	{
		chosen = BuildingBlueprint.get(blueprintCache, names[num]);
	}
}