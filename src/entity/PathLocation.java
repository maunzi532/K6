package entity;

import geom.hex.*;
import java.util.*;

public class PathLocation
{
	public final Hex hex;
	public final int cost;
	public final boolean canEnd;

	public PathLocation(Hex hex, int cost, boolean canEnd)
	{
		this.hex = hex;
		this.cost = cost;
		this.canEnd = canEnd;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof PathLocation)) return false;
		PathLocation that = (PathLocation) o;
		return hex.equals(that.hex);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(hex);
	}
}