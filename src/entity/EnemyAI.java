package entity;

public interface EnemyAI
{
	EnemyMove preferredMove(XCharacter user, boolean canMove, boolean hasToMove, int moveAway);

	EnemyAI copy();
}