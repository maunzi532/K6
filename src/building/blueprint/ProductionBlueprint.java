package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;

public class ProductionBlueprint
{
	public final ItemList inputLimits;
	public final ItemList outputLimits;
	public final List<Recipe> recipes;

	public ProductionBlueprint(ItemList inputLimits, ItemList outputLimits,
			List<Recipe> recipes)
	{
		this.inputLimits = inputLimits;
		this.outputLimits = outputLimits;
		this.recipes = recipes;
	}

	public ProductionBlueprint(JrsObject data, ItemLoader itemLoader)
	{
		inputLimits = new ItemList((JrsArray) data.get("InputLimits"), itemLoader);
		outputLimits = new ItemList((JrsArray) data.get("OutputLimits"), itemLoader);
		recipes = new ArrayList<>();
		((JrsArray) data.get("Recipes")).elements().forEachRemaining(e -> recipes.add(new Recipe((JrsObject) e, itemLoader)));
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		var a2 = inputLimits.save(a1.startArrayField("InputLimits"), itemLoader).end();
		a2 = outputLimits.save(a2.startArrayField("OutputLimits"), itemLoader).end();
		var a3 = a2.startArrayField("Recipes");
		for(Recipe recipe : recipes)
		{
			a3 = recipe.save(a3.startObject(), itemLoader).end();
		}
		return a3.end();
	}
}