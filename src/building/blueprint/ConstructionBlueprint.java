package building.blueprint;

import java.util.*;

public class ConstructionBlueprint
{
	public final List<XCostBlueprint> blueprints;

	public ConstructionBlueprint(XCostBlueprint... blueprints)
	{
		this.blueprints = Arrays.asList(blueprints);
	}
}