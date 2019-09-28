package building;

import building.blueprint.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;
import levelMap.*;

public interface XBuilder
{
	Tile location();

	ItemView viewRecipeItem(Item item);

	Optional<ItemList> tryBuildingCosts(CostBlueprint cost, CommitType commitType);

	default void buildBuilding(LevelMap levelMap, CostBlueprint costs, ItemList refundable, BuildingBlueprint blueprint)
	{
		if(blueprint.productionBlueprint != null)
		{
			ProductionBuilding building = new ProductionBuilding(location(), costs, refundable, blueprint);
			levelMap.addBuilding(building);
			building.claimFloor(levelMap);
		}
		else if(blueprint.transporterBlueprint != null)
		{
			levelMap.addBuilding(new Transporter(location(), costs, refundable, blueprint));
		}
	}
}