package building.transport;

import geom.f1.*;
import item.inv.*;

public interface DoubleInv
{
	static boolean isTargetable(Object target)
	{
		return target instanceof DoubleInv && ((DoubleInv) target).active();
	}

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

	default void afterTrading(){}
}