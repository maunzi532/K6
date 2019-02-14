package inv;

import hex.*;

public interface DoubleInv
{
	String name();

	Hex location();

	Inv inputInv();

	Inv outputInv();

	int inputPriority();

	int outputPriority();
}