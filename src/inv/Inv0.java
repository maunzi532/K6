package inv;

import java.util.Optional;

public interface Inv0
{
	boolean canGive(ItemStack itemStack, boolean unlimited);

	boolean give(ItemStack itemStack, boolean unlimited);

	Optional<ItemStack> wouldProvide(ItemStack itemStack, boolean unlimited);

	Optional<ItemStack> provide(ItemStack itemStack, boolean unlimited);

	boolean canAdd(ItemStack itemStack, boolean unlimited);

	boolean add(ItemStack itemStack, boolean unlimited);
}