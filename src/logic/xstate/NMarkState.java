package logic.xstate;

import geom.f1.*;
import java.util.*;
import levelMap.*;
import logic.*;
import logic.editor.xstate.*;

public interface NMarkState extends NState
{
	void onClick(MainState mainState, LevelMap levelMap, XStateHolder stateHolder, Tile mapTile, XKey key);

	List<VisMark> visMarked(MainState mainState);

	default void onEscape(XStateHolder stateHolder)
	{
		if(editMode())
		{
			stateHolder.setState(EditingState.INSTANCE);
		}
		else
		{
			stateHolder.setState(NoneState.INSTANCE);
		}
	}
}