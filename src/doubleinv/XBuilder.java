package doubleinv;

import geom.f1.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;

public interface XBuilder
{
	Tile location();

	ItemView viewRecipeItem(Item item);

	Optional<ItemList> tryBuildingCosts(ItemList refundable, ItemList costs, CommitType commitType);
}