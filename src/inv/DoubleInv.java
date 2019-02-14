package inv;

import hex.*;

public interface DoubleInv
{
	Hex location();

	Inv3 inputInv();

	Inv3 outputInv();

	int inputPriority();

	int outputPriority();
}