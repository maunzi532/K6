package logic.gui.guis;

import building.*;
import building.blueprint.*;
import entity.*;
import levelMap.*;
import logic.editor.xstate.*;
import logic.gui.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;
import javafx.scene.paint.*;
import logic.*;
import logic.xstate.*;

public class BuildGUI extends XGUIState
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

	private XBuilder builder;
	private BuildingBlueprint blueprint;
	private LevelMap levelMap;
	private int costNum = 0;
	private int tileCostNum = 0;
	private ScrollList<RequiresFloorTiles> floorTiles;
	private ScrollList<ItemStack> required;
	private ScrollList<ItemStack> returned;

	public BuildGUI(XBuilder builder, BuildingBlueprint blueprint)
	{
		this.builder = builder;
		this.blueprint = blueprint;
	}

	@Override
	public boolean editMode()
	{
		return !(builder instanceof XHero);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		levelMap = mainState.levelMap;
		floorTiles = new ScrollList<>(0, 2, 2, 4, 2, 1);
		required = new ScrollList<>(3, 2, 2, 4, 2, 1);
		returned = new ScrollList<>(6, 2, 2, 4, 2, 1);
		update();
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
		return 8;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	@Override
	public void update()
	{
		initTiles();
		CostBlueprint cost = blueprint.constructionBlueprint.blueprints.get(costNum).get(tileCostNum);
		floorTiles.elements = cost.requiredFloorTiles;
		required.elements = cost.required.items;
		returned.elements = cost.refundable.items;
		floorTiles.update();
		required.update();
		returned.update();
		floorTiles.draw(tiles, this::itemView0);
		required.draw(tiles, this::itemView1);
		returned.draw(tiles, this::itemView2);
		setFilledTile(textTiles);
		setFilledTile(textRequired);
		setFilledTile(textReturned);
		setFilledTile(prev);
		setFilledTile(next);
		setFilledTile(lessTiles);
		setFilledTile(moreTiles);
		if(builder.tryBuildingCosts(cost, CommitType.ROLLBACK).isPresent())
			setFilledTile(buildA);
		else
			setFilledTile(build);
	}

	private GuiTile[] itemView0(RequiresFloorTiles rft)
	{
		return new GuiTile[]
				{
						new GuiTile("R " + rft.minRange + " - " + rft.maxRange),
						new GuiTile(rft.floorTileType.name() + " x" + rft.amount)
				};
	}

	private GuiTile[] itemView1(ItemStack stack)
	{
		ItemView itemView = builder.viewRecipeItem(stack.item);
		Color color = itemView.base >= stack.count || itemView.base == -1 ? Color.CYAN : null;
		return new GuiTile[]
				{
						new GuiTile((itemView.base == -1 ? "-" : itemView.base) + " / " + stack.count, null, false, color),
						new GuiTile(null, itemView.item.image(), false, color)
				};
	}

	private GuiTile[] itemView2(ItemStack stack)
	{
		return new GuiTile[]
				{
						new GuiTile(String.valueOf(stack.count)),
						new GuiTile(null, stack.item.image(), false, null)
				};
	}

	@Override
	public void target(int x, int y)
	{
		var result0 = floorTiles.target(x, y, false);
		if(result0.inside)
		{
			targeted = result0.targetTile;
			return;
		}
		var result1 = required.target(x, y, false);
		if(result1.inside)
		{
			targeted = result1.targetTile;
			return;
		}
		var result2 = returned.target(x, y, false);
		if(result2.inside)
		{
			targeted = result2.targetTile;
			return;
		}
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
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = floorTiles.target(x, y, true);
		var result1 = required.target(x, y, true);
		var result2 = returned.target(x, y, true);
		if(result0.requiresUpdate || result1.requiresUpdate || result2.requiresUpdate)
			update();
		if(!result0.inside && !result1.inside && !result2.inside)
		{
			List<List<CostBlueprint>> costBlueprints = blueprint.constructionBlueprint.blueprints;
			CostBlueprint cost = costBlueprints.get(costNum).get(tileCostNum);
			if(costNum > 0 && prev.contains(x, y))
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
				Optional<ItemList> refundable = builder.tryBuildingCosts(cost, CommitType.COMMIT);
				if(refundable.isPresent())
				{
					if(builder instanceof XHero)
					{
						((XHero) builder).takeAp(1);
						((XHero) builder).mainActionTaken();
					}
					builder.buildBuilding(levelMap, cost, refundable.get(), blueprint);
					if(builder instanceof XHero)
						stateHolder.setState(NoneState.INSTANCE);
					else
						stateHolder.setState(EditingState.INSTANCE);
				}
			}
		}
	}
}