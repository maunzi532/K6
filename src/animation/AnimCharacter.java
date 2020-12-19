package animation;

import arrow.*;
import geom.tile.*;

public interface AnimCharacter
{
	Tile location();

	String mapImageName();

	void replaceVisual(XArrow arrow);
}