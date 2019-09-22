package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;

public class ConstructionBlueprint
{
	public final List<List<CostBlueprint>> blueprints;

	public ConstructionBlueprint(List<List<CostBlueprint>> blueprints)
	{
		this.blueprints = blueprints;
	}

	public ConstructionBlueprint(JrsArray data, ItemLoader itemLoader)
	{
		blueprints = new ArrayList<>();
		data.elements().forEachRemaining(e -> l1((JrsArray) e, itemLoader));
	}

	private void l1(JrsArray d1, ItemLoader itemLoader)
	{
		List<CostBlueprint> b1 = new ArrayList<>();
		d1.elements().forEachRemaining(e -> b1.add(new CostBlueprint((JrsObject) e, itemLoader)));
		blueprints.add(b1);
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