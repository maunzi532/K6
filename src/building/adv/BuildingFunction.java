package building.adv;

import com.fasterxml.jackson.jr.ob.comp.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.io.*;
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

	void productionPhase(boolean canWork, LevelMap levelMap, Tile location);

	void transportPhase(boolean canWork, LevelMap levelMap);

	void afterProduction();

	void afterTransport();

	void loadConnect(LevelMap levelMap, XBuilding connectTo);

	<T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException;
}