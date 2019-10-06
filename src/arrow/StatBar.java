package arrow;

import entity.*;
import javafx.scene.paint.*;

public class StatBar
{
	private final Color fg;
	private final Color bg;
	private final Color tc;
	private int data;
	private int maxData;

	public StatBar(Color fg, Color bg, Color tc, int data, int maxData)
	{
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

	public static StatBar forEntity(XEntity entity)
	{
		return new StatBar(entity instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK, Color.WHITE,
				entity.getStats().getVisualStat(0), entity.getStats().getMaxVisualStat(0));
	}
}