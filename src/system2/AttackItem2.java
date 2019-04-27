package system2;

import item.*;
import java.util.*;
import javafx.scene.image.*;

public class AttackItem2 implements Item
{
	private final int damage;
	private final int heavy;
	private final int accuracy;
	private final int crit;
	private final int[] ranges;
	private final int[] counterR;
	private final List<Ability2> abilities;

	public static AttackItem2 item2()
	{
		return new AttackItem2(11, 7, 70, 5, List.of(), 1);
	}

	public AttackItem2(int damage, int heavy, int accuracy, int crit, List<Ability2> abilities,
			int... ranges)
	{
		this(damage, heavy, accuracy, crit, abilities, ranges, ranges);
	}

	public AttackItem2(int damage, int heavy, int accuracy, int crit, List<Ability2> abilities,
			int[] ranges, int[] counterR)
	{
		this.damage = damage;
		this.heavy = heavy;
		this.accuracy = accuracy;
		this.crit = crit;
		this.abilities = abilities;
		this.ranges = ranges;
		this.counterR = counterR;
	}

	public List<AttackMode2> attackModes()
	{
		return List.of(new AttackMode2<>(this, abilities));
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
}