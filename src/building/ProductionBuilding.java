package building;

import arrow.*;
import building.blueprint.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import item.inv.transport.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;

public class ProductionBuilding extends Buildable implements DoubleInv
{
	private static final int ARROW_TIME = 30;

	private SlotInv inputInv;
	private SlotInv outputInv;
	private List<Recipe> recipes;
	private List<Tile> claimed;
	public int lastViewedRecipeNum = 0;

	public ProductionBuilding(Tile location, BuildingBlueprint blueprint)
	{
		super(location, blueprint.constructionBlueprint.blueprints.get(0).get(0),
				blueprint.constructionBlueprint.blueprints.get(0).get(0).refundable, blueprint.name);
		inputInv = new SlotInv(blueprint.productionBlueprint.inputLimits);
		outputInv = new SlotInv(blueprint.productionBlueprint.outputLimits);
		recipes = blueprint.productionBlueprint.recipes;
		claimed = new ArrayList<>();
	}

	public ProductionBuilding(Tile location, CostBlueprint costs, ItemList refundable, BuildingBlueprint blueprint)
	{
		super(location, costs, refundable, blueprint.name);
		inputInv = new SlotInv(blueprint.productionBlueprint.inputLimits);
		outputInv = new SlotInv(blueprint.productionBlueprint.outputLimits);
		recipes = blueprint.productionBlueprint.recipes;
		claimed = new ArrayList<>();
	}

	public void claimFloor(LevelMap levelMap)
	{
		for(RequiresFloorTiles rft : getCosts().requiredFloorTiles)
		{
			int count = 0;
			for(Tile t1 : levelMap.y1.range(location(), rft.minRange, rft.maxRange))
			{
				if(okTile(t1, levelMap, rft, true))
				{
					levelMap.addOwner(t1, this);
					claimed.add(t1);
					count++;
					if(count >= rft.amount)
						break;
				}
			}
		}
	}

	public boolean canWork(LevelMap levelMap, boolean unclaimed)
	{
		return getCosts().requiredFloorTiles.stream().noneMatch(rft -> levelMap.y1.range(location(), rft.minRange, rft.maxRange).stream()
						.filter(e -> okTile(e, levelMap, rft, unclaimed)).count() < rft.amount);
	}

	private boolean okTile(Tile t1, LevelMap levelMap, RequiresFloorTiles rft, boolean unclaimed)
	{
		return levelMap.getFloor(t1) != null && levelMap.getFloor(t1).type == rft.floorTileType
				&& ((unclaimed && levelMap.getOwner(t1) == null) || levelMap.getOwner(t1) == this);
	}

	public Map<Tile, MarkType> floors(LevelMap levelMap)
	{
		Map<Tile, MarkType> floors = new HashMap<>();
		for(RequiresFloorTiles rft : getCosts().requiredFloorTiles)
		{
			floors.putAll(levelMap.y1.range(location(), rft.minRange, rft.maxRange).stream()
					.filter(e -> levelMap.getFloor(e) != null && levelMap.getFloor(e).type == rft.floorTileType)
					.collect(Collectors.toMap(e -> e, e -> levelMap.getOwner(e) == this ? MarkType.ON
							: levelMap.getOwner(e) != null ? MarkType.BLOCKED : MarkType.OFF)));
		}
		return floors;
	}

	public void toggleTarget(Tile target, LevelMap levelMap)
	{
		MBuilding owner = levelMap.getOwner(target);
		if(owner == this)
		{
			levelMap.removeOwner(target);
			claimed.remove(target);
		}
		else if(owner == null)
		{
			levelMap.addOwner(target, this);
			claimed.add(target);
		}
	}

	public List<Tile> getClaimed()
	{
		return claimed;
	}

	public SlotInv getInputInv()
	{
		return inputInv;
	}

	public SlotInv getOutputInv()
	{
		return outputInv;
	}

	public List<Recipe> getRecipes()
	{
		return recipes;
	}

	@Override
	public Inv inputInv()
	{
		return inputInv;
	}

	@Override
	public Inv outputInv()
	{
		return outputInv;
	}

	@Override
	public void productionPhase(LevelMap levelMap)
	{
		if(canWork(levelMap, false))
		{
			for(Recipe recipe : recipes)
			{
				if(inputInv.tryProvide(recipe.required, false, CommitType.LEAVE).isPresent())
				{
					if(outputInv.tryAdd(recipe.results, false, CommitType.COMMIT))
					{
						inputInv.commit();
						levelMap.addArrow(XArrow.factory(location(), null, ARROW_TIME, false,
								recipe.results.items.get(0).item.image(), true));
						return;
					}
					else
					{
						inputInv.rollback();
					}
				}
			}
		}
	}

	@Override
	public void afterProduction()
	{
		inputInv.commit();
		outputInv.commit();
	}

	@Override
	public void afterTransport()
	{
		inputInv.commit();
		outputInv.commit();
	}
}