package statsystem;

import item.*;
import java.util.*;
import java.util.stream.*;
import statsystem.content.*;

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
	private final List<Ability2> abilities;
	private final List<AttackMode> attackModes4;

	public AttackItem(AI2Class itemClass, String image, int damage, int heavy, int accuracy,
			int crit, int slow, int adaptive, AdaptiveType adaptiveType, AdvantageType advantageType,
			DefenseType defenseType, int[] ranges, int[] counterR, List<Ability2> abilities, List<AM2Type> attackModes)
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
	public String nameForAbility()
	{
		return "Equip";
	}

	@Override
	public List<Ability2> abilities()
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

	public List<Ability2> getAbilities()
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
		List<String> list = new ArrayList<>();
		list.add(defenseType.text + "\n" + itemClass.getClass().getSimpleName().replace("Item", ""));
		list.add("Range\n" + displayRange(ranges));
		list.add("Counter\n" + displayRange(counterR));
		list.add("Adv. Type\n" + advantageType.name);
		if(adaptive > 0)
		{
			list.add("Adaptive\n" + adaptiveType.name + " " + adaptive);
		}
		list.addAll(detailedInfo(false));
		return list;
	}

	public static String displayRange(int[] ranges)
	{
		if(ranges.length == 0)
			return "-";
		List<String> collected = new ArrayList<>();
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
					collected.add(String.valueOf(start));
				else
					collected.add(start + "-" + current);
				start = ranges[i];
			}
		}
		if(start == current)
			collected.add(String.valueOf(start));
		else
			collected.add(start + "-" + current);
		return String.join(", ", collected);
	}
}