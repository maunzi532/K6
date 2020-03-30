package statsystem;

import java.util.*;

public final class EvadeMode implements AM2Type
{
	public static final EvadeMode INSTANCE = new EvadeMode();

	@Override
	public int code()
	{
		return 0;
	}

	@Override
	public List<Ability2> abilities()
	{
		return List.of();
	}

	@Override
	public String tile()
	{
		throw new RuntimeException("EvadeMode cannot be displayed");
	}
}