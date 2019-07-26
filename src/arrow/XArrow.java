package arrow;

import geom.f1.*;
import java.util.*;
import javafx.scene.image.*;

public class XArrow
{
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

	public static XArrow factory(Tile start, Tile end, int duration, boolean loop, Image image, boolean shine)
	{
		List<Tile> locations = convert(start, end);
		if(shine)
		{
			return new ShineArrow(locations, duration, loop, image, true);
		}
		else
		{
			return new XArrow(locations, duration, loop, image);
		}
	}

	public static List<Tile> convert(Tile start, Tile end)
	{
		return end == null || end.equals(start) ? List.of(start) : List.of(start, end);
	}
}