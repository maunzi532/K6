package logic.xstate;

public enum TradeDirection
{
	GIVE("menu.trade.give", "Give"),
	TAKE("menu.trade.take", "Take");

	public final String text;
	public final String keybind;

	TradeDirection(String text, String keybind)
	{
		this.text = text;
		this.keybind = keybind;
	}
}