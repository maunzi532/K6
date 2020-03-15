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

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		inputLimits.save(a1.startArrayField("InputLimits"), itemLoader);
		outputLimits.save(a1.startArrayField("OutputLimits"), itemLoader);
		var a2 = a1.startArrayField("Recipes");
		for(Recipe recipe : recipes)
		{
			recipe.save(a2.startObject(), itemLoader);
		}
		a2.end();
		a1.end();
	}
}