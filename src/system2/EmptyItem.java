package system2;

import java.util.*;
import javafx.scene.image.*;
import system2.content.*;

public class EmptyItem extends AttackItem2
{
	public static final EmptyItem INSTANCE = new EmptyItem();

	public EmptyItem()
	{
		super(-1, 0, 0, 0, 0, 0, List.of());
		attackModes = List.of(new StandardMode(this));
	}

	@Override
	public Image image()
	{
		return null;
	}
}