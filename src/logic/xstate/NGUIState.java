package logic.xstate;

import gui.*;
import logic.*;

public interface NGUIState extends NState
{
	XGUI gui(MainState mainState);
}