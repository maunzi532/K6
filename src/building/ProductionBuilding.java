package building;

import arrow.*;
import building.blueprint.*;
import geom.hex.*;
import item.*;
import item.inv.*;
import item.inv.transport.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;

public class ProductionBuilding extends Buildable implements DoubleInv
{
	private SlotInv inputInv;
	private SlotInv outputInv;
	private List<Recipe> recipes;
	public int lastViewedRecipeNum = 0;

	public ProductionBuilding(Hex location, BuildingBlueprint blueprint)
	{
		super(location, blueprint.constructionBlueprint.blueprints.get(0).get(0),
				blueprint.constructionBlueprint.blueprints.get(0).get(0).refundable, blueprint.name);
		inputInv = new SlotInv(blueprint.productionBlueprint.inputLimits);
		outputInv = new SlotInv(blueprint.productionBlueprint.outputLimits);
		recipes = blueprint.productionBlueprint.recipes;
	}

	public ProductionBuilding(Hex location, CostBlueprint costs, ItemList refundable, BuildingBlueprint blueprint)
	{
		super(location, costs, refundable, blueprint.name);
		inputInv = new SlotInv(blueprint.productionBlueprint.inputLimits);
		outputInv = new SlotInv(blueprint.productionBlueprint.outputLimits);
		recipes = blueprint.productionBlueprint.recipes;
	}

	public void claimFloor(LevelMap levelMap)
	{
		for(RequiresFloorTiles rft : getCosts().requiredFloorTiles)
		{
			int count = 0;
			for(Hex hex : location().range(rft.minRange, rft.maxRange))
			{
				if(okTile(hex, levelMap, rft, true))
				{
					levelMap.addOwner(hex, this);
					count++;
					if(count >= rft.amount)
						break;
				}
			}
		}
	}

	public boolean canWork(LevelMap levelMap, boolean unclaimed)
	{
		return getCosts().requiredFloorTiles.stream().noneMatch(rft -> location().range(rft.minRange, rft.maxRange).stream()
						.filter(e -> okTile(e, levelMap, rft, unclaimed)).count() < rft.amount);
	}

	private boolean okTile(Hex hex, LevelMap levelMap, RequiresFloorTiles rft, boolean unclaimed)
	{
		return levelMap.getFloor(hex).type == rft.floorTileType
				&& ((unclaimed && levelMap.getOwner(hex) == null) || levelMap.getOwner(hex) == this);
	}

	public Map<Hex, MarkType> floors(LevelMap levelMap)
	{
		Map<Hex, MarkType> floors = new HashMap<>();
		for(RequiresFloorTiles rft : getCosts().requiredFloorTiles)
		{
			floors.putAll(location().range(rft.minRange, rft.maxRange).stream().filter(e -> levelMap.getFloor(e).type == rft.floorTileType)
					.collect(Collectors.toMap(e -> e, e -> levelMap.getOwner(e) == this ? MarkType.ON
							: levelMap.getOwner(e) != null ? MarkType.BLOCKED : MarkType.OFF)));
		}
		return floors;
	}

	public void toggleTarget(Hex target, LevelMap levelMap)
	{
		MBuilding owner = levelMap.getOwner(target);
		if(owner == this)
			levelMap.removeOwner(target);
		else if(owner == null)
			levelMap.addOwner(target, this);
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
	public int inputPriority()
	{
		return 0;//high
	}

	@Override
	public int outputPriority()
	{
		return 0;//high
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
						levelMap.addArrow(new VisualArrow(location(), location(), ArrowMode.TARROW, 30,
								recipe.results.items.get(0).item.image()));
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