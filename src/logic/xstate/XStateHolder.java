package logic.xstate;

import levelMap.*;
import logic.gui.*;
import java.util.*;

public interface XStateHolder
{
	void setState(NState state);

	NState getState();

	List<NState> getMenu();

	XGUIState getGUI();

	List<VisMark> visMarked();
}