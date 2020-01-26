package building.adv;

import arrow.*;
import building.blueprint.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.util.*;
import levelMap.*;
import logic.xstate.*;

public class ProcessInv implements BuildingFunction
{
	private final String name;
	private final Inv inputInv;
	private final Inv outputInv;
	private final List<Recipe> recipes;
	public int lastViewedRecipeNum;

	public ProcessInv(String name, Inv inputInv, Inv outputInv, List<Recipe> recipes)
	{
		this.name = name;
		this.inputInv = inputInv;
		this.outputInv = outputInv;
		this.recipes = recipes;
		lastViewedRecipeNum = 0;
	}

	public ProcessInv(String name, ProductionBlueprint blueprint)
	{
		this.name = name;
		inputInv = new SlotInv(blueprint.inputLimits);
		outputInv = new SlotInv(blueprint.outputLimits);
		recipes = blueprint.recipes;
		lastViewedRecipeNum = 0;
	}

	public List<Recipe> recipes()
	{
		return recipes;
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
	public void productionPhase(boolean canWork, LevelMap levelMap, Tile location)
	{
		if(canWork)
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
	public void transportPhase(boolean canWork, LevelMap levelMap){}

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
	public void loadConnect(LevelMap levelMap, XBuilding connectTo){}

	public ProcessInv(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		name = data.get("Name").asText();
		inputInv = new SlotInv((JrsObject) data.get("InputInv"), itemLoader);
		outputInv = new SlotInv((JrsObject) data.get("OutputInv"), itemLoader);
		recipes = new ArrayList<>();
		((JrsArray) data.get("Recipes")).elements().forEachRemaining(e -> recipes.add(new Recipe((JrsObject) e, itemLoader)));
		lastViewedRecipeNum = 0;
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1)
			throws IOException
	{
		a1 = a1.put("Name", name);
		a1 = inputInv.save(a1.startObjectField("InputInv"), itemLoader).end();
		a1 = outputInv.save(a1.startObjectField("OutputInv"), itemLoader).end();
		var a2 = a1.startArrayField("Recipes");
		for(Recipe recipe : recipes)
		{
			a2 = recipe.save(a2.startObject(), itemLoader).end();
		}
		return a2.end();
	}
}