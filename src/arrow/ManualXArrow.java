package arrow;

import geom.tile.*;
import java.util.*;

public final class ManualXArrow extends XArrow
{
	public ManualXArrow(List<Tile> locations, int duration, String imageName)
	{
		super(locations, duration, true, imageName);
	}

	@Override
	public void tick(){}

	public void setCounter(int counter)
	{
		this.counter = counter;
	}
}