package arrow;

public enum ArrowMode
{
	FULL(true, true, true, true, true),
	ARROW(false, true, true, true, true),
	TARROW(false, true, true, true, false),
	TRANSPORT(true, false, false, false, false);

	ArrowMode(boolean showTransport, boolean showArrow, boolean showZero, boolean showShine, boolean loop)
	{
		this.showTransport = showTransport;
		this.showArrow = showArrow;
		this.showZero = showZero;
		this.showShine = showShine;
		this.loop = loop;
	}

	public final boolean showTransport;
	public final boolean showArrow;
	public final boolean showZero;
	public final boolean showShine;
	public final boolean loop;
}