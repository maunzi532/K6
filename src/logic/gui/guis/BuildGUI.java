package logic.gui.guis;

import building.blueprint.*;
import doubleinv.*;
import entity.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;
import levelmap.*;
import logic.*;
import logic.editor.xstate.*;
import logic.gui.*;
import logic.xstate.*;
import text.*;

public final class BuildGUI extends XGUIState
{
	private static final CTile textTiles = new CTile(0, 1, new GuiTile("gui.build.requiredfloor"), 2, 1);
	private static final CTile textRequired = new CTile(3, 1, new GuiTile("gui.build.requireditem"), 2, 1);
	private static final CTile textReturned = new CTile(6, 1, new GuiTile("gui.build.returneditem"), 2, 1);
	private static final CTile prev = new CTile(2, 1);
	private static final CTile next = new CTile(5, 1);
	private static final CTile lessTiles = new CTile(0, 0);
	private static final CTile moreTiles = new CTile(1, 0);
	private static final CTile build = new CTile(6, 0, 2, 1);

	private final XBuilder builder;
	private final BuildingBlueprint blueprint;
	private final List<List<CostBlueprint>> costBlueprints;
	private LevelMap levelMap;
	private int costNum;
	private int tileCostNum;
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
		costBlueprints = blueprint.construction().blueprints();
	}

	@Override
	public boolean editMode()
	{
		return !(builder instanceof XCharacter);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		levelMap = mainState.levelMap();
		floorTiles = new ScrollList<>(0, 2, 2, 4, 2, 1, null,
				BuildGUI::itemView0, null);
		elements.add(floorTiles);
		required = new ScrollList<>(3, 2, 2, 4, 2, 1, null,
				this::itemView1, null);
		elements.add(required);
		returned = new ScrollList<>(6, 2, 2, 4, 2, 1, null,
				BuildGUI::itemView2, null);
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
		buildElement = new CElement(build, true, null, () -> clickBuild(mainState.stateHolder()));
		elements.add(buildElement);
		update();
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
		List<List<CostBlueprint>> blueprints = blueprint.construction().blueprints();
		CostBlueprint cost = blueprints.get(costNum).get(tileCostNum);
		floorTiles.elements = cost.requiredFloorTiles();
		required.elements = cost.required().items;
		returned.elements = cost.refundable().items;
		prevElement.fillTile = activeIf("gui.build.previous", costNum > 0, "gui.build.more");
		nextElement.fillTile = activeIf("gui.build.next", costNum < blueprints.size() - 1, "gui.build.more");
		lessTilesElement.fillTile = activeIf("gui.build.lesstiles", tileCostNum > 0, "gui.build.more");
		moreTilesElement.fillTile = activeIf("gui.build.moretiles", tileCostNum < blueprints.get(costNum).size() - 1, "gui.build.more");
		buildElement.fillTile = activeIf("gui.build.build", builder.tryBuildingCosts(cost.refundable(), cost.costs(), CommitType.ROLLBACK).isPresent(), "gui.background.active");
	}

	private static GuiTile activeIf(String text, boolean active, String color)
	{
		if(active)
			return new GuiTile(text, null, false, color);
		else
			return new GuiTile(text);
	}

	private static GuiTile[] itemView0(RequiresFloorTiles rft)
	{
		return new GuiTile[]
				{
						new GuiTile(new ArgsText("gui.build.floor.range", rft.minRange(), rft.maxRange())),
						new GuiTile(new ArgsText("gui.build.floor.required", rft.floorTileType().name(), rft.amount()))
				};
	}

	private GuiTile[] itemView1(ItemStack stack)
	{
		ItemView itemView = builder.viewRecipeItem(stack.item);
		String color = itemView.base >= stack.count || itemView.base == -1 ? "gui.background.active" : null;
		return new GuiTile[]
				{
						new GuiTile(itemView.base == -1 ? new ArgsText("gui.build.nocost", stack.count)
								: new ArgsText("gui.build.cost", itemView.base, stack.count), null, false, color),
						new GuiTile((XText) null, itemView.item.image(), false, color)
				};
	}

	private static GuiTile[] itemView2(ItemStack stack)
	{
		return new GuiTile[]
				{
						new GuiTile(new ArgsText("i", stack.count)),
						new GuiTile((XText) null, stack.item.image(), false, null)
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

	private void clickBuild(XStateHolder stateHolder)
	{
		CostBlueprint cost = costBlueprints.get(costNum).get(tileCostNum);
		Optional<ItemList> refundable = builder.tryBuildingCosts(cost.refundable(), cost.costs(), CommitType.COMMIT);
		if(refundable.isPresent())
		{
			if(builder instanceof XCharacter character)
			{
				character.resources().action(true, 1);
			}
			levelMap.buildBuilding(builder, cost, refundable.get(), blueprint);
			if(builder instanceof XCharacter)
				stateHolder.setState(NoneState.INSTANCE);
			else
				stateHolder.setState(EditingState.INSTANCE);
		}
	}
}