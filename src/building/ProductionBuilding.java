package building;

import arrow.*;
import building.blueprint.*;
import hex.*;
import inv.*;
import java.util.*;

public class ProductionBuilding implements Building, DoubleInv
{
	private BuildingBlueprint blueprint;
	private Hex location;
	private SlotInv inputInv;
	private SlotInv outputInv;
	private List<Recipe> recipes;

	public ProductionBuilding(Hex location, BuildingBlueprint blueprint)
	{
		this.location = location;
		this.blueprint = blueprint;
		inputInv = new SlotInv(blueprint.productionBlueprint.inputLimits);
		outputInv = new SlotInv(blueprint.productionBlueprint.outputLimits);
		recipes = blueprint.productionBlueprint.recipes;
	}

	public String name()
	{
		return blueprint.name;
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
	public Hex location()
	{
		return location;
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
	public void productionPhase(CanAddArrows canAddArrows)
	{
		for(Recipe recipe : recipes)
		{
			if(inputInv.tryDecrease(recipe.required).isPresent())
			{
				if(outputInv.tryIncrease(recipe.results))
				{
					inputInv.commit();
					outputInv.commit();
					return;
				}
				else
				{
					inputInv.rollback();
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