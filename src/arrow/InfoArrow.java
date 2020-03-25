package arrow;

import geom.f1.*;
import java.util.*;

public class InfoArrow extends XArrow
{
	private final StatBar statBar;

	public InfoArrow(Tile location1, Tile location2, int duration, String fg, String bg, String tc, int current, int max)
	{
		super(List.of(location1, location2), duration, false, null);
		statBar = new StatBar(fg, bg, tc, current, max, "");
	}

	public InfoArrow(Tile location1, Tile location2, String fg, String bg, String tc, int current, int max)
	{
		super(List.of(location1, location2), 0, true, null);
		statBar = new StatBar(fg, bg, tc, current, max, "");
	}

	public InfoArrow(Tile location1, String fg, String bg, String tc, int current, int max)
	{
		super(List.of(location1), 0, true, null);
		statBar = new StatBar(fg, bg, tc, current, max, "");
	}

	public StatBar statBar()
	{
		return statBar;
	}
}