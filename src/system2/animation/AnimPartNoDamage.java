package system2.animation;

import arrow.*;
import entity.*;
import levelMap.*;

public class AnimPartNoDamage implements AnimPart
{
	private static final int DURATION = 40;

	private final XEntity target;
	private final LevelMap levelMap;
	private final boolean crit;
	private XArrow arrow;
	private int counter;

	public AnimPartNoDamage(XEntity target, boolean crit, LevelMap levelMap)
	{
		this.target = target;
		this.crit = crit;
		this.levelMap = levelMap;
		//arrow = new BlinkArrow(target.location(), DURATION, false, target.getImage(), BLINKTIME);
	}

	@Override
	public boolean finished1()
	{
		return false;
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
		//if(counter == AnimPartAttack.DODGETIME)
		//{
			/*levelMap.addArrow(arrow);
			target.setReplacementArrow(arrow);*/
		//}
		return true;//arrow.finished();
	}
}