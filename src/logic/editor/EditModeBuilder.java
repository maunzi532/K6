package logic.editor;

import doubleinv.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;

public class EditModeBuilder implements XBuilder
{
	private Tile location;

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