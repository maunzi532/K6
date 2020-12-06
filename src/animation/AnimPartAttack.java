package animation;

import arrow.*;
import entity.*;

public final class AnimPartAttack implements AnimPart
{
	public static final int DODGETIME = 10;
	private static final int LEN = 30;
	private static final int FORWARD = 20;
	private static final int BACKWARD = 10;

	private final ManualXArrow arrow;
	private int counter;

	public AnimPartAttack(XCharacter attacker, XCharacter target, Arrows arrows)
	{
		arrow = new ManualXArrow(XArrow.convert(attacker.location(), target.location()), LEN, attacker.mapImageName());
		arrows.addArrow(arrow);
		attacker.replaceVisual(arrow);
	}

	@Override
	public boolean finished1()
	{
		return counter >= FORWARD - DODGETIME;
	}

	@Override
	public boolean finished2()
	{
		return false;
	}

	@Override
	public boolean tick()
	{
		counter++;
		if(counter >= FORWARD + BACKWARD)
		{
			arrow.remove();
			return true;
		}
		if(counter >= FORWARD)
			arrow.setCounter(FORWARD * 2 - counter);
		else
			arrow.setCounter(counter);
		return false;
	}
}