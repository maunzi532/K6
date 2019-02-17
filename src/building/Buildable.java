package building;

import building.blueprint.*;
import geom.hex.Hex;

public abstract class Buildable implements Building
{
	private Hex location;
	private CostBlueprint costs;

	public Buildable(Hex location, CostBlueprint costs)
	{
		this.location = location;
		this.costs = costs;
	}

	@Override
	public Hex location()
	{
		return location;
	}

	public CostBlueprint getCosts()
	{
		return costs;
	}
}