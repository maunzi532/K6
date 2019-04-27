package system2;

import entity.*;
import java.util.*;
import javafx.scene.image.*;

public abstract class AttackMode2 implements XMode
{
	protected AttackItem2 item;
	protected List<Ability2> abilities;

	public AttackMode2(AttackItem2 item)
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

	public int getSlow()
	{
		return item.getSlow();
	}

	public int getAccuracy()
	{
		return item.getAccuracy();
	}

	public int getCrit()
	{
		return item.getCrit();
	}

	public int getDefense()
	{
		return 0;
	}

	public int getMagicDef()
	{
		return 0;
	}

	public int attackCount()
	{
		return 2;
	}

	public List<Ability2> getAllAbilities(Stats2 stats)
	{
		ArrayList<Ability2> allAbilities = new ArrayList<>();
		//allAbilities.addAll(stats)
		allAbilities.addAll(item.getAbilities());
		allAbilities.addAll(abilities);
		return allAbilities;
	}

	public int[] getRanges(boolean counter)
	{
		return item.getRanges(counter);
	}

	public int getAdvType()
	{
		return item.getAdvType();
	}

	public boolean magical()
	{
		return item.magical();
	}
}