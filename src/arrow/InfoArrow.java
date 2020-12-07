package arrow;

import geom.tile.*;
import java.util.*;

public final class InfoArrow extends XArrow
{
	private final StatBar statBar;

	public InfoArrow(Tile location1, Tile location2, StatBar statBar)
	{
		super(List.of(location1, location2), 0, true, null);
		this.statBar = statBar;
	}

	public InfoArrow(Tile location1, StatBar statBar)
	{
		super(List.of(location1), 0, true, null);
		this.statBar = statBar;
	}

	public StatBar statBar()
	{
		return statBar;
	}
}