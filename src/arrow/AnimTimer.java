package arrow;

import entity.analysis.*;

public interface AnimTimer
{
	boolean finished();

	void tick();

	default RNGOutcome hack()
	{
		throw new RuntimeException();
	}
}