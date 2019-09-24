package system2.content;

import java.util.*;
import system2.*;

public class StandardMode extends AttackMode2
{
	public StandardMode(AttackItem2 item)
	{
		super(item, 1);
		abilities = List.of();
	}
}