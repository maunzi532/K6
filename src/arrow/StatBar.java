package arrow;

import javafx.scene.paint.*;

public class StatBar
{
	private final Color fg;
	private final Color bg;
	private final Color tc;
	private int current;
	private int max;
	private String t1;

	public StatBar(Color fg, Color bg, Color tc, int current, int max, String t1)
	{
		this.fg = fg;
		this.bg = bg;
		this.tc = tc;
		this.current = current;
		this.max = max;
		this.t1 = t1;
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

	public double filledPart()
	{
		return (double) current / max;
	}

	public void setCurrent(int current)
	{
		this.current = current;
	}

	public void changeCurrent(int change)
	{
		current += change;
	}

	public void setMax(int max)
	{
		this.max = max;
	}

	public void setT1(String t1)
	{
		this.t1 = t1;
	}

	public String getText()
	{
		return current + t1 + "/" + max;
	}
}