package item.inv.transport;

import geom.hex.*;
import item.inv.Inv;

public interface DoubleInv
{
	String name();

	Hex location();

	Inv inputInv();

	Inv outputInv();

	default int inputPriority()
	{
		return 0;
	}

	default int outputPriority()
	{
		return 0;
	}
}