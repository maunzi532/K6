package system;

import arrow.*;
import entity.*;
import java.util.*;
import animation.*;

public final class AttackAnim4 implements AnimTimer
{
	private final ACResult4 result;
	private final Arrows arrows;
	private final XCharacter initiator;
	private final XCharacter target;
	private int eventCounter;
	private final List<AnimPart> linked;
	private final InfoArrow healthBar1;
	private final InfoArrow healthBar2;

	public AttackAnim4(ACResult4 result, Arrows arrows, XCharacter initiator, XCharacter target)
	{
		this.result = result;
		this.arrows = arrows;
		this.initiator = initiator;
		this.target = target;
		linked = new ArrayList<>();
		healthBar1 = new InfoArrow(initiator.location(), target.location(), result.hpBar1());
		arrows.addArrow(healthBar1);
		healthBar2 = new InfoArrow(target.location(), initiator.location(), result.hpBar2());
		arrows.addArrow(healthBar2);
	}

	@Override
	public boolean finished()
	{
		if(eventCounter >= result.animParts().size() && linked.stream().allMatch(AnimPart::finished2))
		{
			healthBar1.remove();
			healthBar2.remove();
			return true;
		}
		else
			return false;
	}

	@Override
	public void tick()
	{
		linked.removeIf(AnimPart::tick);
		if(eventCounter >= result.animParts().size())
			return;
		if(linked.stream().allMatch(AnimPart::finished1))
		{
			linked.add(result.animParts().get(eventCounter));
			eventCounter++;
		}
	}
}