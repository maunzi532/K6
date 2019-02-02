package building.blueprint;

import java.util.*;

public class XCostBlueprint
{
	private final List<CostBlueprint> blueprints;

	public XCostBlueprint(CostBlueprint blueprint)
	{
		blueprints = List.of(blueprint);
	}

	protected XCostBlueprint(CostBlueprint... blueprints)
	{
		this.blueprints = Arrays.asList(blueprints);
	}

	public CostBlueprint get()
	{
		return blueprints.get(0);
	}
}