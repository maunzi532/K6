package entity;

import arrow.*;
import geom.tile.*;

public record SideInfo(Tile location, String imageName, StatBar statBar, CharSequence... texts)
{

}