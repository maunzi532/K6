package building.blueprint;

import file.*;
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
}