package system2;

import entity.*;
import java.util.*;
import javafx.scene.image.*;

public class AttackMode2<T extends AttackItem2> implements XMode
{
	private T item;
	private List<Ability2> abilities;

	public AttackMode2(T item, List<Ability2> abilities)
	{
		this.item = item;
		this.abilities = abilities;
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
}