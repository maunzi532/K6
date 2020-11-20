package item4;

import com.fasterxml.jackson.jr.stree.*;
import load.*;
import system4.*;

public record TagStack4(ItemStack4 items, String tag)
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
		String tag;
		JrsValue v1 = data.get("Tag");
		if(v1 instanceof JrsString v2)
			tag = v2.asText();
		else
			tag = null;
		return new TagStack4(item, count, tag);
	}
}