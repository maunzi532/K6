package logic.xstate;

import geom.tile.*;
import java.util.*;
import levelmap.*;
import logic.*;
import logic.editor.xstate.*;

public interface NMarkState extends NState
{
	void onClick(MainState mainState, Tile mapTile, XKey key);

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