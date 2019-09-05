package system2;

import com.fasterxml.jackson.jr.ob.comp.*;
import item.*;
import java.io.*;
import java.util.*;

public abstract class AttackItem2 implements Item
{
	private final int code;
	private final int damage;
	private final int heavy;
	private final int slow;
	private final int accuracy;
	private final int crit;
	private final int[] ranges;
	private final int[] counterR;
	private final List<Ability2> abilities;
	protected List<AttackMode2> attackModes;

	public AttackItem2(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities,
			int... ranges)
	{
		this(code, damage, heavy, slow, accuracy, crit, abilities, ranges, ranges);
	}

	public AttackItem2(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities,
			int[] ranges, int[] counterR)
	{
		this.code = code;
		this.damage = damage;
		this.heavy = heavy;
		this.slow = slow;
		this.accuracy = accuracy;
		this.crit = crit;
		this.abilities = abilities;
		this.ranges = ranges;
		this.counterR = counterR;
	}

	public List<AttackMode2> attackModes()
	{
		return attackModes;
	}

	@Override
	public int weight()
	{
		return 1;
	}

	@Override
	public boolean canContain(Item item)
	{
		return false;
	}

	public int getDamage()
	{
		return damage;
	}

	public int getHeavy()
	{
		return heavy;
	}

	public int getSlow()
	{
		return slow;
	}

	public int getAccuracy()
	{
		return accuracy;
	}

	public int getCrit()
	{
		return crit;
	}

	public List<Ability2> getAbilities()
	{
		return abilities;
	}

	public int[] getRanges(boolean counter)
	{
		return counter ? counterR : ranges;
	}

	public int getAdvType()
	{
		return 0;
	}

	public boolean magical()
	{
		return false;
	}

	@Override
	public List<Integer> save()
	{
		return List.of(1, code);
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		return a1.put("AttackItemCode", code);
	}

	@Override
	public List<String> info()
	{
		List<String> info = new ArrayList<>();
		info.add("Type\n" + getClass().getSimpleName().replace("Item", ""));
		info.add("Damage\n" + damage);
		info.add("Heavy\n" + heavy);
		info.add(slow > 0 ? "Slow\n" + slow : "");
		info.add("Acc%\n" + accuracy);
		info.add("Crit%\n" + crit);
		info.add("Range\n" + displayRange(ranges));
		info.add("Counter\n" + displayRange(counterR));
		info.add("");
		for(Ability2 ability : abilities)
		{
			info.add("Ability\n" + ability.name);
		}
		return info;
	}

	private static String displayRange(int[] ranges)
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