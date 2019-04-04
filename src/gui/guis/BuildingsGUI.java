package gui.guis;

import entity.hero.*;
import levelMap.MBuilding;
import building.blueprint.BuildingBlueprint;
import file.BlueprintCache;
import gui.*;
import logic.*;
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
		tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), null);*/
		BuildingBlueprint blueprint = BuildingBlueprint.get(blueprintCache, names[index]);
		tiles[x][y1] = new GuiTile(blueprint.name);
		tiles[x + 1][y1] = new GuiTile(null, MBuilding.IMAGE, null);
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
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		buildingsView.checkClick(x, y, names.length, this);
		if(chosen != null)
		{
			//stateControl.stateInfo[3] = chosen;
			stateControl.setState(new BuildState(builder, chosen));
			return true;
		}
		if(buildingsView.updateGUIFlag())
			update();
		return false;
	}

	@Override
	public void onClickItem(int invID, int num, int xi, int yi)
	{
		chosen = BuildingBlueprint.get(blueprintCache, names[num]);
	}
}