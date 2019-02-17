package item;

import javafx.scene.image.*;

public interface Item
{
	Image image();

	int weight();

	default boolean canContain(Item item)
	{
		return equals(item);
	}
}