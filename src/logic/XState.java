package logic;

public enum XState
{
	NONE("Error", false, false, false),
	CHARACTER_MOVEMENT("Move", true, true, false),
	PRODUCTION_VIEW("View", true, false, true),
	PRODUCTION_INV("Inv.", true, false, true),
	TRANSPORT_VIEW("View", true, false, false),
	TRANSPORT_TARGETS("Targets", true, true, false),
	VIEW_INV("Inv.", true, false, true),
	TAKE_TARGET("Take", true, true, false),
	GIVE_TARGET("Give", true, true, false),
	DIRECTED_TRADE("Error", false, false, true),
	BUILDINGS("Build", true, false, true),
	BUILD("Build", false, false, true),
	REMOVE("Remove", true, false, true),
	PRODUCTION_PHASE("Production", false, false, false),
	TRANSPORT_PHASE("Transport", false, false, false);

	XState(String text, boolean set, boolean mark, boolean hasGUI)
	{
		this.text = text;
		this.set = set;
		this.mark = mark;
		this.hasGUI = hasGUI;
	}

	public final String text;
	public final boolean set;
	public final boolean mark;
	public final boolean hasGUI;
}