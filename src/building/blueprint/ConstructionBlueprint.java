package building.blueprint;

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
}