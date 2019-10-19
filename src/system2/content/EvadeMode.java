package system2.content;

import java.util.*;
import system2.*;

public class EvadeMode implements AM2Type
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
		throw new RuntimeException();
	}

	@Override
	public List<String> info()
	{
		throw new RuntimeException();
	}
}