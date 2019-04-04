package logic.xstate;

import building.*;
import geom.f1.*;
import item.inv.transport.*;
import java.util.*;
import levelMap.*;
import logic.*;

public class TransportTargetsState implements NMarkState
{
	private Transporter transporter;

	public TransportTargetsState(Transporter transporter)
	{
		this.transporter = transporter;
	}

	@Override
	public String text()
	{
		return "Targets";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.transportMenu(transporter);
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateControl stateControl)
	{
		transporter.toggleTarget((DoubleInv) levelMap.getBuilding(mapTile));
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return transporter.targets(levelMap);
	}
}