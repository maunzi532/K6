package entity;

public class EnemyMove
{
	private XEnemy entity;
	private int initiative;
	private PathLocation moveTo;
	private AttackInfo attackInfo;
	private int tileAdvantage;

	public EnemyMove(XEnemy entity, int initiative, PathLocation moveTo, AttackInfo attackInfo, int tileAdvantage)
	{
		this.entity = entity;
		this.initiative = initiative;
		this.moveTo = moveTo;
		this.attackInfo = attackInfo;
		this.tileAdvantage = tileAdvantage;
	}

	public XEnemy getEntity()
	{
		return entity;
	}

	public int getInitiative()
	{
		return initiative;
	}

	public PathLocation moveTo()
	{
		return moveTo;
	}

	public AttackInfo attackInfo()
	{
		return attackInfo;
	}

	public int getTileAdvantage()
	{
		return tileAdvantage;
	}
}