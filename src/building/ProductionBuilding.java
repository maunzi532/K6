package building;

import arrow.*;
import building.blueprint.*;
import geom.hex.*;
import item.inv.*;
import item.inv.transport.DoubleInv;
import java.util.*;

public class ProductionBuilding implements Building, DoubleInv
{
	public BuildingBlueprint blueprint;
	private Hex location;
	private SlotInv inputInv;
	private SlotInv outputInv;
	private List<Recipe> recipes;
	public int lastViewedRecipeNum = 0;

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
			if(inputInv.tryProvide(recipe.required, false, CommitType.LEAVE).isPresent())
			{
				if(outputInv.tryAdd(recipe.results, false, CommitType.COMMIT))
				{
					inputInv.commit();
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