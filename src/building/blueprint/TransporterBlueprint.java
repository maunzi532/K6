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

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		return a1.put("Range", range).put("Amount", amount);
	}
}