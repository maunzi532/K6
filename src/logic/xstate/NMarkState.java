package logic.xstate;

import geom.f1.*;
import java.util.*;
import levelMap.*;
import logic.*;

public interface NMarkState extends NState
{
	default void onClick(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateControl stateControl)
	{
		if(markType != MarkType.NOT)
			onClickMarked(mapTile, markType, key, levelMap, stateControl);
		else
			stateControl.setState(editMode() ? EditingState.INSTANCE : NoneState.INSTANCE);
	}

	void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateControl stateControl);

	Map<Tile, MarkType> marked(LevelMap levelMap);
}