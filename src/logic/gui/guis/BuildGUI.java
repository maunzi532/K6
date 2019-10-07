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
	private List<List<CostBlueprint>> costBlueprints;
	private LevelMap levelMap;
	private int costNum = 0;
	private int tileCostNum = 0;
	private ScrollList<RequiresFloorTiles> floorTiles;
	private ScrollList<ItemStack> required;
	private ScrollList<ItemStack> returned;
	private CElement buildElement;

	public BuildGUI(XBuilder builder, BuildingBlueprint blueprint)
	{
		this.builder = builder;
		this.blueprint = blueprint;
		costBlueprints = blueprint.constructionBlueprint.blueprints;
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
				this::itemView0, null, null);
		elements.add(floorTiles);
		required = new ScrollList<>(3, 2, 2, 4, 2, 1, null,
				this::itemView1, null, null);
		elements.add(required);
		returned = new ScrollList<>(6, 2, 2, 4, 2, 1, null,
				this::itemView2, null, null);
		elements.add(returned);
		elements.add(new CElement(textTiles));
		elements.add(new CElement(textRequired));
		elements.add(new CElement(textReturned));
		elements.add(new CElement(prev, true, null, null, this::clickPrev));
		elements.add(new CElement(next, true, null, null, this::clickNext));
		elements.add(new CElement(lessTiles, true, null, null, this::clickLessTiles));
		elements.add(new CElement(moreTiles, true, null, null, this::clickMoreTiles));
		buildElement = new CElement(build, true, null, null, () -> clickBuild(mainState));
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
		CostBlueprint cost = blueprint.constructionBlueprint.blueprints.get(costNum).get(tileCostNum);
		floorTiles.elements = cost.requiredFloorTiles;
		required.elements = cost.required.items;
		returned.elements = cost.refundable.items;
		if(builder.tryBuildingCosts(cost, CommitType.ROLLBACK).isPresent())
			buildElement.fillTile = new GuiTile("Build", null, false, Color.CYAN);
		else
			buildElement.fillTile = new GuiTile("Build");
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
				mainState.stateHolder.setState(NoneState.INSTANCE);
			else
				mainState.stateHolder.setState(EditingState.INSTANCE);
		}
	}
}