package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import file.*;
import item.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class ProductionBlueprint
{
	public final ItemList inputLimits;
	public final ItemList outputLimits;
	public final List<Recipe> recipes;

	public ProductionBlueprint(ItemList inputLimits, ItemList outputLimits, Recipe... recipes)
	{
		this.inputLimits = inputLimits;
		this.outputLimits = outputLimits;
		this.recipes = Arrays.asList(recipes);
	}

	public ProductionBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		inputLimits = new ItemList(node.get(0));
		outputLimits = new ItemList(node.get(1));
		recipes = node.get(2).inside.stream().map(Recipe::new).collect(Collectors.toList());
	}

	public ProductionBlueprint(JrsObject data)
	{
		inputLimits = new ItemList((JrsArray) data.get("InputLimits"));
		outputLimits = new ItemList((JrsArray) data.get("OutputLimits"));
		recipes = new ArrayList<>();
		((JrsArray) data.get("Recipes")).elements().forEachRemaining(e -> recipes.add(new Recipe((JrsObject) e)));
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		var a2 = inputLimits.save(a1.startArrayField("InputLimits")).end();
		a2 = outputLimits.save(a2.startArrayField("OutputLimits")).end();
		var a3 = a2.startArrayField("Recipes");
		for(Recipe recipe : recipes)
		{
			a3 = recipe.save(a3.startObject()).end();
		}
		return a3.end();
	}
}