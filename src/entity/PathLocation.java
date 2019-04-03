package entity;

import geom.f1.*;
import java.util.*;

public class PathLocation
{
	public final Tile t1;
	public final int cost;
	public final boolean canEnd;

	public PathLocation(Tile t1, int cost, boolean canEnd)
	{
		this.t1 = t1;
		this.cost = cost;
		this.canEnd = canEnd;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof PathLocation)) return false;
		PathLocation that = (PathLocation) o;
		return t1.equals(that.t1);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(t1);
	}
}