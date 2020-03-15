package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;

public record TransporterBlueprint(int range, int amount)
{
	public static TransporterBlueprint create(JrsObject data)
	{
		int range = ((JrsNumber) data.get("Range")).getValue().intValue();
		int amount = ((JrsNumber) data.get("Amount")).getValue().intValue();
		return new TransporterBlueprint(range, amount);
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1) throws IOException
	{
		a1.put("Range", range);
		a1.put("Amount", amount);
		a1.end();
	}
}