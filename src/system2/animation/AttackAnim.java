package system2.animation;

import arrow.*;
import entity.*;
import file.*;
import java.util.*;
import javafx.scene.paint.*;
import system2.*;
import system2.analysis.*;

public class AttackAnim implements AnimTimer
{
	private RNGDivider2 divider;
	private RNGDivider2 lastDivider;
	private final Arrows arrows;
	private final AttackInfo aI;
	private List<String> events;
	private int eventCounter;
	private final List<AnimPart> linked;
	private final InfoArrow healthBar1;
	private final InfoArrow healthBar2;

	public AttackAnim(RNGDivider2 divider, Arrows arrows, ColorScheme colorScheme)
	{
		this.divider = divider;
		lastDivider = divider;
		this.arrows = arrows;
		aI = divider.getAttackInfo();
		events = divider.getEvents();
		eventCounter = -1;
		linked = new ArrayList<>();
		Color backgroundColor = colorScheme.color("arrow.healthbar.background");
		Color textColor = colorScheme.color("arrow.healthbar.text");
		healthBar1 = new InfoArrow(aI.entity.location(), aI.entityT.location(),
				colorScheme.color(aI.entity.team().healthBarColor), backgroundColor, textColor,
				aI.getStats(false).currentHealth(), aI.getStats(false).maxHealth());
		healthBar2 = new InfoArrow(aI.entityT.location(), aI.entity.location(),
				colorScheme.color(aI.entityT.team().healthBarColor), backgroundColor, textColor,
				aI.getStats(true).currentHealth(), aI.getStats(true).maxHealth());
		arrows.addArrow(healthBar1);
		arrows.addArrow(healthBar2);
	}

	private InfoArrow healthBar(boolean inverse)
	{
		return inverse ? healthBar2 : healthBar1;
	}

	@Override
	public boolean finished()
	{
		if(divider != null)
			return false;
		healthBar1.remove();
		healthBar2.remove();
		return true;
	}

	@Override
	public void tick()
	{
		if(divider == null)
			return;
		linked.removeIf(AnimPart::tick);
		if(linked.stream().allMatch(AnimPart::finished1))
		{
			eventCounter++;
			if(eventCounter >= events.size())
			{
				if(linked.stream().allMatch(AnimPart::finished2))
				{
					lastDivider = divider;
					divider = (RNGDivider2) divider.rollRNG();
					if(divider == null)
						return;
					events = divider.getEvents();
					eventCounter = -1;
				}
			}
			else
			{
				String event = events.get(eventCounter);
				//System.out.println(event);
				boolean inverse = event.charAt(event.length() - 1) == '2';
				String eventType = event.substring(0, event.length() - 1);
				if(eventType.equals("defeated"))
					aI.getEntity(!inverse).setDefeated();
				startEvent(eventType, inverse);
			}
		}
	}

	private void startEvent(String eventType, boolean inverse)
	{
		switch(eventType)
		{
			case "healthcost" -> linked.add(new AnimPartHealthCost(aI.getCalc(inverse).healthCost,
						aI.getStats(inverse), healthBar(inverse).statBar()));
			case "attack" -> linked.add(new AnimPartAttack(aI.getEntity(inverse), aI.getEntity(!inverse), arrows));
			case "miss" -> linked.add(new AnimPartDodge(aI.getEntity(inverse), aI.getEntity(!inverse), aI.distance, arrows));
			case "hit" -> linked.add(new AnimPartHit(aI.getEntity(!inverse), aI.getStats(!inverse),
					aI.getCalc(inverse).damage, healthBar(!inverse).statBar(), false, false, arrows));
			case "melt" -> linked.add(new AnimPartHit(aI.getEntity(!inverse), aI.getStats(!inverse),
					aI.getCalc(inverse).meltDamage, healthBar(!inverse).statBar(), false, true, arrows));
			case "nodamage" -> linked.add(new AnimPartNoDamage(aI.getEntity(!inverse), false, arrows));
			case "defeated" -> linked.add(new AnimPartVanish(aI.getEntity(!inverse), arrows));
			case "crit" -> linked.add(new AnimPartHit(aI.getEntity(!inverse), aI.getStats(!inverse),
					aI.getCalc(inverse).critDamage, healthBar(!inverse).statBar(), true, false, arrows));
			case "meltcrit" -> linked.add(new AnimPartHit(aI.getEntity(!inverse), aI.getStats(!inverse),
					aI.getCalc(inverse).meltCritDamage, healthBar(!inverse).statBar(), true, true, arrows));
			case "nodamagecrit" -> linked.add(new AnimPartNoDamage(aI.getEntity(!inverse), true, arrows));
		}
	}

	public RNGOutcome2 outcome()
	{
		return (RNGOutcome2) lastDivider.asOutcome();
	}
}