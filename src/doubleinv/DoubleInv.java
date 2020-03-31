package doubleinv;

import geom.tile.*;
import item.inv.*;

public interface DoubleInv
{
	DoubleInvType type();

	CharSequence name();

	Tile location();

	default boolean playerTradeable(boolean levelStarted)
	{
		return false;
	}

	Inv inputInv();

	Inv outputInv();

	boolean active();

	void afterTrading();

	default int inputPriority()
	{
		return 0;
	}

	default int outputPriority()
	{
		return 0;
	}
}