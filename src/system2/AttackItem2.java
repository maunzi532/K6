package system2;

import item.*;
import java.util.*;
import javafx.scene.image.*;

public abstract class AttackItem2 implements Item
{
	private final int damage;
	private final int heavy;
	private final int slow;
	private final int accuracy;
	private final int crit;
	private final int[] ranges;
	private final int[] counterR;
	private final List<Ability2> abilities;
	protected List<AttackMode2> attackModes;

	public AttackItem2(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities,
			int... ranges)
	{
		this(damage, heavy, slow, accuracy, crit, abilities, ranges, ranges);
	}

	public AttackItem2(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities,
			int[] ranges, int[] counterR)
	{
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
	public Image image()
	{
		return new Image("BLUE.png");
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
}