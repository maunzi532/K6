package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import file.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class ConstructionBlueprint
{
	public final List<List<CostBlueprint>> blueprints;

	public ConstructionBlueprint(CostBlueprint... blueprints)
	{
		this.blueprints = Arrays.stream(blueprints).map(List::of).collect(Collectors.toList());
	}

	public ConstructionBlueprint(CostBlueprint[][] blueprints)
	{
		this.blueprints = Arrays.stream(blueprints).map(List::of).collect(Collectors.toList());
	}

	public ConstructionBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		blueprints = node.inside.stream().map(e -> e.inside.stream().map(CostBlueprint::new)
				.collect(Collectors.toList())).collect(Collectors.toList());
	}

	public ConstructionBlueprint(JrsArray data)
	{
		blueprints = new ArrayList<>();
		data.elements().forEachRemaining(e -> l1((JrsArray) e));
	}

	private void l1(JrsArray d1)
	{
		List<CostBlueprint> b1 = new ArrayList<>();
		d1.elements().forEachRemaining(e -> b1.add(new CostBlueprint((JrsObject) e)));
		blueprints.add(b1);
	}

	public <T extends ComposerBase> ArrayComposer<T> save(ArrayComposer<T> a1) throws IOException
	{
		for(List<CostBlueprint> l1 : blueprints)
		{
			var a2 = a1.startArray();
			for(CostBlueprint c1 : l1)
			{
				a2 = c1.save(a2.startObject()).end();
			}
			a1 = a2.end();
		}
		return a1;
	}
}