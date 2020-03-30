package logic.xstate;

import building.adv.*;
import building.transport.*;
import doubleinv.*;
import entity.sideinfo.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.*;

public class TransportTargetsState implements NMarkState
{
	private final XBuilding building;
	private final Transport transport;
	private List<DoubleInv> possibleTargets;
	private List<VisMark> visMarked;

	public TransportTargetsState(XBuilding building, Transport transport)
	{
		this.building = building;
		this.transport = transport;
	}

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		possibleTargets = levelMap.y1.range(building.location(), 0, transport.range()).stream()
				.map(levelMap::getBuilding).filter(e -> e != null && e.active()).collect(Collectors.toList());
		createVisMarked();
	}

	private void createVisMarked()
	{
		visMarked = possibleTargets.stream().map(e -> new VisMark(e.location(),
				transport.isTarget(e) ? "mark.transport.target.active" : "mark.transport.target.inactive", VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public String text()
	{
		return "Targets";
	}

	@Override
	public String keybind()
	{
		return "Transport Targets";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.transportMenu(building, transport);
	}

	@Override
	public void onClick(MainState mainState, LevelMap levelMap, XStateHolder stateHolder, Tile mapTile, XKey key)
	{
		List<DoubleInv> list = possibleTargets.stream().filter(e -> mapTile.equals(e.location())).collect(Collectors.toList());
		if(list.isEmpty())
		{
			stateHolder.setState(NoneState.INSTANCE);
		}
		else
		{
			list.forEach(transport::toggleTarget);
			createVisMarked();
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}