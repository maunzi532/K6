package entity;

public class NoAI implements EnemyAI
{
	@Override
	public EnemyMove preferredMove(XCharacter user, boolean canMove, boolean hasToMove, int moveAway)
	{
		return new EnemyMove(user, -1, null, null, 0);
	}

	@Override
	public EnemyAI copy()
	{
		return new NoAI();
	}
}