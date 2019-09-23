package item.inv.transport;

import building.*;
import geom.f1.*;
import item.inv.*;

public interface DoubleInv
{
	DoubleInvType type();

	String name();

	Tile location();

	Inv inputInv();

	Inv outputInv();

	default boolean active()
	{
		return true;
	}

	default int inputPriority()
	{
		return 0;
	}

	default int outputPriority()
	{
		return 0;
	}
}