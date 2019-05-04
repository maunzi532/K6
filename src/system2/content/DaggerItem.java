package system2.content;

import java.util.*;
import javafx.scene.image.*;
import system2.*;

public class DaggerItem extends AttackItem2
{
	private DaggerItem(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		this(code, damage, heavy, slow, accuracy, crit, abilities, new int[]{1}, new int[]{1, 2});
	}

	private DaggerItem(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(code, damage, heavy, slow, accuracy, crit, abilities, ranges, counterR);
		attackModes = List.of(new FinesseMode(this));
	}

	@Override
	public int getAdvType()
	{
		return 1;
	}

	@Override
	public Image image()
	{
		return new Image("DaggerItem.png");
	}

	public static DaggerItem create(int code, int damage, int heavy, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>();
		abilities.add(Ability2.FAST);
		abilities.addAll(Arrays.asList(extraAbilities));
		return new DaggerItem(code, damage, heavy, slow, accuracy, crit, abilities);
	}

	public static DaggerItem standard()
	{
		return create(100, 6, 6, 0, 80, 10);
	}
}