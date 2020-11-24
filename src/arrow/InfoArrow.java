package arrow;

import geom.tile.*;
import java.util.*;

public final class InfoArrow extends XArrow
{
	private final StatBar statBar;

	public InfoArrow(Tile location1, Tile location2, String fg, String bg, String tc, int current, int max)
	{
		super(List.of(location1, location2), 0, true, null);
		statBar = new StatBar(fg, bg, tc, current, max);
	}

	public InfoArrow(Tile location1, String fg, String bg, String tc, int current, int max)
	{
		super(List.of(location1), 0, true, null);
		statBar = new StatBar(fg, bg, tc, current, max);
	}

	public InfoArrow(Tile location1, Tile location2, StatBar statBar)
	{
		super(List.of(location1, location2), 0, true, null);
		this.statBar = statBar;
	}

	public StatBar statBar()
	{
		return statBar;
	}
}