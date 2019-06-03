package system2.content;

import entity.*;
import entity.analysis.*;
import java.util.*;
import java.util.stream.*;
import logic.*;

public class StandardAI implements EnemyAI
{
	private static final Random RANDOM = new Random();

	@Override
	public EnemyMove preferredMove(MainState mainState, XEnemy user, boolean canMove, boolean hasToMove, int moveAway)
	{
		List<PathLocation> locations = new ArrayList<>();
		List<PathAttackX> pathsX = new ArrayList<>();
		if(canMove)
		{
			List<XEntity> allies = hasToMove ? null : mainState.levelMap.getEntitiesE().stream().filter(e -> e != user && e.canMove()).collect(Collectors.toList());
			locations.addAll(new Pathing(mainState.y2, user, user.movement(), mainState.levelMap, allies).start().getEndpaths());
		}
		else
			locations.add(new PathLocation(user.location()));
		for(PathLocation pl : locations)
		{
			pathsX.addAll(mainState.combatSystem.pathAttackInfo(mainState, user, pl.tile, user.getStats(), mainState.levelMap.getEntitiesH(), pl));
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
				analysis.put(px.attack, mainState.combatSystem.enemyAIScore(new RNGInfoAnalysis(mainState.combatSystem.supplyDivider(px.attack)).create().outcomes()));
			if(px.attack != null)
				px.score += analysis.get(px.attack) * 1000;
			if(!px.path.tile.equals(user.location()))
				px.score += moveAway;
			px.score += mainState.levelMap.getEntitiesH().stream().mapToInt(e -> mainState.y2.distance(e.location(), user.location())).max().orElse(0) * 20;
		}
		if(analysis.isEmpty())
		{
			PathLocation doubledPath = new Pathing(mainState.y2, user, user.movement() * 2,
					mainState.levelMap, null).start().getEndpaths().stream().min(Comparator.comparingInt((PathLocation f) ->
					mainState.levelMap.getEntitiesH().stream().mapToInt(e -> mainState.y2.distance(e.location(), f.tile)).min().orElse(0))
					.thenComparingInt(f -> f.cost)).orElseThrow();
			int len = 0;
			PathLocation doubledPath2 = doubledPath;
			while(doubledPath2 != null)
			{
				doubledPath2 = doubledPath2.from;
				len++;
			}
			PathLocation doubledPath3 = doubledPath;
			int z1 = len;
			while(doubledPath3 != null)
			{
				for(PathAttackX e : pathsX)
				{
					if(e.path.tile.equals(doubledPath3.tile))
					{
						e.score += 800 * z1 / len;
						break;
					}
				}
				doubledPath3 = doubledPath3.from;
				z1--;
			}
		}
		PathAttackX max = pathsX.stream().max(Comparator.comparingInt(e -> e.score)).orElseThrow();
		PathAttackX max2 = pathsX.stream().filter(e -> !e.path.tile.equals(max.path.tile)).max(Comparator.comparingInt(e -> e.score)).orElse(max);
		return new EnemyMove(user, max.score, max.path, max.attack, max.score - max2.score);
	}

	@Override
	public EnemyAI copy()
	{
		return new StandardAI();
	}
}