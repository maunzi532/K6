package logic.xstate;

import logic.gui.*;
import java.util.*;

public interface XStateHolder
{
	void setState(NState state);

	NState getState();

	List<NState> getMenu();

	NGUIState getGUI();
}