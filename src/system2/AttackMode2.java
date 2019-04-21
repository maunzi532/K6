package system2;

import entity.*;
import javafx.scene.image.*;

public class AttackMode2<T extends AttackItem2> implements XMode
{
	private T item;

	public AttackMode2(T item)
	{
		this.item = item;
	}

	@Override
	public Image image()
	{
		return new Image("BLUE.png");
	}

	public int getDamage()
	{
		return item.getDamage();
	}

	public int getHeavy()
	{
		return item.getHeavy();
	}

	public int getAccuracy()
	{
		return item.getAccuracy();
	}

	public int getCrit()
	{
		return item.getCrit();
	}

	public int attackCount()
	{
		return 2;
	}

	public int[] getRanges(boolean counter)
	{
		return item.getRanges(counter);
	}
}