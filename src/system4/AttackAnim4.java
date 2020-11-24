package system4;

import arrow.*;
import entity.*;
import java.util.*;
import statsystem.*;
import statsystem.analysis.*;
import statsystem.animation.*;

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
		eventCounter = -1;
		linked = new ArrayList<>();
		/*healthBar1 = new InfoArrow(initiator.location(), target.location(),
				initiator.team().healthBarColor, "arrow.healthbar.background", "arrow.healthbar.text",
				initiator.currentHP(), initiator.maxHP());*/
		healthBar1 = new InfoArrow(initiator.location(), target.location(), result.hpBar1());
		arrows.addArrow(healthBar1);
		/*healthBar2 = new InfoArrow(target.location(), initiator.location(),
				target.team().healthBarColor, "arrow.healthbar.background", "arrow.healthbar.text",
				target.currentHP(), target.maxHP());*/
		healthBar2 = new InfoArrow(target.location(), initiator.location(), result.hpBar2());
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
		/*if(divider != null)
			return false;*/
		if(eventCounter >= result.animParts().size())
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
		if(eventCounter >= result.animParts().size())
			return;
		/*if(divider == null)
			return;*/
		linked.removeIf(AnimPart::tick);
		if(linked.stream().allMatch(AnimPart::finished1))
		{
			eventCounter++;
			if(eventCounter >= result.animParts().size())
				return;
			linked.add(result.animParts().get(eventCounter));

			/*if(eventCounter >= events.size())
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
			else*/
			/*{
				SidedAttackAnalysisEvent event = events.get(eventCounter);
				if(event.event() == AttackAnalysisEvent.DEFEATED)
				{
					switch(event.side())
					{
						case INITIATOR -> target.setDefeated();
						case TARGET -> initiator.setDefeated();
					}
				}
				startEvent(event.event(), event.side());
			}*/
		}
	}

	private void startEvent(AttackAnalysisEvent eventType, AttackSide side)
	{
		/*AttackInfoPart3 calc = aI.getCalc(side);
		XCharacter entity = aI.getEntity(side);
		XCharacter entityT = aI.getEntity(AttackSide.inverted(side));
		Stats stats = aI.getStats(side);
		Stats statsT = aI.getStats(AttackSide.inverted(side));*/
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

	/*public RNGOutcome2 outcome()
	{
		return lastDivider.asOutcome();
	}*/
}