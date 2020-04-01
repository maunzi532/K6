package arrow;

import text.*;

public final class StatBar
{
	private final String fg;
	private final String bg;
	private final String tc;
	private int current;
	private int max;
	private int change;

	public StatBar(String fg, String bg, String tc, int current, int max)
	{
		this.fg = fg;
		this.bg = bg;
		this.tc = tc;
		this.current = current;
		this.max = max;
		change = 0;
	}

	public StatBar(String fg, String bg, String tc, int current, int max, int change)
	{
		this.fg = fg;
		this.bg = bg;
		this.tc = tc;
		this.current = current;
		this.max = max;
		this.change = change;
	}

	public String getFg()
	{
		return fg;
	}

	public String getBg()
	{
		return bg;
	}

	public String getTc()
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

	public void alterCurrent(int diff)
	{
		current += diff;
	}

	public void alterCurrentAndChange(int diff)
	{
		current += diff;
		change -= diff;
	}

	public void setMax(int max)
	{
		this.max = max;
	}

	public CharSequence getText()
	{
		if(change != 0)
			return new ArgsText("statbar.change", current, change, max);
		else
			return new ArgsText("statbar", current, max);
	}
}