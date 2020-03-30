package logic.xstate;

public enum TradeDirection
{
	GIVE("Give", "Give"),
	TAKE("Take", "Give");

	public final String text;
	public final String keybind;

	TradeDirection(String text, String keybind)
	{
		this.text = text;
		this.keybind = keybind;
	}
}