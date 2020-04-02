package logic.xstate;

import building.adv.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;
import levelmap.*;
import logic.*;

public final class ProductionFloorsState implements NMarkState
{
	private final XBuilding building;
	private final ProcessInv processInv;
	private List<Tile> targetableTiles;
	private List<VisMark> visMarked;

	public ProductionFloorsState(XBuilding building, ProcessInv processInv)
	{
		this.building = building;
		this.processInv = processInv;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		targetableTiles = building.costBlueprint().requiredFloorTiles().stream().flatMap(flt ->
				mainState.levelMap().y1.range(building.location(), flt.minRange(), flt.maxRange()).stream()
						.filter(e -> mainState.levelMap().getFloor(e) != null && mainState.levelMap().getFloor(e).type == flt.floorTileType())).collect(Collectors.toList());
		createVisMarked();
	}

	private void createVisMarked()
	{
		visMarked = targetableTiles.stream().map(e -> new VisMark(e,
				building.claimed().contains(e) ? "mark.production.floor.active" : "mark.production.floor.inactive", VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public CharSequence text()
	{
		return "menu.floor";
	}

	@Override
	public String keybind()
	{
		return "state.floor";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building, processInv);
	}

	@Override
	public void onClick(MainState mainState, Tile mapTile, XKey key)
	{
		if(targetableTiles.contains(mapTile))
		{
			mainState.levelMap().toggleTargetClaimed(mapTile, building);
			createVisMarked();
		}
		else
		{
			mainState.stateHolder().setState(NoneState.INSTANCE);
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}