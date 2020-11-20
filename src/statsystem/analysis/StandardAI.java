package statsystem.analysis;

import com.fasterxml.jackson.jr.ob.comp.*;
import entity.*;
import geom.tile.*;
import item.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import levelmap.*;
import statsystem.*;

public final class StandardAI implements EnemyAI
{
	private static final Random RANDOM = new Random();

	private final LevelMap levelMap;

	public StandardAI(LevelMap levelMap)
	{
		this.levelMap = levelMap;
	}

	@Override
	public EnemyMove preferredMove(XCharacter user, boolean canMove, boolean hasToMove, int moveAway)
	{
		List<PathLocation> locations = new ArrayList<>();
		List<PathAttackX> pathsX = new ArrayList<>();
		if(canMove)
		{
			List<XCharacter> allies = hasToMove ? null :
					levelMap.teamTargetCharacters(user.team()).stream().filter(e -> e != user && e.resources().hasMoveAction()).collect(Collectors.toList());
			locations.addAll(new Pathing(levelMap.y1, user, user.resources().leftoverMovement(), null/*levelMap*/, allies).start().getEndpaths());
		}
		else
			locations.add(new PathLocation(user.location()));
		for(PathLocation pl : locations)
		{
			/*pathsX.addAll(levelMap.pathAttackInfo(user, pl.tile(),
					levelMap.enemyTeamTargetCharacters(user.team()), pl));*/
			if(user.resources().hasMoveAction())
				pathsX.add(new PathAttackX(pl, null));
		}
		if(pathsX.isEmpty())
			return new EnemyMove(user, -1, null, null, 0);
		Collections.shuffle(pathsX, RANDOM);
		HashMap<AttackInfo, Double> analysis = new HashMap<>();
		for(PathAttackX px : pathsX)
		{
			if(px.attack != null && !analysis.containsKey(px.attack))
				analysis.put(px.attack, new EnemyThoughts2(px.attack.analysis.outcomes()).score());
			if(px.attack != null)
				px.score += (int) (analysis.get(px.attack) * 1000.0);
			if(!px.path.tile().equals(user.location()))
				px.score += moveAway;
			px.score += levelMap.enemyTeamTargetCharacters(user.team()).stream().mapToInt(e -> levelMap.y1
					.distance(e.location(), user.location())).max().orElse(0) * 20;
		}
		if(analysis.isEmpty())
		{
			PathLocation doubledPath = new Pathing(levelMap.y1, user, user.resources().leftoverMovement() * 2,
					/*levelMap*/null, null).start().getEndpaths().stream().min(Comparator.comparingInt((PathLocation location) ->
					levelMap.enemyTeamTargetCharacters(user.team()).stream().mapToInt(e -> levelMap.y1.distance(e.location(), location.tile())).min().orElse(0))
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
				Tile tile3 = doubledPath3.tile();
				int score = 800 * z1 / len;
				pathsX.stream().filter(pa -> pa.path.tile().equals(tile3)).findFirst().ifPresent(pa -> pa.score += score);
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
		return new StandardAI(levelMap);
	}

	@Override
	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1)
			throws IOException
	{
		a1.put("Type", "StandardAI");
		a1.end();
	}
}