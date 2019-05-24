package arrow;

import geom.f1.*;
import java.util.*;
import javafx.scene.paint.*;

public class InfoArrow extends XArrow
{
	private StatBar statBar;

	public InfoArrow(Tile location1, Tile location2, int duration,
			Color fg, Color bg, Color tc, int data, int maxData)
	{
		super(List.of(location1, location2), duration, false, null);
		statBar = new StatBar(fg, bg, tc, data, maxData);
	}

	public StatBar statBar()
	{
		return statBar;
	}
}