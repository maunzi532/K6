package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;

public record ConstructionBlueprint(List<List<CostBlueprint>> blueprints)
{
	public static ConstructionBlueprint create(JrsArray data, ItemLoader itemLoader)
	{
		List<List<CostBlueprint>> blueprints = new ArrayList<>();
		data.elements().forEachRemaining(e ->
		{
			List<CostBlueprint> b1 = new ArrayList<>();
			((JrsArray) e).elements().forEachRemaining(f -> b1.add(CostBlueprint.create((JrsObject) f, itemLoader)));
			blueprints.add(b1);
		});
		return new ConstructionBlueprint(blueprints);
	}

	public <T extends ComposerBase> ArrayComposer<T> save(ArrayComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		for(List<CostBlueprint> l1 : blueprints)
		{
			var a2 = a1.startArray();
			for(CostBlueprint c1 : l1)
			{
				a2 = c1.save(a2.startObject(), itemLoader).end();
			}
			a1 = a2.end();
		}
		return a1;
	}
}