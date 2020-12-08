package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import load.*;
import system.*;

public record TagStack(ItemStack items, String tag) implements XSaveableS
{
	public TagStack(ItemStack items)
	{
		this(items, null);
	}

	public TagStack(Item item, int count, String tag)
	{
		this(new ItemStack(item, count), tag);
	}

	public static TagStack load(JrsObject data, SystemScheme systemScheme)
	{
		Item item = systemScheme.getItem(data.get("Item").asText());
		int count = LoadHelper.asInt(data.get("Count"));
		String tag = LoadHelper.asOptionalString(data.get("Tag"));
		return new TagStack(item, count, tag);
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