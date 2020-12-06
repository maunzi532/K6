package animation;

import arrow.*;
import entity.*;

public final class AnimPartNoDamage implements AnimPart
{
	private static final int DURATION = 20;

	private final XCharacter target;
	private final Arrows arrows;
	private final boolean crit;
	private XArrow arrow;
	private int counter;

	public AnimPartNoDamage(XCharacter target, boolean crit, Arrows arrows)
	{
		this.target = target;
		this.crit = crit;
		this.arrows = arrows;
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