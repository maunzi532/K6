package building.adv;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import doubleinv.*;
import geom.tile.*;
import item.*;
import item.inv.*;
import java.io.*;

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

	void productionPhase(boolean canWork, Arrows arrows, Tile location);

	void transportPhase(boolean canWork, Arrows arrows);

	void afterProduction();

	void afterTransport();

	void loadConnect(ConnectRestore cr);

	<T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException;
}