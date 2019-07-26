package system2.animation;

import arrow.*;
import entity.*;
import levelMap.*;

public class AnimPartAttack implements AnimPart
{
	private static final int LEN = 60;
	private static final int FORWARD = 40;
	private static final int BACKWARD = 20;

	private final XEntity attacker;
	private final XEntity target;
	private final LevelMap levelMap;
	private ManualXArrow arrow;
	private int counter;

	public AnimPartAttack(XEntity attacker, XEntity target, LevelMap levelMap)
	{
		this.attacker = attacker;
		this.target = target;
		this.levelMap = levelMap;
		arrow = new ManualXArrow(XArrow.convert(attacker.location(), target.location()), LEN, attacker.getImage());
		levelMap.addArrow(arrow);
		attacker.setReplacementArrow(arrow);
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
			arrow.setCounter(FORWARD * 2 - counter);
		else
			arrow.setCounter(counter);
		return false;
	}
}