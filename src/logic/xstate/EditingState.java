package logic.xstate;

public class EditingState implements NState
{
	public static final EditingState INSTANCE = new EditingState();

	@Override
	public String text()
	{
		return "Edit";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}
}