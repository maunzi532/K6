package building;

import arrow.*;
import building.blueprint.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import building.transport.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.xstate.*;

public class ProductionBuilding extends Buildable implements DoubleInv
{
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
	public DoubleInvType type()
	{
		return DoubleInvType.BUILDING;
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
						levelMap.addArrow(XArrow.factory(location(), null, ProductionPhaseState.ARROW_TIME, false,
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

	@Override
	public void loadConnect(LevelMap levelMap)
	{
		for(Tile tile : claimed)
		{
			levelMap.addOwner(tile, this);
		}
	}

	public ProductionBuilding(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		super(data, itemLoader, y1);
		inputInv = new SlotInv((JrsObject) data.get("InputInv"), itemLoader);
		outputInv = new SlotInv((JrsObject) data.get("OutputInv"), itemLoader);
		recipes = new ArrayList<>();
		((JrsArray) data.get("Recipes")).elements().forEachRemaining(e -> recipes.add(new Recipe((JrsObject) e, itemLoader)));
		claimed = new ArrayList<>();
		((JrsArray) data.get("Claimed")).elements().forEachRemaining(e ->
				claimed.add(y1.create2(((JrsNumber) ((JrsObject) e).get("sx")).getValue().intValue(),
						((JrsNumber) ((JrsObject) e).get("sy")).getValue().intValue())));
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		a1 = super.save(a1, itemLoader, y1);
		a1 = inputInv.save(a1.startObjectField("InputInv"), itemLoader).end();
		a1 = outputInv.save(a1.startObjectField("OutputInv"), itemLoader).end();
		var a2 = a1.startArrayField("Recipes");
		for(Recipe recipe : recipes)
		{
			a2 = recipe.save(a2.startObject(), itemLoader).end();
		}
		var a3 = a2.end().startArrayField("Claimed");
		for(Tile tile : claimed)
		{
			a3 = a3.startObject().put("sx", y1.sx(tile)).put("sy", y1.sy(tile)).end();
		}
		return a3.end();
	}
}