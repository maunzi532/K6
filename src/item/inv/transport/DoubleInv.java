package item.inv.transport;

import geom.hex.*;
import item.inv.Inv;

public interface DoubleInv
{
	String name();

	Hex location();

	Inv inputInv();

	Inv outputInv();

	int inputPriority();

	int outputPriority();
}