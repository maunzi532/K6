package inv;

import hex.*;

public interface DoubleInv
{
	Hex location();

	Inv2 inputInv();

	Inv2 outputInv();

	int inputPriority();

	int outputPriority();
}