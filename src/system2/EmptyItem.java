package system2;

import java.util.*;
import system2.content.*;

public class EmptyItem extends AttackItem2
{
	public static final EmptyItem INSTANCE = new EmptyItem();

	public EmptyItem()
	{
		super(-1, 0, 0, 0, 0, 0, List.of());
		attackModes = List.of(new StandardMode(this));
	}
}