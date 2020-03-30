package logic;

import java.util.*;

public record XKey(List<String> functions, boolean canClick, boolean canDrag)
{
	public boolean hasFunction(String function)
	{
		return functions.contains(function);
	}
}