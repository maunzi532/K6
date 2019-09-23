package arrow;

import geom.f1.*;
import java.util.*;
import javafx.scene.image.*;

public class XArrow
{
	public static final int TIME_PER_DISTANCE = 10;

	protected final List<Tile> locations;
	protected final int duration;
	protected final boolean loop;
	protected final Image image;
	protected int counter;
	protected boolean remove;

	public XArrow(List<Tile> locations, int duration, boolean loop, Image image)
	{
		this.locations = locations;
		this.duration = duration;
		this.loop = loop;
		this.image = image;
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

	public Image image()
	{
		return image;
	}

	public boolean zeroUpwards()
	{
		return true;
	}

	public static XArrow factory(Tile start, Tile end, TileType y1, boolean loop, Image image)
	{
		int duration = end == null || end.equals(start) ? 0 : y1.distance(end, start) * TIME_PER_DISTANCE;
		List<Tile> locations = convert(start, end);
		return new XArrow(locations, duration, loop, image);
	}

	public static List<Tile> convert(Tile start, Tile end)
	{
		return end == null || end.equals(start) ? List.of(start) : List.of(start, end);
	}
}