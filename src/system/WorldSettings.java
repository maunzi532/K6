package system;

import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.util.*;
import java.util.stream.*;
import load.*;

public class WorldSettings implements AllItemsList
{
	public final List<Item> allItems;
	private final Map<String, Item> items;
	public final List<XClass> allXClasses;
	private final Map<String, XClass> xClasses;

	public WorldSettings(List<Item> allItems, List<XClass> allXClasses)
	{
		this.allItems = allItems;
		items = allItems.stream().collect(Collectors.toMap(e -> e.name().toString(), e -> e));
		this.allXClasses = allXClasses;
		xClasses = allXClasses.stream().collect(Collectors.toMap(e -> e.visItem().name().toString(), e -> e));
	}

	@Override
	public Item getItem(String name)
	{
		if(!items.containsKey(name))
			throw new RuntimeException("Item \"" + name + "\" does not exist");
		return items.get(name);
	}

	@Override
	public String saveItem(Item item)
	{
		return item.name().toString();
	}

	public XClass getXClass(String name)
	{
		return xClasses.get(name);
	}

	public String saveXClass(XClass xClass)
	{
		return xClass.visItem().name().toString();
	}

	public static WorldSettings load(JrsObject data)
	{
		List<Item> allItems = LoadHelper.asListNum(data.get("Items"), EquipableItem::load);
		Map<String, Item> items1 = allItems.stream().collect(Collectors.toMap(e -> e.name().toString(), e -> e)); //TODO
		List<XClass> allXClasses = LoadHelper.asList(data.get("Classes"), e -> XClass.load(e, items1));
		return new WorldSettings(allItems, allXClasses);
	}
}