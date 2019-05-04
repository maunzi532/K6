package system2.content;

import java.util.*;
import javafx.scene.image.*;
import system2.*;

public class SpellItem extends AttackItem2
{
	private SpellItem(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		this(code, damage, heavy, slow, accuracy, crit, abilities, new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6});
	}

	private SpellItem(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(code, damage, heavy, slow, accuracy, crit, abilities, ranges, counterR);
		attackModes = List.of(new StandardMode(this));
	}

	@Override
	public int getAdvType()
	{
		return 4;
	}

	@Override
	public boolean magical()
	{
		return true;
	}

	@Override
	public Image image()
	{
		return new Image("SpellItem.png");
	}

	public static SpellItem create(int code, int damage, int heavy, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>(Arrays.asList(extraAbilities));
		return new SpellItem(code, damage, heavy, slow, accuracy, crit, abilities);
	}

	public static SpellItem standard()
	{
		return create(400, 9, 14, 2, 80, 0);
	}
}