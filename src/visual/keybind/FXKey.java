package visual.keybind;

import java.util.*;
import logic.*;

public class FXKey implements XKey
{
	public List<String> functions;
	public boolean canClick;
	public boolean canDrag;

	public FXKey()
	{
		functions = new ArrayList<>();
	}

	@Override
	public boolean hasFunction(String function)
	{
		return functions.contains(function);
	}

	@Override
	public boolean canClick()
	{
		return canClick;
	}

	@Override
	public boolean canDrag()
	{
		return canDrag;
	}
}