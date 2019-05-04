package item;

import java.util.*;
import javafx.scene.image.*;

public interface Item
{
	Image image();

	int weight();

	default boolean canContain(Item item)
	{
		return equals(item);
	}

	List<String> info();

	List<Integer> save();
}