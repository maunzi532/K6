package system;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import load.*;

public record XModifier(XStats stat, XModifierType type, int amount) implements XSaveableS
{
	public int apply(int value)
	{
		return switch(type)
		{
			case ADD -> value + amount;
			case MULT -> value * amount;
			case DIV -> Math.floorDiv(value, amount);
			case SET -> amount;
		};
	}

	public static XModifier load(JrsObject data)
	{
		XStats stat = XStats.valueOf(data.get("Stat").asText());
		XModifierType type = XModifierType.valueOf(data.get("Type").asText());
		int amount = LoadHelper.asInt(data.get("Amount"));
		return new XModifier(stat, type, amount);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		a1.put("Stat", stat.name());
		a1.put("Type", type.name());
		a1.put("Amount", amount);
	}
}