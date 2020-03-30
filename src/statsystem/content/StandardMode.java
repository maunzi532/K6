package statsystem.content;

import java.util.*;
import statsystem.*;

public final class StandardMode implements AM2Type
{
	public static final StandardMode INSTANCE = new StandardMode();

	@Override
	public int code()
	{
		return 1;
	}

	@Override
	public List<Ability2> abilities()
	{
		return List.of();
	}

	@Override
	public String tile()
	{
		return "Standard";
	}
}