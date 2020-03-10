package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;

public record ProductionBlueprint(ItemList inputLimits, ItemList outputLimits, List<Recipe> recipes)
{
	public static ProductionBlueprint create(JrsObject data, ItemLoader itemLoader)
	{
		ItemList inputLimits = new ItemList((JrsArray) data.get("InputLimits"), itemLoader);
		ItemList outputLimits = new ItemList((JrsArray) data.get("OutputLimits"), itemLoader);
		List<Recipe> recipes = new ArrayList<>();
		((JrsArray) data.get("Recipes")).elements().forEachRemaining(e -> recipes.add(Recipe.create((JrsObject) e, itemLoader)));
		return new ProductionBlueprint(inputLimits, outputLimits, recipes);
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