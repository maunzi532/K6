package building;

import com.fasterxml.jackson.jr.stree.*;
import geom.f1.*;
import item.*;
import levelMap.*;

public interface BuildingLoader
{
	MBuilding loadBuilding(JrsObject data, ItemLoader itemLoader, TileType y1);
}