package logic.xstate;

import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import java.io.*;
import load.*;
import logic.*;

public class WinState implements NAutoState
{
	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
	}

	@Override
	public void tick(MainState mainState)
	{
		//TODO save team
		//change level/team
		try
		{
			ObjectComposer<JSONComposer<String>> a1 = LoadHelper.startSave();
			mainState.levelMap().saveTeam(a1, mainState.systemScheme());
			String teamText = LoadHelper.endSave(a1);
			mainState.worldControl().updateTeam(teamText, null);
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		mainState.stateHolder().updateLevel(mainState.worldControl().createLevel());
	}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		return NoneState.INSTANCE;
	}
}