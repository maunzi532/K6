package system2.animation;

import arrow.*;
import entity.analysis.*;
import java.util.*;
import system2.*;
import system2.analysis.*;

public class AttackAnim extends AnimationArrow
{
	private RNGDivider divider;
	private System2 system;
	private List<String> events;
	private int eventCounter;

	public AttackAnim(RNGDivider2 divider, System2 system)
	{
		this.divider = divider;
		this.system = system;
		events = divider.getEvents();
	}

	@Override
	public void tick()
	{
		super.tick();
		if(linked.isEmpty())
		{
			linked.add(system.animationForEvent(events.get(eventCounter), divider));
		}
	}
}