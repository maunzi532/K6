package item;

import java.util.*;

public interface Item
{
	String imageName();

	int weight();

	default boolean canContain(Item item)
	{
		return equals(item);
	}

	List<String> info();
}