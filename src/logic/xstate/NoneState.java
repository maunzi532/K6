package logic.xstate;

public class NoneState implements NState
{
	public static final NoneState INSTANCE = new NoneState();

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}