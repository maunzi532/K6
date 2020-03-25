package item;

import java.util.*;

public interface Item
{
	String image();

	int weight();

	default boolean canContain(Item item)
	{
		return equals(item);
	}

	List<String> info();
}