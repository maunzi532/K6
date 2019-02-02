package logic;

public enum XMenuEntry
{
	DUMMY("Something", false),
	PRODUCTION_PHASE("Production Phase", true),
	TRANSPORT_PHASE("Transport Phase", true),
	CHARACTER_MOVEMENT("Move", false),
	BUILDING_VIEW("View", false);

	XMenuEntry(String text, boolean direct)
	{
		this.text = text;
		this.direct = direct;
	}

	public final String text;
	public final boolean direct;
}