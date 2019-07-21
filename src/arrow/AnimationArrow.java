package arrow;

import java.util.*;

public abstract class AnimationArrow
{
	protected List<AnimationArrow> linked;
	protected XArrow arrow;

	public AnimationArrow()
	{
		linked = new ArrayList<>();
	}

	public boolean finished()
	{
		return finished2() && (arrow == null || arrow.finished()) && linked.stream().allMatch(AnimationArrow::finished);
	}

	public boolean finished2()
	{
		return true;
	}

	public void tick()
	{
		linked.forEach(AnimationArrow::tick);
	}
}