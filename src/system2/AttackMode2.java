package system2;

import entity.*;
import java.util.*;
import javafx.scene.image.*;
import system2.content.*;

public class AttackMode2 implements XMode
{
	public final AttackItem2 item;
	public final int code;
	public final AM2Type type;

	public AttackMode2(AttackItem2 item, AM2Type type)
	{
		this.item = item;
		code = type.code();
		this.type = type;
	}

	@Override
	public Image image()
	{
		return item.image();
	}

	@Override
	public String tile()
	{
		return type.tile();
	}

	@Override
	public List<String> info()
	{
		return type.info();
	}

	public int getDamage()
	{
		return item.getDamage();
	}

	public int getHeavy()
	{
		return item.getHeavy();
	}

	public int getAdaptive()
	{
		return item.getAdaptive();
	}

	public AdaptiveType getAdaptiveType()
	{
		return item.getAdaptiveType();
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

	public int attackCount()
	{
		return 2;
	}

	public List<Ability2> getAllAbilities(Stats2 stats)
	{
		ArrayList<Ability2> allAbilities = new ArrayList<>();
		//allAbilities.addAll(stats)
		allAbilities.addAll(item.getAbilities());
		allAbilities.addAll(type.abilities());
		return allAbilities;
	}

	public int[] getRanges(boolean counter)
	{
		return item.getRanges(counter);
	}

	public AdvantageType getAdvType()
	{
		return item.getAdvType();
	}

	public boolean magical()
	{
		return item.magical();
	}
}