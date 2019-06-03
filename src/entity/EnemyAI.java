package entity;

import logic.*;

public interface EnemyAI
{
	EnemyMove preferredMove(MainState mainState, XEnemy user, boolean canMove, boolean hasToMove, int moveAway);

	EnemyAI copy();
}