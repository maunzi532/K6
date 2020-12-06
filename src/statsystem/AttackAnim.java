package statsystem;

import animation.*;
import arrow.*;
import entity.*;
import java.util.*;
import statsystem.*;
import statsystem.analysis.*;

public final class AttackAnim implements AnimTimer
{
	private RNGDivider2 divider;
	private RNGDivider2 lastDivider;
	private final Arrows arrows;
	private final AttackInfo aI;
	private List<SidedAttackAnalysisEvent> events;
	private int eventCounter;
	private final List<AnimPart> linked;
	private final InfoArrow healthBar1;
	private final InfoArrow healthBar2;

	public AttackAnim(RNGDivider2 divider, Arrows arrows)
	{
		this.divider = divider;
		lastDivider = divider;
		this.arrows = arrows;
		aI = divider.getAttackInfo();
		events = divider.getEvents();
		eventCounter = -1;
		linked = new ArrayList<>();
		healthBar1 = new InfoArrow(aI.entity.location(), aI.entityT.location(),
				aI.entity.team().healthBarColor, "arrow.healthbar.background", "arrow.healthbar.text",
				aI.getStats(AttackSide.INITIATOR).currentHealth(), aI.getStats(AttackSide.INITIATOR).maxHealth());
		healthBar2 = new InfoArrow(aI.entityT.location(), aI.entity.location(),
				aI.entityT.team().healthBarColor, "arrow.healthbar.background", "arrow.healthbar.text",
				aI.getStats(AttackSide.TARGET).currentHealth(), aI.getStats(AttackSide.TARGET).maxHealth());
		arrows.addArrow(healthBar1);
		arrows.addArrow(healthBar2);
	}

	private InfoArrow healthBar(AttackSide side)
	{
		return switch(side)
				{
					case INITIATOR -> healthBar1;
					case TARGET -> healthBar2;
				};
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
				SidedAttackAnalysisEvent event = events.get(eventCounter);
				/*if(event.event() == AttackAnalysisEvent.DEFEATED)
					aI.getEntity(AttackSide.inverted(event.side())).setDefeated();*/
				startEvent(event.event(), event.side());
			}
		}
	}

	private void startEvent(AttackAnalysisEvent eventType, AttackSide side)
	{
		AttackInfoPart3 calc = aI.getCalc(side);
		XCharacter entity = aI.getEntity(side);
		XCharacter entityT = aI.getEntity(AttackSide.inverted(side));
		Stats stats = aI.getStats(side);
		Stats statsT = aI.getStats(AttackSide.inverted(side));
		StatBar healthBar = healthBar(side).statBar();
		StatBar healthBarT = healthBar(AttackSide.inverted(side)).statBar();
		/*linked.add(switch(eventType)
		{
			case HEALTHCOST -> new AnimPartHealthCost(calc.healthCost, stats, healthBar);
			case ATTACK -> new AnimPartAttack(entity, entityT, arrows);
			case MISS -> new AnimPartDodge(entity, entityT, aI.distance, arrows);
			case HIT -> new AnimPartHit(entityT, statsT, calc.damage, healthBarT, false, false, arrows);
			case MELT -> new AnimPartHit(entityT, statsT, calc.meltDamage, healthBarT, false, true, arrows);
			case NODAMAGE -> new AnimPartNoDamage(entityT, false, arrows);
			case DEFEATED -> new AnimPartVanish(entityT, arrows);
			case CRIT -> new AnimPartHit(entityT, statsT, calc.critDamage, healthBarT, true, false, arrows);
			case MELTCRIT -> new AnimPartHit(entityT, statsT, calc.meltCritDamage, healthBarT, true, true, arrows);
			case NODAMAGECRIT -> new AnimPartNoDamage(entityT, true, arrows);
		});*/
	}

	public RNGOutcome2 outcome()
	{
		return lastDivider.asOutcome();
	}
}