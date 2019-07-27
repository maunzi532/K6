package logic.xstate;

import javafx.scene.input.*;
import logic.*;

public class ProductionPhaseState implements NAutoState
{
	public static final int ARROW_TIME = 30;
	public static final int WAIT = 30;

	private int counter;

	@Override
	public String text()
	{
		return "Production";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.P;
	}

	@Override
	public void tick(MainState mainState)
	{
		if(counter == 0)
		{
			mainState.levelMap.productionPhase();
		}
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= ARROW_TIME + WAIT;
	}

	@Override
	public NState nextState()
	{
		return new TransportPhaseState();
	}
}