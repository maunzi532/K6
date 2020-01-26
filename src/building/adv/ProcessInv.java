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
	public int lastViewedRecipeNum;

	public ProcessInv(String name, Inv inputInv, Inv outputInv, List<Recipe> recipes)
	{
		this.name = name;
		this.inputInv = inputInv;
		this.outputInv = outputInv;
		this.recipes = recipes;
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
}