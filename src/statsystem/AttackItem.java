package statsystem;

import item.*;
import java.util.*;
import java.util.stream.*;
import statsystem.content.*;
import text.*;

public final class AttackItem implements Item, ModifierAspect
{
	public final AI2Class itemClass;
	private final String image;
	private final int damage;
	private final int heavy;
	private final int accuracy;
	private final int crit;
	private final int slow;
	private final int adaptive;
	private final AdaptiveType adaptiveType;
	private final AdvantageType advantageType;
	private final DefenseType defenseType;
	private final int[] ranges;
	private final int[] counterR;
	private final List<XAbility> abilities;
	private final List<AttackMode> attackModes4;

	public AttackItem(AI2Class itemClass, String image, int damage, int heavy, int accuracy,
			int crit, int slow, int adaptive, AdaptiveType adaptiveType, AdvantageType advantageType,
			DefenseType defenseType, int[] ranges, int[] counterR, List<XAbility> abilities, List<AM2Type> attackModes)
	{
		this.itemClass = itemClass;
		this.image = image;
		this.damage = damage;
		this.heavy = heavy;
		this.accuracy = accuracy;
		this.crit = crit;
		this.slow = slow;
		this.adaptive = adaptive;
		this.adaptiveType = adaptiveType;
		this.advantageType = advantageType;
		this.defenseType = defenseType;
		this.ranges = ranges;
		this.counterR = counterR;
		this.abilities = abilities;
		attackModes4 = attackModes.stream().map(e -> new AttackMode(this, e)).collect(Collectors.toList());
	}

	@Override
	public boolean canContain(Item item)
	{
		return false;
	}

	@Override
	public int weight()
	{
		return 1;
	}

	@Override
	public String image()
	{
		return image;
	}

	@Override
	public CharSequence nameForAbility()
	{
		return "modifier.name.item";
	}

	@Override
	public List<XAbility> abilities()
	{
		return abilities;
	}

	@Override
	public int heavy()
	{
		return heavy;
	}

	@Override
	public int attackPower()
	{
		return damage;
	}

	@Override
	public int speedMod()
	{
		return -slow;
	}

	@Override
	public int accuracy()
	{
		return accuracy;
	}

	@Override
	public int crit()
	{
		return crit;
	}

	public int getAdaptive()
	{
		return adaptive;
	}

	public AdaptiveType getAdaptiveType()
	{
		return adaptiveType;
	}

	public AdvantageType getAdvantageType()
	{
		return advantageType;
	}

	public DefenseType defenseType()
	{
		return defenseType;
	}

	public int[] getRanges(AttackSide side)
	{
		return switch(side)
				{
					case INITIATOR -> ranges;
					case TARGET -> counterR;
				};
	}

	public List<XAbility> getAbilities()
	{
		return abilities;
	}

	public List<AttackMode> attackModes()
	{
		return attackModes4;
	}

	@Override
	public List<? extends CharSequence> info()
	{
		List<CharSequence> list = new ArrayList<>();
		list.add(MultiText.lines(itemClass.name(), defenseType.text));
		list.add(new ArgsText("attackitem.ranges", displayRange(ranges)));
		list.add(new ArgsText("attackitem.counterranges", displayRange(counterR)));
		list.add(MultiText.lines("attackitem.advantage", advantageType.name));
		if(adaptive > 0)
		{
			list.add(MultiText.lines("attackitem.adaptive", new ArgsText(adaptiveType.name.toString(), adaptive)));
		}
		list.addAll(detailedInfo(false));
		return list;
	}

	public static CharSequence displayRange(int[] ranges)
	{
		if(ranges.length == 0)
			return "range.none";
		List<CharSequence> collected = new ArrayList<>();
		int start = ranges[0];
		int current = start;
		for(int i = 1; i < ranges.length; i++)
		{
			if(ranges[i] == current + 1)
			{
				current++;
			}
			else
			{
				if(start == current)
					collected.add(new ArgsText("range.one", start));
				else
					collected.add(new ArgsText("range.range", start, current));
				start = ranges[i];
			}
		}
		if(start == current)
			collected.add(new ArgsText("range.one", start));
		else
			collected.add(new ArgsText("range.range", start, current));
		return new MultiText(collected, MultiTextConnect.LISTED);
	}
}