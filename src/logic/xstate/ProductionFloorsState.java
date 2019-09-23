package logic.xstate;

import building.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class ProductionFloorsState implements NMarkState
{
	private ProductionBuilding building;
	private List<Tile> targetableTiles;
	private List<VisMark> visMarked;

	public ProductionFloorsState(ProductionBuilding building)
	{
		this.building = building;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		targetableTiles = building.getCosts().requiredFloorTiles.stream().flatMap(flt -> mainState.y1
				.range(building.location(), flt.minRange, flt.maxRange).stream()
				.filter(e -> mainState.levelMap.getFloor(e) != null && mainState.levelMap.getFloor(e).type == flt.floorTileType)).collect(Collectors.toList());
		createVisMarked();
	}

	private void createVisMarked()
	{
		visMarked = targetableTiles.stream().map(e -> new VisMark(e, building.getClaimed().contains(e) ? Color.BLUE : Color.YELLOW, VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public String text()
	{
		return "Floor";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.F;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, int key)
	{
		if(targetableTiles.contains(mapTile))
		{
			building.toggleTarget(mapTile, mainState.levelMap);
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