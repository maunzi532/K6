package doubleinv;

import geom.tile.*;
import item.*;

public interface InvHolder
{
	CharSequence name();

	Tile location();

	Inv4 inv();

	void afterTrading();
}