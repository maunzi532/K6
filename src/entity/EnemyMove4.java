package entity;

import system4.*;

public record EnemyMove4(XCharacter character, PathLocation moveTo, AttackCalc4 aI, boolean attackFirst)
		implements Comparable<EnemyMove4>
{
	//TODO sort
	//1y can attack
	//2y can outrange
	//3 rarest attacked
	//4 def/dodge
	//5 can move after attack
	//6 distance to target

	@Override
	public int compareTo(EnemyMove4 o)
	{
		if(aI != null && o.aI() != null)
		{
			if(aI.canAttack2 && !o.aI().canAttack2)
				return 1;
			if(!aI.canAttack2 && o.aI().canAttack2)
				return -1;
			return 0;
		}
		if(aI != null)
			return -1;
		if(o.aI() != null)
			return 1;
		return 0;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(!(obj instanceof EnemyMove4 other))
			return false;
		if(attackFirst != other.attackFirst)
			return false;
		if(!character.equals(other.character))
			return false;
		if(!moveTo.equals(other.moveTo))
			return false;
		return aI != null ? aI.equals(other.aI) : other.aI == null;
	}

	@Override
	public int hashCode()
	{
		int result = character.hashCode();
		result = 31 * result + moveTo.hashCode();
		result = 31 * result + (aI != null ? aI.hashCode() : 0);
		result = 31 * result + (attackFirst ? 1 : 0);
		return result;
	}
}