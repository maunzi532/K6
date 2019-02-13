package logic;

public enum XState2
{
	NONE(false, false, false),
	CHARACTER_MOVEMENT(true, true, false),
	PRODUCTION_VIEW(true, false, true),
	TRANSPORT_VIEW(true, false, false),
	TRANSPORT_TARGETS(true, true, false),
	TAKE_TARGET(true, true, false),
	GIVE_TARGET(true, true, false),
	DIRECTED_TRADE(false, false, false);

	XState2(boolean set, boolean mark, boolean hasGUI)
	{
		this.set = set;
		this.mark = mark;
		this.hasGUI = hasGUI;
	}

	public final boolean set;
	public final boolean mark;
	public final boolean hasGUI;
}