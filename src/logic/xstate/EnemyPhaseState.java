package logic.xstate;

import entity.*;
import logic.*;

public final class EnemyPhaseState implements NAutoState
{
	private EnemyMove4 initiativeMove;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		initiativeMove = mainState.levelMap().allCharacters().stream()
				.filter(e -> e.team() == CharacterTeam.ENEMY && e.hasMainAction())
				.map(e -> e.systemChar().enemyAI().preferredMove(e, mainState.levelMap()))
				.sorted().findFirst().orElse(null);
		System.out.println(initiativeMove);
		/*initiativeMove = mainState.levelMap().teamTargetCharacters(CharacterTeam.ENEMY).stream().filter(e -> e.resources().ready())
				.map(e -> e.preferredMove(false, 0))
				.max(Comparator.comparingInt(EnemyMove::initiative)).filter(e -> e.initiative() >= 0).orElse(null);
		if(initiativeMove != null && initiativeMove.moveTo() != null && initiativeMove.moveTo().movingAlly() != null)
		{
			EnemyMove initiativeMove2 = (initiativeMove.moveTo().movingAlly()).preferredMove(true, initiativeMove.tileAdvantage());
			if(initiativeMove2.initiative() >= 0)
				initiativeMove = initiativeMove2;
		}*/
		/*mainState.levelMap().allCharacters().stream()
				.filter(e -> e.team() == CharacterTeam.ENEMY)
				.map(XCharacter::preferredMove)
				.sorted(Comparator.comparingInt(e -> e.aI() == null ? 1 : 0))*/
	}

	@Override
	public void tick(MainState mainState){}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		if(initiativeMove == null)
		{
			return new StartTurnState();
		}
		else
		{
			initiativeMove.character().setHasMainAction(false);
			if(initiativeMove.aI() == null)
			{
				return new MoveAnimState(this, initiativeMove.character(), initiativeMove.moveTo().tile());
			}
			else if(initiativeMove.attackFirst())
			{
				return new PreAttackState(new MoveAnimState(this, initiativeMove.character(), initiativeMove.moveTo().tile()), initiativeMove.aI());
			}
			else
			{
				return new MoveAnimState(new PreAttackState(this, initiativeMove.aI()), initiativeMove.character(), initiativeMove.moveTo().tile());
			}
		}
		/*if(!initiativeMove.attackFirst())
			initiativeMove.character().resources().move(initiativeMove.moveTo().cost());
		if(attackInfo != null)
		{
			initiativeMove.entity().resources().action(true);
			if(moveTo != null)
				return new MoveAnimState(new PreAttackState(this, attackInfo), initiativeMove.entity(), moveTo);
			else
				return new PreAttackState(this, attackInfo);
			return null;
		}
		else
		{
			if(moveTo != null)
				return new MoveAnimState(this, initiativeMove.entity(), moveTo);
			else
				return new StartTurnState();
		}*/
	}
}