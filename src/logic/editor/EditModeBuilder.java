package logic.editor;

import doubleinv.*;
import geom.tile.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;

public final class EditModeBuilder implements XBuilder
{
	private final Tile location;

	public EditModeBuilder(Tile location)
	{
		this.location = location;
	}

	@Override
	public Tile location()
	{
		return location;
	}

	@Override
	public ItemView viewRecipeItem(Item item)
	{
		return new ItemView(new ItemStack(item, -1));
	}

	@Override
	public Optional<ItemList> tryBuildingCosts(ItemList refundable, ItemList costs, CommitType commitType)
	{
		return Optional.of(refundable);
	}
}