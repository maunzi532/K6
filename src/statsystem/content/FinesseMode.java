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
	public List<Ability2> abilities()
	{
		return List.of(Ability2.TWO_HANDED);
	}

	@Override
	public String tile()
	{
		return "FinesseMode";
	}
}