package logic.xstate;

import building.adv.*;
import building.transport.*;
import doubleinv.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class TransportTargetsState implements NMarkState
{
	public static final Color ACTIVE = Color.BLUE;
	public static final Color INACTIVE = Color.YELLOW;

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
	public void onEnter(MainState mainState)
	{
		possibleTargets = mainState.y1.range(building.location(), 0, transport.range()).stream()
				.map(e -> (DoubleInv) mainState.levelMap.getBuilding(e))
				.filter(e -> e != null && e.active()).collect(Collectors.toList());
		createVisMarked();
	}

	private void createVisMarked()
	{
		visMarked = possibleTargets.stream().map(e -> new VisMark(e.location(), transport.isTarget(e) ? ACTIVE : INACTIVE, VisMark.d1)).collect(Collectors.toList());
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
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, XKey key)
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