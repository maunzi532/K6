package logic.gui.guis;

import building.blueprint.*;
import doubleinv.*;
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
	private static final CTile prev = new CTile(2, 1);
	private static final CTile next = new CTile(5, 1);
	private static final CTile lessTiles = new CTile(0, 0);
	private static final CTile moreTiles = new CTile(1, 0);
	private static final CTile build = new CTile(6, 0, 2, 1);

	private final XBuilder builder;
	private final BuildingBlueprint blueprint;
	private final List<List<CostBlueprint>> costBlueprints;
	private LevelMap levelMap;
	private int costNum = 0;
	private int tileCostNum = 0;
	private ScrollList<RequiresFloorTiles> floorTiles;
	private ScrollList<ItemStack> required;
	private ScrollList<ItemStack> returned;
	private CElement prevElement;
	private CElement nextElement;
	private CElement lessTilesElement;
	private CElement moreTilesElement;
	private CElement buildElement;

	public BuildGUI(XBuilder builder, BuildingBlueprint blueprint)
	{
		this.builder = builder;
		this.blueprint = blueprint;
		costBlueprints = blueprint.constructionBlueprint().blueprints();
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
		floorTiles = new ScrollList<>(0, 2, 2, 4, 2, 1, null,
				this::itemView0, null);
		elements.add(floorTiles);
		required = new ScrollList<>(3, 2, 2, 4, 2, 1, null,
				this::itemView1, null);
		elements.add(required);
		returned = new ScrollList<>(6, 2, 2, 4, 2, 1, null,
				this::itemView2, null);
		elements.add(returned);
		elements.add(new CElement(textTiles));
		elements.add(new CElement(textRequired));
		elements.add(new CElement(textReturned));
		prevElement = new CElement(prev, true, null, this::clickPrev);
		elements.add(prevElement);
		nextElement = new CElement(next, true, null, this::clickNext);
		elements.add(nextElement);
		lessTilesElement = new CElement(lessTiles, true, null, this::clickLessTiles);
		elements.add(lessTilesElement);
		moreTilesElement = new CElement(moreTiles, true, null, this::clickMoreTiles);
		elements.add(moreTilesElement);
		buildElement = new CElement(build, true, null, () -> clickBuild(mainState));
		elements.add(buildElement);
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
	protected void updateBeforeDraw()
	{
		List<List<CostBlueprint>> blueprints = blueprint.constructionBlueprint().blueprints();
		CostBlueprint cost = blueprints.get(costNum).get(tileCostNum);
		floorTiles.elements = cost.requiredFloorTiles();
		required.elements = cost.required().items;
		returned.elements = cost.refundable().items;
		prevElement.fillTile = activeIf("Prev", costNum > 0, Color.LIGHTCYAN);
		nextElement.fillTile = activeIf("Next", costNum < blueprints.size() - 1, Color.LIGHTCYAN);
		lessTilesElement.fillTile = activeIf("Less Tiles", tileCostNum > 0, Color.LIGHTCYAN);
		moreTilesElement.fillTile = activeIf("More Tiles", tileCostNum < blueprints.get(costNum).size() - 1, Color.LIGHTCYAN);
		buildElement.fillTile = activeIf("Build", builder.tryBuildingCosts(cost.refundable(), cost.costs(), CommitType.ROLLBACK).isPresent(), Color.CYAN);
	}

	private GuiTile activeIf(String text, boolean active, Color color)
	{
		if(active)
			return new GuiTile(text, null, false, color);
		else
			return new GuiTile(text);
	}

	private GuiTile[] itemView0(RequiresFloorTiles rft)
	{
		return new GuiTile[]
				{
						new GuiTile("R " + rft.minRange() + " - " + rft.maxRange()),
						new GuiTile(rft.floorTileType().name() + " x" + rft.amount())
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

	private void clickPrev()
	{
		if(costNum > 0)
		{
			costNum--;
			tileCostNum = 0;
		}
	}

	private void clickNext()
	{
		if(costNum < costBlueprints.size() - 1)
		{
			costNum++;
			tileCostNum = 0;
		}
	}

	private void clickLessTiles()
	{
		if(tileCostNum > 0)
		{
			tileCostNum--;
		}
	}

	private void clickMoreTiles()
	{
		if(tileCostNum < costBlueprints.get(costNum).size() - 1)
		{
			tileCostNum++;
		}
	}

	private void clickBuild(MainState mainState)
	{
		CostBlueprint cost = costBlueprints.get(costNum).get(tileCostNum);
		Optional<ItemList> refundable = builder.tryBuildingCosts(cost.refundable(), cost.costs(), CommitType.COMMIT);
		if(refundable.isPresent())
		{
			if(builder instanceof XHero)
			{
				((XHero) builder).takeAp(1);
				((XHero) builder).mainActionTaken();
			}
			levelMap.buildBuilding(builder, cost, refundable.get(), blueprint);
			if(builder instanceof XHero)
				mainState.stateHolder.setState(NoneState.INSTANCE);
			else
				mainState.stateHolder.setState(EditingState.INSTANCE);
		}
	}
}