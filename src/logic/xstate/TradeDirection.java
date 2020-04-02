package logic.xstate;

public enum TradeDirection
{
	GIVE("menu.trade.give", "state.trade.give"),
	TAKE("menu.trade.take", "state.trade.take");

	public final String text;
	public final String keybind;

	TradeDirection(String text, String keybind)
	{
		this.text = text;
		this.keybind = keybind;
	}
}