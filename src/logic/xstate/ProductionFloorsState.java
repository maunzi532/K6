package logic.xstate;

import building.*;
import geom.f1.*;
import java.util.*;
import levelMap.*;
import logic.*;

public class ProductionFloorsState implements NMarkState
{
	private ProductionBuilding building;

	public ProductionFloorsState(ProductionBuilding building)
	{
		this.building = building;
	}

	@Override
	public String text()
	{
		return "Floor";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building);
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateControl stateControl)
	{
		building.toggleTarget(mapTile, levelMap);
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return building.floors(levelMap);
	}
}