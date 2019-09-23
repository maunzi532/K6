package logic.xstate;

import building.*;
import geom.f1.*;
import item.inv.transport.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class TransportTargetsState implements NMarkState
{
	private Transporter transporter;
	private List<DoubleInv> possibleTargets;
	private List<VisMark> visMarked;

	public TransportTargetsState(Transporter transporter)
	{
		this.transporter = transporter;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		possibleTargets = mainState.y2.range(transporter.location(), 0, transporter.getRange()).stream()
				.filter(e -> DoubleInv.isTargetable(mainState.levelMap.getBuilding(e)))
				.map(e -> (DoubleInv) e).collect(Collectors.toList());
		createVisMarked();
	}

	private void createVisMarked()
	{
		visMarked = possibleTargets.stream().map(e -> new VisMark(e.location(), transporter.isTarget(e) ? Color.BLUE : Color.YELLOW, VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public String text()
	{
		return "Targets";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.T;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.transportMenu(transporter);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, int key)
	{
		List<DoubleInv> list = possibleTargets.stream().filter(e -> mapTile.equals(e.location())).collect(Collectors.toList());
		if(list.isEmpty())
		{
			stateHolder.setState(NoneState.INSTANCE);
		}
		else
		{
			list.forEach(e -> transporter.toggleTarget(e));
			createVisMarked();
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}