package system2.animation;

import arrow.*;
import entity.*;
import java.util.*;
import javafx.scene.paint.*;
import levelMap.*;
import system2.*;
import system2.analysis.*;

public class AttackAnim implements AnimTimer
{
	private RNGDivider2 divider;
	private LevelMap levelMap;
	private AttackInfo2 aI;
	private List<String> events;
	private int eventCounter;
	private List<AnimPart> linked;
	private InfoArrow healthBar1;
	private InfoArrow healthBar2;

	public AttackAnim(RNGDivider2 divider, LevelMap levelMap)
	{
		this.divider = divider;
		this.levelMap = levelMap;
		aI = divider.getAttackInfo();
		events = divider.getEvents();
		eventCounter = -1;
		linked = new ArrayList<>();
		healthBar1 = new InfoArrow(aI.entity.location(), aI.entityT.location(),
				aI.entity instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK, Color.WHITE,
				aI.getStats(false).getCurrentHealth(), aI.getStats(false).getToughness());
		healthBar2 = new InfoArrow(aI.entityT.location(), aI.entity.location(),
				aI.entityT instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK, Color.WHITE,
				aI.getStats(true).getCurrentHealth(), aI.getStats(true).getToughness());
		levelMap.addArrow(healthBar1);
		levelMap.addArrow(healthBar2);
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
				startEvent(eventType, inverse);
			}
		}
	}

	private void startEvent(String eventType, boolean inverse)
	{
		switch(eventType)
		{
			case "healthcost" -> linked.add(new AnimPartHealthCost(aI.getCalc(inverse).cost,
						aI.getStats(inverse), healthBar(inverse).statBar()));
			case "attack" -> linked.add(new AnimPartAttack(aI.getEntity(inverse), aI.getEntity(!inverse), levelMap));
			case "miss" -> linked.add(new AnimPartDodge(aI.getEntity(inverse), aI.getEntity(!inverse), aI.distance, levelMap));
			case "hit" -> linked.add(new AnimPartHit(aI.getEntity(!inverse), aI.getStats(!inverse),
					aI.getCalc(inverse).damage, healthBar(!inverse).statBar(), false, false, levelMap));
			case "melt" -> linked.add(new AnimPartHit(aI.getEntity(!inverse), aI.getStats(!inverse),
					aI.getCalc(inverse).meltDamage, healthBar(!inverse).statBar(), false, true, levelMap));
			case "nodamage" -> linked.add(new AnimPartNoDamage(aI.getEntity(!inverse), false, levelMap));
			case "defeated" -> linked.add(new AnimPartVanish(aI.getEntity(!inverse), levelMap));
			case "crit" -> linked.add(new AnimPartHit(aI.getEntity(!inverse), aI.getStats(!inverse),
					aI.getCalc(inverse).critDamage, healthBar(!inverse).statBar(), true, false, levelMap));
			case "meltcrit" -> linked.add(new AnimPartHit(aI.getEntity(!inverse), aI.getStats(!inverse),
					aI.getCalc(inverse).meltCritDamage, healthBar(!inverse).statBar(), true, true, levelMap));
			case "nodamagecrit" -> linked.add(new AnimPartNoDamage(aI.getEntity(!inverse), true, levelMap));
		}
	}
}