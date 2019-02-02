package building;

import arrow.*;
import building.blueprint.*;
import inv.*;
import java.util.*;

public class ProductionBuilding implements Building, DoubleInv
{
	private Inv2 inputInv;
	private Inv2 outputInv;
	private List<Recipe> recipes;

	public ProductionBuilding(ProductionBlueprint blueprint)
	{
		inputInv = new Inv2(blueprint.inputLimits);
		outputInv = new Inv2(blueprint.outputLimits);
		recipes = blueprint.recipes;
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
		System.out.println(outputInv.getCurrent());
	}

	@Override
	public void afterTransport()
	{
		inputInv.commit();
		outputInv.commit();
	}
}