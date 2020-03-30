package arrow;

import geom.tile.*;
import java.util.*;

public class XArrow
{
	public static final int TIME_PER_DISTANCE = 10;

	protected final List<Tile> locations;
	protected final int duration;
	protected final boolean loop;
	protected final String imageName;
	protected int counter;
	protected boolean remove;

	public XArrow(List<Tile> locations, int duration, boolean loop, String imageName)
	{
		this.locations = locations;
		this.duration = duration;
		this.loop = loop;
		this.imageName = imageName;
	}

	public static XArrow factory(Tile start, Tile end, TileType y1, boolean loop, String imageName)
	{
		int duration = end == null || end.equals(start) ? 0 : y1.distance(end, start) * TIME_PER_DISTANCE;
		List<Tile> locations = convert(start, end);
		return new XArrow(locations, duration, loop, imageName);
	}

	public List<Tile> locations()
	{
		return locations;
	}

	public void tick()
	{
		counter++;
	}

	public boolean finished()
	{
		return remove || (!loop && counter >= duration);
	}

	public int counter()
	{
		return counter;
	}

	public int duration()
	{
		return duration;
	}

	public boolean loop()
	{
		return loop;
	}

	public void remove()
	{
		remove = true;
	}

	public String imageName()
	{
		return imageName;
	}

	public boolean zeroUpwards()
	{
		return true;
	}

	public static List<Tile> convert(Tile start, Tile end)
	{
		return end == null || end.equals(start) ? List.of(start) : List.of(start, end);
	}
}