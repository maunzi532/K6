package vis.keybind;

import java.util.*;
import logic.*;

public final class XKeyBuilder
{
	public List<String> functions;
	public boolean canClick;
	public boolean canDrag;

	public XKeyBuilder()
	{
		functions = new ArrayList<>();
	}

	public XKey finish()
	{
		return new XKey(functions, canClick, canDrag);
	}
}