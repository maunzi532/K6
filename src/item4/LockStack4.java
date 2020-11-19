package item4;

public record LockStack4(ItemStack4 items, String locked)
{
	public LockStack4(ItemStack4 items)
	{
		this(items, null);
	}
}