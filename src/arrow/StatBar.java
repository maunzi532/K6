package arrow;

public final class StatBar
{
	private final String fg;
	private final String bg;
	private final String tc;
	private int current;
	private int max;
	private CharSequence t1;

	public StatBar(String fg, String bg, String tc, int current, int max, CharSequence t1)
	{
		this.fg = fg;
		this.bg = bg;
		this.tc = tc;
		this.current = current;
		this.max = max;
		this.t1 = t1;
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

	public void changeCurrent(int change)
	{
		current += change;
	}

	public void setMax(int max)
	{
		this.max = max;
	}

	public void setT1(CharSequence t1)
	{
		this.t1 = t1;
	}

	public CharSequence getText()
	{
		return current + t1.toString() + "/" + max; //TODO
	}
}