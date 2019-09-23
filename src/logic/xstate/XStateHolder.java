package logic.xstate;

import gui.*;
import java.util.*;

public interface XStateHolder
{
	void setState(NState state);

	NState getState();

	List<NState> getMenu();

	XGUI getGUI();
}