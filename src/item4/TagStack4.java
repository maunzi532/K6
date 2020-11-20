package item4;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import load.*;
import system4.*;

public record TagStack4(ItemStack4 items, String tag) implements XSaveable
{
	public TagStack4(ItemStack4 items)
	{
		this(items, null);
	}

	public TagStack4(Item4 item, int count, String tag)
	{
		this(new ItemStack4(item, count), tag);
	}

	public static TagStack4 load(JrsObject data, SystemScheme systemScheme)
	{
		Item4 item = systemScheme.getItem(data.get("Item").asText());
		int count = LoadHelper.asInt(data.get("Count"));
		String tag = LoadHelper.asOptionalString(data.get("Tag"));
		return new TagStack4(item, count, tag);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		a1.put("Item", systemScheme.saveItem(items.item()));
		a1.put("Count", items.count());
		if(tag != null)
			a1.put("Tag", tag);
	}
}