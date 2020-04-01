package statsystem.content;

import java.util.*;
import statsystem.*;

public final class FinesseMode implements AM2Type
{
	public static final FinesseMode INSTANCE = new FinesseMode();

	@Override
	public int code()
	{
		return 2;
	}

	@Override
	public List<XAbility> abilities()
	{
		return List.of(XAbility.TWO_HANDED);
	}

	@Override
	public CharSequence tile()
	{
		return "attackitem.mode.finesse";
	}
}