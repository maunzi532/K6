package system2.content;

import java.util.*;
import system2.*;

public class FinesseMode extends AttackMode2
{
	public FinesseMode(AttackItem2 item)
	{
		super(item, 1);
		abilities = List.of(Ability2.TWO_HANDED);
	}
}