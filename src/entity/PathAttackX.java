package entity;

import system2.*;

public class PathAttackX
{
	public final PathLocation path;
	public final AttackInfo attack;
	public int score;

	public PathAttackX(PathLocation path, AttackInfo attack)
	{
		this.path = path;
		this.attack = attack;
	}
}