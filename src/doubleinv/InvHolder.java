package doubleinv;

import geom.tile.*;
import item.*;

public interface InvHolder
{
	CharSequence name();

	Tile location();

	Inv inv();

	void afterTrading();
}