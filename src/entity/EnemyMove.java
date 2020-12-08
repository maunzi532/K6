package entity;

import java.util.*;
import system.*;

public record EnemyMove(XCharacter character, PathLocation moveTo, AttackCalc aI, boolean attackFirst, int targetDistance)
		implements Comparable<EnemyMove>
{
	//TODO sort
	//1y can attack
	//2y can outrange
	//3 rarest attacked
	//4 def/dodge
	//5y can move after attack
	//6y distance to target

	@Override
	public int compareTo(EnemyMove o)
	{
		if(aI != null && o.aI() != null)
		{
			if(aI.canAttack2 && !o.aI().canAttack2)
				return 1;
			if(!aI.canAttack2 && o.aI().canAttack2)
				return -1;
			if(attackFirst && o.attackFirst())
			{
				if(targetDistance < o.targetDistance())
					return -1;
				if(targetDistance > o.targetDistance())
					return 1;
			}
			if(attackFirst && !o.attackFirst())
				return -1;
			if(!attackFirst && o.attackFirst())
				return 1;
			return 0;
		}
		if(aI != null)
			return -1;
		if(o.aI() != null)
			return 1;
		if(targetDistance < o.targetDistance())
			return -1;
		if(targetDistance > o.targetDistance())
			return 1;
		return 0;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(!(obj instanceof EnemyMove other))
			return false;
		if(attackFirst != other.attackFirst)
			return false;
		if(!character.equals(other.character))
			return false;
		if(!moveTo.equals(other.moveTo))
			return false;
		return Objects.equals(aI, other.aI);
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