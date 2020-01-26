package building;

import building.adv.*;
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
		XBuilding building = new XBuilding(location(), costs, refundable, blueprint);
		levelMap.addBuilding(building);
		building.autoClaimFloor(levelMap);
	}
}