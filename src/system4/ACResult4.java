package system4;

import arrow.*;
import entity.*;
import java.util.*;
import statsystem.animation.*;

public record ACResult4(List<AnimPart> animParts, int hp1, int hp2, StatBar hpBar1, StatBar hpBar2)
{
	public static ACResult4 calc1(AttackCalc4 aI, Arrows arrows, Random random)
	{
		ArrayList<AnimPart> animParts = new ArrayList<>();
		boolean init1 = true;
		int attacks1 = aI.attackCount1;
		int attacks2 = aI.attackCount2;
		int hp1 = aI.aI.initiator().currentHP();
		int hp2 = aI.aI.target().currentHP();
		StatBar hpBar1 = new StatBar(aI.aI.initiator().team().healthBarColor,
				"arrow.healthbar.background", "arrow.healthbar.text", hp1, aI.aI.initiator().maxHP());
		StatBar hpBar2 = new StatBar(aI.aI.initiator().team().healthBarColor,
				"arrow.healthbar.background", "arrow.healthbar.text", hp2, aI.aI.target().maxHP());
		while(true)
		{
			if(hp1 <= 0 || hp2 <= 0 || (attacks1 <= 0 && attacks2 <= 0))
			{
				return new ACResult4(animParts, hp1, hp2, hpBar1, hpBar2);
			}
			if(init1)
			{
				if(attacks1 > 0)
				{
					animParts.add(new AnimPartAttack(aI.aI.initiator(), aI.aI.target(), arrows));
					int rn = random.nextInt(100);
					int acN = aI.accuracy1 + rn;
					if(acN >= 150)
					{
						int damage = aI.damage1 * 2;
						animParts.add(a1(aI.aI.target(), arrows, hpBar2, damage, true, false));
						hp2 -= damage;
					}
					else if(acN >= 50)
					{
						int damage = aI.damage1;
						animParts.add(a1(aI.aI.target(), arrows, hpBar2, damage, false, false));
						hp2 -= damage;
					}
					else if(acN >= -50)
					{
						int damage = aI.damage1 - aI.damage1 / 2;
						animParts.add(a1(aI.aI.target(), arrows, hpBar2, damage, false, true));
						hp2 -= damage;
					}
					else
					{
						animParts.add(new AnimPartDodge(aI.aI.initiator(), aI.aI.target(), aI.aI.distance(), arrows));
					}
					if(hp2 <= 0)
						animParts.add(new AnimPartVanish(aI.aI.target(), arrows));
					attacks1--;
				}
			}
			else
			{
				if(attacks2 > 0)
				{
					animParts.add(new AnimPartAttack(aI.aI.target(), aI.aI.initiator(), arrows));
					int rn = random.nextInt(100);
					int acN = aI.accuracy2 + rn;
					if(acN >= 150)
					{
						int damage = aI.damage2 * 2;
						animParts.add(a1(aI.aI.initiator(), arrows, hpBar1, damage, true, false));
						hp1 -= damage;
					}
					else if(acN >= 50)
					{
						int damage = aI.damage2;
						animParts.add(a1(aI.aI.initiator(), arrows, hpBar1, damage, false, false));
						hp1 -= damage;
					}
					else if(acN >= -50)
					{
						int damage = aI.damage2 - aI.damage2 / 2;
						animParts.add(a1(aI.aI.initiator(), arrows, hpBar1, damage, false, true));
						hp1 -= damage;
					}
					else
					{
						animParts.add(new AnimPartDodge(aI.aI.initiator(), aI.aI.target(), aI.aI.distance(), arrows));
					}
					if(hp1 <= 0)
						animParts.add(new AnimPartVanish(aI.aI.initiator(), arrows));
					attacks2--;
				}
			}
			init1 = !init1;
		}
	}

	private static AnimPart a1(XCharacter t1, Arrows arrows, StatBar hpBar, int damage, boolean crit, boolean dodge)
	{
		if(damage > 0)
		{
			return new AnimPartHit(t1, damage, hpBar, crit, dodge, arrows);
		}
		else
		{
			return new AnimPartNoDamage(t1, crit, arrows);
		}
	}
}