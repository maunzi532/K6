package system2.animation;

import arrow.*;
import entity.*;

public class AnimPartAttack implements AnimPart
{
	public static final int DODGETIME = 10;
	private static final int LEN = 30;
	private static final int FORWARD = 20;
	private static final int BACKWARD = 10;

	private final XEntity attacker;
	private final XEntity target;
	private ManualXArrow arrow;
	private int counter;

	public AnimPartAttack(XEntity attacker, XEntity target, Arrows arrows)
	{
		this.attacker = attacker;
		this.target = target;
		arrow = new ManualXArrow(XArrow.convert(attacker.location(), target.location()), LEN, attacker.getImage());
		arrows.addArrow(arrow);
		attacker.setReplacementArrow(arrow);
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