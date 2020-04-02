package item.inv;

import com.fasterxml.jackson.jr.ob.comp.*;
import item.*;
import item.view.*;
import java.io.*;
import java.util.*;

public final class BlockedInv implements Inv
{
	public static final BlockedInv INSTANCE = new BlockedInv();

	@Override
	public List<Item> providedItemTypesX()
	{
		return List.of();
	}

	@Override
	public List<ItemView> viewItems(boolean withEmpty)
	{
		return List.of();
	}

	@Override
	public ItemList allItems()
	{
		return new ItemList();
	}

	@Override
	public ItemView viewRecipeItem(Item item)
	{
		return new ItemView(item, 0, 0);
	}

	@Override
	public InvNumView viewInvWeight()
	{
		return new InvNumView(0, 0);
	}

	@Override
	public Inv copy()
	{
		return new BlockedInv();
	}

	@Override
	public boolean ok()
	{
		return true;
	}

	@Override
	public void commit(){}

	@Override
	public void rollback(){}

	@Override
	public boolean canGive(ItemStack itemStack, boolean unlimited)
	{
		return false;
	}

	@Override
	public boolean give(ItemStack itemStack, boolean unlimited)
	{
		return false;
	}

	@Override
	public Optional<ItemStack> showProvidable(ItemStack itemStack, boolean unlimited)
	{
		return Optional.empty();
	}

	@Override
	public Optional<ItemStack> provide(ItemStack itemStack, boolean unlimited)
	{
		return Optional.empty();
	}

	@Override
	public boolean canAdd(ItemStack itemStack, boolean unlimited)
	{
		return false;
	}

	@Override
	public boolean add(ItemStack itemStack, boolean unlimited)
	{
		return false;
	}

	@Override
	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		a1.end();
	}
}