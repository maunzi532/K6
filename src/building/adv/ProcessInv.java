package building.adv;

import arrow.*;
import building.blueprint.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.util.*;

public class ProcessInv implements BuildingFunction
{
	public static final int ARROW_TIME = 30;

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
		inputInv = new SlotInv(blueprint.inputLimits());
		outputInv = new SlotInv(blueprint.outputLimits());
		recipes = blueprint.recipes();
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
	public void productionPhase(boolean canWork, Arrows arrows, Tile location)
	{
		if(canWork)
		{
			for(Recipe recipe : recipes)
			{
				if(inputInv.tryProvide(recipe.required(), false, CommitType.LEAVE).isPresent())
				{
					if(outputInv.tryAdd(recipe.results()))
					{
						inputInv.commit();
						arrows.addArrow(ShineArrow.factory(location, null,
								ARROW_TIME, false, recipe.results().items.get(0).item.image()));
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
	public void transportPhase(boolean canWork, Arrows arrows){}

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
	public void loadConnect(ConnectRestore cr, XBuilding connectTo){}

	public ProcessInv(JrsObject data, ItemLoader itemLoader)
	{
		name = data.get("Name").asText();
		inputInv = new SlotInv((JrsObject) data.get("InputInv"), itemLoader);
		outputInv = new SlotInv((JrsObject) data.get("OutputInv"), itemLoader);
		recipes = new ArrayList<>();
		((JrsArray) data.get("Recipes")).elements().forEachRemaining(e -> recipes.add(Recipe.create((JrsObject) e, itemLoader)));
		lastViewedRecipeNum = 0;
	}

	@Override
	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		a1.put("Name", name);
		inputInv.save(a1.startObjectField("InputInv"), itemLoader);
		outputInv.save(a1.startObjectField("OutputInv"), itemLoader);
		var a2 = a1.startArrayField("Recipes");
		for(Recipe recipe : recipes)
		{
			recipe.save(a2.startObject(), itemLoader);
		}
		a2.end();
		a1.end();
	}
}