package arrow;

import geom.f1.*;
import java.util.*;
import javafx.scene.paint.*;

public class InfoArrow extends XArrow
{
	private final Color fg;
	private final Color bg;
	private final Color tc;
	private int data;
	private int maxData;

	public InfoArrow(Tile location1, Tile location2, int duration, Color fg, Color bg, Color tc,
			int data, int maxData)
	{
		super(List.of(location1, location2), duration, false, null);
		this.fg = fg;
		this.bg = bg;
		this.tc = tc;
		this.data = data;
		this.maxData = maxData;
	}

	public Color getFg()
	{
		return fg;
	}

	public Color getBg()
	{
		return bg;
	}

	public Color getTc()
	{
		return tc;
	}

	public int getData()
	{
		return data;
	}

	public int getMaxData()
	{
		return maxData;
	}

	public void setData(int data)
	{
		this.data = data;
	}

	public void setMaxData(int maxData)
	{
		this.maxData = maxData;
	}
}