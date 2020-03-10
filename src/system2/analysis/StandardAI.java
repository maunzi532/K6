package system2.analysis;

import entity.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;

public class StandardAI implements EnemyAI
{
	private static final Random RANDOM = new Random();

	private final LevelMap levelMap;
	private final CombatSystem combatSystem;

	public StandardAI(LevelMap levelMap, CombatSystem combatSystem)
	{
		this.levelMap = levelMap;
		this.combatSystem = combatSystem;
	}

	@Override
	public EnemyMove preferredMove(XEnemy user, boolean canMove, boolean hasToMove, int moveAway)
	{
		List<PathLocation> locations = new ArrayList<>();
		List<PathAttackX> pathsX = new ArrayList<>();
		if(canMove)
		{
			List<XEntity> allies = hasToMove ? null : levelMap.getEntitiesE().stream().filter(e -> e != user && e.canMove()).collect(Collectors.toList());
			locations.addAll(new Pathing(levelMap.y1, user, user.movement(), levelMap, allies).start().getEndpaths());
		}
		else
			locations.add(new PathLocation(user.location()));
		for(PathLocation pl : locations)
		{
			pathsX.addAll(combatSystem.pathAttackInfo(user, pl.tile(), user.getStats(), levelMap.getEntitiesH(), pl));
			if(user.canMove())
				pathsX.add(new PathAttackX(pl, null));
		}
		if(pathsX.isEmpty())
			return new EnemyMove(user, -1, null, null, 0);
		Collections.shuffle(pathsX, RANDOM);
		HashMap<AttackInfo, Double> analysis = new HashMap<>();
		for(PathAttackX px : pathsX)
		{
			if(px.attack != null && !analysis.containsKey(px.attack))
				analysis.put(px.attack, combatSystem.enemyAIScore(px.attack.analysis.outcomes()));
			if(px.attack != null)
				px.score += analysis.get(px.attack) * 1000;
			if(!px.path.tile().equals(user.location()))
				px.score += moveAway;
			px.score += levelMap.getEntitiesH().stream().mapToInt(e -> levelMap.y1
					.distance(e.location(), user.location())).max().orElse(0) * 20;
		}
		if(analysis.isEmpty())
		{
			PathLocation doubledPath = new Pathing(levelMap.y1, user, user.movement() * 2,
					levelMap, null).start().getEndpaths().stream().min(Comparator.comparingInt((PathLocation f) ->
					levelMap.getEntitiesH().stream().mapToInt(e -> levelMap.y1.distance(e.location(), f.tile())).min().orElse(0))
					.thenComparingInt(PathLocation::cost)).orElseThrow();
			int len = 0;
			PathLocation doubledPath2 = doubledPath;
			while(doubledPath2 != null)
			{
				doubledPath2 = doubledPath2.from();
				len++;
			}
			PathLocation doubledPath3 = doubledPath;
			int z1 = len;
			while(doubledPath3 != null)
			{
				for(PathAttackX e : pathsX)
				{
					if(e.path.tile().equals(doubledPath3.tile()))
					{
						e.score += 800 * z1 / len;
						break;
					}
				}
				doubledPath3 = doubledPath3.from();
				z1--;
			}
		}
		PathAttackX max = pathsX.stream().max(Comparator.comparingInt(e -> e.score)).orElseThrow();
		PathAttackX max2 = pathsX.stream().filter(e -> !e.path.tile().equals(max.path.tile())).max(Comparator.comparingInt(e -> e.score)).orElse(max);
		return new EnemyMove(user, max.score, max.path, max.attack, max.score - max2.score);
	}

	@Override
	public EnemyAI copy()
	{
		return new StandardAI(levelMap, combatSystem);
	}
}