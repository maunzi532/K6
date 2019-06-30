package logic;

import geom.f1.*;
import logic.xstate.*;

public class StateControl2 implements XStateHolder, ConvInputConsumer
{
	private MainState mainState;
	private NState state;

	@Override
	public void setState(NState state)
	{
		this.state = state;
		state.onEnter(mainState);
	}

	@Override
	public void mousePosition(double xRel, double yRel, boolean insideGUI, Tile offsetGUITile, int menuOption,
			int editorOption, Tile mapTile, boolean moved, boolean drag, int mouseKey)
	{

	}

	@Override
	public void dragPosition(Tile startTile, Tile endTile, int mouseKey, boolean finished)
	{

	}

	@Override
	public void handleKey()
	{

	}
}