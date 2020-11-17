package doubleinv;

import geom.tile.*;
import item.inv.*;

public interface DoubleInv
{
	CharSequence name();

	Tile location();

	Inv inv(TradeDirection tradeDirection);

	boolean active();

	void afterTrading();

	default int priority(TradeDirection tradeDirection)
	{
		return 0;
	}
}