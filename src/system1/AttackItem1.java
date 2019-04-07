package system1;

import item.*;
import javafx.scene.image.*;

public class AttackItem1 implements Item
{
	private final int attack;
	private final int slowdown;
	private final int accuracy;
	private final int crit;
	private final int[] ranges;

	public static AttackItem1 item1()
	{
		return new AttackItem1(5, 1, 70, 5, 1);
	}

	public AttackItem1(int attack, int slowdown, int accuracy, int crit, int... ranges)
	{
		this.attack = attack;
		this.slowdown = slowdown;
		this.accuracy = accuracy;
		this.crit = crit;
		this.ranges = ranges;
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

	public int getAttack()
	{
		return attack;
	}

	public int getSlowdown()
	{
		return slowdown;
	}

	public int getAccuracy()
	{
		return accuracy;
	}

	public int getCrit()
	{
		return crit;
	}

	public int[] getRanges()
	{
		return ranges;
	}
}