package entity;

public interface EnemyAI
{
	EnemyMove preferredMove(XEnemy user, boolean canMove, boolean hasToMove, int moveAway);

	EnemyAI copy();
}