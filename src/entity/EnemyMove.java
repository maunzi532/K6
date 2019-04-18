package entity;

import geom.f1.*;

public class EnemyMove
{
	private XEnemy entity;
	private int initiative;
	private Tile moveTo;
	private AttackInfo attackInfo;

	public EnemyMove(XEnemy entity, int initiative, Tile moveTo, AttackInfo attackInfo)
	{
		this.entity = entity;
		this.initiative = initiative;
		this.moveTo = moveTo;
		this.attackInfo = attackInfo;
	}

	public XEnemy getEntity()
	{
		return entity;
	}

	public int getInitiative()
	{
		return initiative;
	}

	public Tile moveTo()
	{
		return moveTo;
	}

	public AttackInfo attackInfo()
	{
		return attackInfo;
	}
}