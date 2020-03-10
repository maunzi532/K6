package item.inv;

import item.ItemStack;
import java.util.Optional;

public interface Inv0
{
	boolean ok();

	void commit();

	void rollback();

	boolean canGive(ItemStack itemStack, boolean unlimited);

	boolean give(ItemStack itemStack, boolean unlimited);

	Optional<ItemStack> wouldProvide(ItemStack itemStack, boolean unlimited);

	Optional<ItemStack> provide(ItemStack itemStack, boolean unlimited);

	boolean canAdd(ItemStack itemStack, boolean unlimited);

	boolean add(ItemStack itemStack, boolean unlimited);
}