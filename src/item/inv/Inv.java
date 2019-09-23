package item.inv;

import com.fasterxml.jackson.jr.ob.comp.*;
import item.*;
import item.view.*;
import java.io.*;
import java.util.*;

public interface Inv extends Inv0
{
	List<Item> providedItemTypesX();

	List<ItemView> viewItems(boolean withEmpty);

	ItemView viewRecipeItem(Item item);

	InvNumView viewInvWeight();

	Inv copy();

	default void useCommitType(CommitType type)
	{
		if(type == CommitType.COMMIT)
			commit();
		else if(type == CommitType.ROLLBACK)
			rollback();
	}

	default boolean tryGive(ItemList itemList, boolean unlimited, CommitType commitType)
	{
		for(ItemStack itemStack : itemList.items)
		{
			if(!give(itemStack, unlimited))
			{
				rollback();
				return false;
			}
		}
		useCommitType(commitType);
		return true;
	}

	default Optional<ItemList> tryProvide(ItemList itemList, boolean unlimited, CommitType commitType)
	{
		List<ItemStack> stacks = new ArrayList<>();
		for(ItemStack itemStack : itemList.items)
		{
			Optional<ItemStack> provided = provide(itemStack, unlimited);
			if(provided.isPresent())
			{
				stacks.add(provided.get());
			}
			else
			{
				rollback();
				return Optional.empty();
			}

		}
		useCommitType(commitType);
		return Optional.of(new ItemList(stacks));
	}

	default boolean tryAdd(ItemList itemList, boolean unlimited, CommitType commitType)
	{
		for(ItemStack itemStack : itemList.items)
		{
			if(!add(itemStack, unlimited))
			{
				rollback();
				return false;
			}
		}
		useCommitType(commitType);
		return true;
	}

	<T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException;
}