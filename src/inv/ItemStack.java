package inv;

import file.*;

public class ItemStack
{
	public final Item item;
	public final int count;

	public ItemStack(Item item, int count)
	{
		this.item = item;
		this.count = count;
	}

	public ItemStack(BlueprintNode node)
	{
		if(node.size() == 0)
		{
			item = Items.valueOf(node.data);
			count = 1;
		}
		else
		{
			if(!node.data.equals(getClass().getSimpleName()))
				throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
			item = Items.valueOf(node.get(0).data);
			count = node.get(1).dataInt();
		}
	}
}