package system2.animation;

import arrow.*;
import entity.*;

public class AnimPartDodge implements AnimPart
{
	private static final int LEN = 15;
	private static final int FORWARD = 10;
	private static final int BACKWARD = 10;

	private final XEntity attacker;
	private final XEntity target;
	private final int distance;
	private ManualXArrow arrow;
	private int counter;

	public AnimPartDodge(XEntity attacker, XEntity target, int distance, Arrows arrows)
	{
		this.attacker = attacker;
		this.target = target;
		this.distance = distance;
		arrow = new ManualXArrow(XArrow.convert(target.location(), attacker.location()), LEN, target.getImage());
		arrows.addArrow(arrow);
		target.setReplacementArrow(arrow);
	}

	@Override
	public boolean finished1()
	{
		return counter >= FORWARD;
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
			arrow.setCounter((counter - FORWARD * 2) / distance);
		else
			arrow.setCounter(-counter / distance);
		return false;
	}
}