package logic;

public enum XMenuEntry
{
	DUMMY("Something", false, false),
	PRODUCTION_PHASE("Production Phase", true, false),
	TRANSPORT_PHASE("Transport Phase", true, false),
	CHARACTER_MOVEMENT("Move", false, false),
	PRODUCTION_VIEW("View", false, true),
	TRANSPORT_VIEW("View", false, false),
	EDIT_TARGETS("Targets", false, false);

	XMenuEntry(String text, boolean direct, boolean withGUI)
	{
		this.text = text;
		this.direct = direct;
		this.withGUI = withGUI;
	}

	public final String text;
	public final boolean direct;
	public final boolean withGUI;
}