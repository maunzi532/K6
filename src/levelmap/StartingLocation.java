package levelmap;

import geom.tile.*;

public record StartingLocation(CharSequence characterName, Tile location, boolean canSwap, boolean canTrade){}