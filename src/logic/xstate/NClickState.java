package logic.xstate;

import logic.*;

public interface NClickState extends NState
{
	void onMenuClick(int key, MainState mainState);
}