package doubleinv;

import geom.tile.*;
import item4.*;

public interface InvHolder
{
	CharSequence name();

	Tile location();

	Inv4 inv();

	void afterTrading();
}