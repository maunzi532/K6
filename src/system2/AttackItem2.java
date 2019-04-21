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

	public static AttackItem2 item2()
	{
		return new AttackItem2(5, 1, 70, 5, 1);
	}

	public AttackItem2(int damage, int heavy, int accuracy, int crit, int... ranges)
	{
		this(damage, heavy, accuracy, crit, ranges, ranges);
	}

	public AttackItem2(int damage, int heavy, int accuracy, int crit, int[] ranges, int[] counterR)
	{
		this.damage = damage;
		this.heavy = heavy;
		this.accuracy = accuracy;
		this.crit = crit;
		this.ranges = ranges;
		this.counterR = counterR;
	}

	public List<AttackMode2> attackModes()
	{
		return List.of();
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

	public int[] getRanges(boolean counter)
	{
		return counter ? counterR : ranges;
	}
}