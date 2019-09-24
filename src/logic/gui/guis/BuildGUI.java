package logic.gui.guis;

import building.blueprint.*;
import entity.*;
import logic.gui.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;
import javafx.scene.paint.*;
import logic.*;
import logic.xstate.*;

public class BuildGUI extends XGUIState implements InvGUI
{
	private static final CTile textTiles = new CTile(0, 1, new GuiTile("Floor Req."), 2, 1);
	private static final CTile textRequired = new CTile(3, 1, new GuiTile("Required"), 2, 1);
	private static final CTile textReturned = new CTile(6, 1, new GuiTile("When removed"), 2, 1);
	private static final CTile prev = new CTile(2, 1, new GuiTile("Prev"));
	private static final CTile next = new CTile(5, 1, new GuiTile("Next"));
	private static final CTile lessTiles = new CTile(0, 0, new GuiTile("Less Tiles"));
	private static final CTile moreTiles = new CTile(1, 0, new GuiTile("More Tiles"));
	private static final CTile build = new CTile(6, 0, new GuiTile("Build"), 2, 1);
	private static final CTile buildA = new CTile(6, 0, new GuiTile("Build", null, false, Color.CYAN), 2, 1);

	private XHero character;
	private BuildingBlueprint blueprint;
	private int costNum = 0;
	private int tileCostNum = 0;
	private InvGUIPart floorTiles;
	private InvGUIPart required;
	private InvGUIPart returned;

	public BuildGUI(XHero character, BuildingBlueprint blueprint)
	{
		this.character = character;
		this.blueprint = blueprint;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		floorTiles = new InvGUIPart(0, 0, 2, 1, 4, 2, 1);
		required = new InvGUIPart(1, 3, 2, 1, 4, 2, 1);
		returned = new InvGUIPart(2, 6, 2, 1, 4, 2, 1);
		update();
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterGUIMenu(character);
	}

	@Override
	public int xw()
	{
		return 8;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	public void update()
	{
		initTiles();
		CostBlueprint cost = blueprint.constructionBlueprint.blueprints.get(costNum).get(tileCostNum);
		floorTiles.addToGUI(cost.requiredFloorTiles.size(), this);
		required.addToGUI(cost.required.items.size(), this);
		returned.addToGUI(cost.refundable.items.size(), this);
		setTile(textTiles);
		setTile(textRequired);
		setTile(textReturned);
		setTile(prev);
		setTile(next);
		setTile(lessTiles);
		setTile(moreTiles);
		if(character.tryBuildingCosts(cost, CommitType.ROLLBACK).isPresent())
			setTile(buildA);
		else
			setTile(build);
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		CostBlueprint cost = blueprint.constructionBlueprint.blueprints.get(costNum).get(tileCostNum);
		if(invID == 0)
		{
			RequiresFloorTiles rft = cost.requiredFloorTiles.get(index);
			tiles[x][y1] = new GuiTile("R " + rft.minRange + " - " + rft.maxRange);
			tiles[x + 1][y1] = new GuiTile(rft.floorTileType.name() + " x" + rft.amount);
		}
		else if(invID == 1)
		{
			ItemStack items = cost.required.items.get(index);
			ItemView itemView = character.outputInv().viewRecipeItem(items.item);
			Color color = itemView.base >= items.count ? Color.CYAN : null;
			tiles[x][y1] = new GuiTile(itemView.base + " / " + items.count, null, false, color);
			tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), false, color);
		}
		else
		{
			ItemStack items = cost.refundable.items.get(index);
			tiles[x][y1] = new GuiTile(String.valueOf(items.count));
			tiles[x + 1][y1] = new GuiTile(null, items.item.image(), false, null);
		}
	}

	@Override
	public void target(int x, int y)
	{
		CostBlueprint cost = blueprint.constructionBlueprint.blueprints.get(costNum).get(tileCostNum);
		if(floorTiles.target(x, y, cost.requiredFloorTiles.size(), this))
			return;
		if(required.target(x, y, cost.required.items.size(), this))
			return;
		if(returned.target(x, y, cost.refundable.items.size(), this))
			return;
		if(prev.contains(x, y))
			setTargeted(prev);
		else if(next.contains(x, y))
			setTargeted(next);
		else if(lessTiles.contains(x, y))
			setTargeted(lessTiles);
		else if(moreTiles.contains(x, y))
			setTargeted(moreTiles);
		else if(build.contains(x, y))
			setTargeted(build);
		else
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
		List<List<CostBlueprint>> costBlueprints = blueprint.constructionBlueprint.blueprints;
		CostBlueprint cost = costBlueprints.get(costNum).get(tileCostNum);
		floorTiles.checkClick(x, y, cost.requiredFloorTiles.size(), this);
		required.checkClick(x, y, cost.required.items.size(), this);
		returned.checkClick(x, y, cost.refundable.items.size(), this);
		if(floorTiles.updateGUIFlag() | required.updateGUIFlag() | returned.updateGUIFlag())
			update();
		else if(costNum > 0 && prev.contains(x, y))
		{
			costNum--;
			tileCostNum = 0;
			update();
		}
		else if(costNum < costBlueprints.size() - 1 && next.contains(x, y))
		{
			costNum++;
			tileCostNum = 0;
			update();
		}
		else if(tileCostNum > 0 && lessTiles.contains(x, y))
		{
			tileCostNum--;
			update();
		}
		else if(tileCostNum < costBlueprints.get(costNum).size() - 1 && moreTiles.contains(x, y))
		{
			tileCostNum++;
			update();
		}
		else if(build.contains(x, y))
		{
			Optional<ItemList> refundable = character.tryBuildingCosts(cost, CommitType.COMMIT);
			if(refundable.isPresent())
			{
				character.takeAp(1);
				character.mainActionTaken();
				character.buildBuilding(cost, refundable.get(), blueprint);
				stateHolder.setState(NoneState.INSTANCE);
			}
		}
	}
}