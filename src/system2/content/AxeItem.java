package system2.content;

import java.util.*;
import javafx.scene.image.*;
import system2.*;

public class AxeItem extends AttackItem2
{
	private static final Image IMAGE = new Image("AxeItem.png");

	private AxeItem(int code, int damage, int heavy, int adaptive, AdaptiveType adaptiveType, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		this(code, damage, heavy, adaptive, adaptiveType, slow, accuracy, crit, abilities, new int[]{1}, new int[]{1, 2});
	}

	private AxeItem(int code, int damage, int heavy, int adaptive, AdaptiveType adaptiveType, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(code, damage, heavy, adaptive, adaptiveType, slow, accuracy, crit, abilities, ranges, counterR);
		attackModes = List.of(new FinesseMode(this));
	}

	@Override
	public AdvantageType getAdvType()
	{
		return AdvantageType.AXE;
	}

	@Override
	public Image image()
	{
		return IMAGE;
	}

	public static AxeItem create(int code, int damage, int heavy, int adaptive, AdaptiveType adaptiveType, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>();
		abilities.add(Ability2.MELTING);
		abilities.addAll(Arrays.asList(extraAbilities));
		return new AxeItem(code, damage, heavy, adaptive, adaptiveType, slow, accuracy, crit, abilities);
	}
}