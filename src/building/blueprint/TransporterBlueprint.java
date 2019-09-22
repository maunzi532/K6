package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import file.*;
import java.io.*;

public class TransporterBlueprint
{
	public final int range;
	public final int amount;

	public TransporterBlueprint(int range, int amount)
	{
		this.range = range;
		this.amount = amount;
	}

	public TransporterBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		range = node.get(0).dataInt();
		amount = node.get(1).dataInt();
	}

	public TransporterBlueprint(JrsObject data)
	{
		range = ((JrsNumber) data.get("Range")).getValue().intValue();
		amount = ((JrsNumber) data.get("Amount")).getValue().intValue();
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		return a1.put("Range", range).put("Amount", amount);
	}
}