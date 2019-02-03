package building;

import arrow.*;
import building.blueprint.*;
import hex.*;
import inv.*;
import java.util.*;

public class ProductionBuilding implements Building, DoubleInv
{
	private Hex location;
	private Inv2 inputInv;
	private Inv2 outputInv;
	private List<Recipe> recipes;

	public ProductionBuilding(Hex location, ProductionBlueprint blueprint)
	{
		this.location = location;
		inputInv = new Inv2(blueprint.inputLimits);
		outputInv = new Inv2(blueprint.outputLimits);
		recipes = blueprint.recipes;
	}

	@Override
	public Hex location()
	{
		return location;
	}

	@Override
	public Inv2 inputInv()
	{
		return inputInv;
	}

	@Override
	public Inv2 outputInv()
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
	public void productionPhase(CanAddArrows canAddArrows)
	{
		for(Recipe recipe : recipes)
		{
			if(inputInv.canDecrease(recipe.required) && outputInv.canIncrease(recipe.results))
			{
				inputInv.decrease(recipe.required);
				outputInv.increase(recipe.results);
				return;
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