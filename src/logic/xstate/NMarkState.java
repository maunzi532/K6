package logic.xstate;

import geom.f1.*;
import java.util.*;
import levelMap.*;

public interface NMarkState extends NState
{
	default void onClick(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateHolder stateHolder)
	{
		if(markType != MarkType.NOT)
			onClickMarked(mapTile, markType, key, levelMap, stateHolder);
		else
			stateHolder.setState(editMode() ? EditingState.INSTANCE : NoneState.INSTANCE);
	}

	void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateHolder stateHolder);

	Map<Tile, MarkType> marked(LevelMap levelMap);
}