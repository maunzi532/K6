package system;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import load.*;

public record Modifier4(Stats4 stat, ModifierType4 type, int amount) implements XSaveableS
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

	public static Modifier4 load(JrsObject data)
	{
		Stats4 stat = Stats4.valueOf(data.get("Stat").asText());
		ModifierType4 type = ModifierType4.valueOf(data.get("Type").asText());
		int amount = LoadHelper.asInt(data.get("Amount"));
		return new Modifier4(stat, type, amount);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		a1.put("Stat", stat.name());
		a1.put("Type", type.name());
		a1.put("Amount", amount);
	}
}