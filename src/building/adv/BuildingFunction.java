package building.adv;

import building.blueprint.*;
import geom.f1.*;
import item.inv.*;
import levelMap.*;

public interface BuildingFunction
{
	String name();

	Inv inputInv();

	Inv outputInv();

	boolean playerTradeable(boolean levelStarted);

	void afterTrading();

	default int inputPriority()
	{
		return 0;
	}

	default int outputPriority()
	{
		return 0;
	}

	void productionPhase(LevelMap levelMap, CostBlueprint costBlueprint, Tile location);

	void afterProduction();

	void transportPhase(LevelMap levelMap);

	void afterTransport();

	void loadConnect(LevelMap levelMap, XBuilding connectTo);
}