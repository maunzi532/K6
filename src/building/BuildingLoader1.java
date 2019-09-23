package building;

import com.fasterxml.jackson.jr.stree.*;
import geom.f1.*;
import item.*;
import levelMap.*;

public class BuildingLoader1 implements BuildingLoader
{
	@Override
	public MBuilding loadBuilding(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		if(data.get("Recipes") != null)
		{
			return new ProductionBuilding(data, itemLoader, y1);
		}
		if(data.get("Amount") != null)
		{
			return new Transporter(data, itemLoader, y1);
		}
		throw new RuntimeException();
	}
}