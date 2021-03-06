package arrow;

import geom.dtile.*;
import geom.tile.*;
import java.util.*;

public final class ShineArrow extends XArrow
{
	private final boolean hasShine;
	private DoubleTile[] stored;

	private ShineArrow(List<Tile> locations, int duration, boolean loop, String imageName, boolean hasShine)
	{
		super(locations, duration, loop, imageName);
		this.hasShine = hasShine;
	}

	public static ShineArrow factory(Tile start, Tile end, int duration, boolean loop, String imageName)
	{
		List<Tile> locations = convert(start, end);
		return new ShineArrow(locations, duration, loop, imageName, true);
	}

	public boolean hasShine()
	{
		return hasShine;
	}

	public void storeArrowPoints(DoubleTile[] store)
	{
		stored = store;
	}

	public DoubleTile[] storedArrowPoints()
	{
		return stored;
	}
}