package building.adv;

import arrow.*;
import building.blueprint.*;
import geom.f1.*;
import item.inv.*;
import java.util.*;
import levelMap.*;
import logic.xstate.*;

public class ProcessInv implements BuildingFunction
{
	private final String name;
	private final Inv inputInv;
	private final Inv outputInv;
	private final List<Recipe> recipes;
	private final List<Tile> claimed;
	public int lastViewedRecipeNum;

	public ProcessInv(String name, Inv inputInv, Inv outputInv, List<Recipe> recipes, List<Tile> claimed)
	{
		this.name = name;
		this.inputInv = inputInv;
		this.outputInv = outputInv;
		this.recipes = recipes;
		this.claimed = claimed;
		lastViewedRecipeNum = 0;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public boolean playerTradeable(boolean levelStarted)
	{
		return true;
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
	public void afterTrading(){}

	@Override
	public void productionPhase(LevelMap levelMap, CostBlueprint costBlueprint, Tile location)
	{
		if(canWork(levelMap, costBlueprint, location, false))
		{
			for(Recipe recipe : recipes)
			{
				if(inputInv.tryProvide(recipe.required, false, CommitType.LEAVE).isPresent())
				{
					if(outputInv.tryAdd(recipe.results, false, CommitType.COMMIT))
					{
						inputInv.commit();
						levelMap.addArrow(ShineArrow.factory(location, null,
								ProductionPhaseState.ARROW_TIME, false, recipe.results.items.get(0).item.image()));
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
	public void transportPhase(LevelMap levelMap){}

	@Override
	public void afterTransport()
	{
		inputInv.commit();
		outputInv.commit();
	}

	@Override
	public void loadConnect(LevelMap levelMap, XBuilding connectTo)
	{
		for(Tile tile : claimed)
		{
			levelMap.addOwner(tile, connectTo);
		}
	}

	public void claimFloor(LevelMap levelMap, CostBlueprint costBlueprint, Tile location, XBuilding connectTo)
	{
		for(RequiresFloorTiles rft : costBlueprint.requiredFloorTiles)
		{
			int count = 0;
			for(Tile t1 : levelMap.y1.range(location, rft.minRange, rft.maxRange))
			{
				if(okTile(t1, levelMap, rft, true))
				{
					levelMap.addOwner(t1, connectTo);
					claimed.add(t1);
					count++;
					if(count >= rft.amount)
						break;
				}
			}
		}
	}

	public boolean canWork(LevelMap levelMap, CostBlueprint costBlueprint, Tile location, boolean unclaimed)
	{
		return costBlueprint.requiredFloorTiles.stream().noneMatch(rft -> levelMap.y1.range(location, rft.minRange, rft.maxRange).stream()
				.filter(e -> okTile(e, levelMap, rft, unclaimed)).count() < rft.amount);
	}

	private boolean okTile(Tile t1, LevelMap levelMap, RequiresFloorTiles rft, boolean unclaimed)
	{
		return levelMap.getFloor(t1) != null && levelMap.getFloor(t1).type == rft.floorTileType
				&& ((unclaimed && levelMap.getOwner(t1) == null) || levelMap.getOwner(t1) == this);
	}

	public void toggleTarget(Tile target, LevelMap levelMap, XBuilding connectTo)
	{
		MBuilding owner = levelMap.getOwner(target);
		if(owner == connectTo)
		{
			levelMap.removeOwner(target);
			claimed.remove(target);
		}
		else if(owner == null)
		{
			levelMap.addOwner(target, connectTo);
			claimed.add(target);
		}
	}
}