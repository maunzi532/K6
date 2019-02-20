package building;

import arrow.*;
import building.blueprint.*;
import geom.hex.*;
import item.ItemList;
import item.inv.*;
import item.inv.transport.DoubleInv;
import java.util.*;

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
	public void productionPhase(CanAddArrows canAddArrows)
	{
		for(Recipe recipe : recipes)
		{
			if(inputInv.tryProvide(recipe.required, false, CommitType.LEAVE).isPresent())
			{
				if(outputInv.tryAdd(recipe.results, false, CommitType.COMMIT))
				{
					inputInv.commit();
					canAddArrows.addArrow(new VisualArrow(location(),
							location(), ArrowMode.TARROW, 30, recipe.results.items.get(0).item.image()));
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