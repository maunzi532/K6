package system4;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item4.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import load.*;

public class SystemScheme
{
	public final List<Item4> allItems;
	private final Map<String, Item4> items;
	public final List<XClass4> allXClasses;
	private final Map<String, XClass4> xClasses;

	public SystemScheme(List<Item4> allItems, List<XClass4> allXClasses)
	{
		this.allItems = allItems;
		items = allItems.stream().collect(Collectors.toMap(e -> e.name().toString(), e -> e));
		this.allXClasses = allXClasses;
		xClasses = allXClasses.stream().collect(Collectors.toMap(e -> e.visItem().name().toString(), e -> e));
	}

	public Item4 getItem(String name)
	{
		return items.get(name);
	}

	public String saveItem(Item4 item)
	{
		return item.name().toString();
	}

	public XClass4 getXClass(String name)
	{
		return xClasses.get(name);
	}

	public String saveXClass(XClass4 xClass)
	{
		return xClass.visItem().name().toString();
	}

	public static SystemScheme load(JrsObject data)
	{
		List<Item4> allItems = LoadHelper.asList(data.get("Items"), EquipableItem4::load);
		Map<String, Item4> items1 = allItems.stream().collect(Collectors.toMap(e -> e.name().toString(), e -> e)); //TODO
		List<XClass4> allXClasses = LoadHelper.asList(data.get("Classes"), e -> XClass4.load(e, items1));
		return new SystemScheme(allItems, allXClasses);
	}

	public static void saveObject(String key, XSaveable save,
			ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		var a2 = a1.startObjectField(key);
		save.save(a2, systemScheme);
		a2.end();
	}

	public static void saveList(String key, List<? extends XSaveable> save,
			ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		var a2 = a1.startArrayField(key);
		for(XSaveable saveable : save)
		{
			var a3 = a2.startObject();
			saveable.save(a3, systemScheme);
			a3.end();
		}
		a2.end();
	}
}