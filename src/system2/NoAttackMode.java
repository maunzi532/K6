package system2;

import java.util.*;
import javafx.scene.image.*;
import system2.content.*;

public class NoAttackMode extends AttackMode2
{
	public NoAttackMode()
	{
		super(null, 0);
	}

	@Override
	public Image image()
	{
		return null;
	}

	@Override
	public int getDamage()
	{
		return 0;
	}

	@Override
	public int getHeavy()
	{
		return 0;
	}

	@Override
	public int getAdaptive()
	{
		return 0;
	}

	@Override
	public AdaptiveType getAdaptiveType()
	{
		return AdaptiveType.NONE;
	}

	@Override
	public int getSlow()
	{
		return 0;
	}

	@Override
	public int getAccuracy()
	{
		return 0;
	}

	@Override
	public int getCrit()
	{
		return 0;
	}

	@Override
	public int attackCount()
	{
		return 0;
	}

	@Override
	public List<Ability2> getAllAbilities(Stats2 stats)
	{
		ArrayList<Ability2> allAbilities = new ArrayList<>();
		//allAbilities.addAll(stats)
		return allAbilities;
	}

	@Override
	public int[] getRanges(boolean counter)
	{
		return new int[0];
	}

	@Override
	public AdvantageType getAdvType()
	{
		return AdvantageType.DEFEND;
	}

	@Override
	public boolean magical()
	{
		return false;
	}
}