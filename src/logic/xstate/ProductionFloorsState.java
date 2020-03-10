package logic.xstate;

import building.adv.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class ProductionFloorsState implements NMarkState
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
		targetableTiles = building.costBlueprint().requiredFloorTiles().stream().flatMap(flt -> mainState.y1
				.range(building.location(), flt.minRange(), flt.maxRange()).stream()
				.filter(e -> mainState.levelMap.getFloor(e) != null && mainState.levelMap.getFloor(e).type == flt.floorTileType())).collect(Collectors.toList());
		createVisMarked();
	}

	private void createVisMarked()
	{
		visMarked = targetableTiles.stream().map(e -> new VisMark(e, building.claimed().contains(e) ? Color.BLUE : Color.YELLOW, VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public String text()
	{
		return "Floor";
	}

	@Override
	public String keybind()
	{
		return "Production Floors";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building, processInv);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, XKey key)
	{
		if(targetableTiles.contains(mapTile))
		{
			building.toggleTargetClaimed(mapTile, mainState.levelMap);
			createVisMarked();
		}
		else
		{
			stateHolder.setState(NoneState.INSTANCE);
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}