package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.tile.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import levelmap.*;

public class EnemyAI4 implements XSaveableY
{
	private final Tile targetTile;

	public EnemyAI4(Tile targetTile)
	{
		this.targetTile = targetTile;
	}

	private List<EnemyMove4> possibleMoves(XCharacter character, LevelMap4 levelMap)
	{
		List<EnemyMove4> moves = new ArrayList<>();
		List<PathLocation> locations = new Pathing(character, character.movement(), levelMap, false).getEndpaths();
		for(PathLocation pl : locations)
		{
			moves.addAll(levelMap.allCharacters().stream().filter(e -> e.team() != character.team()
					&& character.enemyTargetRanges().contains(levelMap.y1().distance(e.location(), pl.tile())))
					.flatMap(e -> character.attackOptions(levelMap.y1().distance(e.location(), pl.tile()), e).stream())
					.map(e -> new EnemyMove4(character, pl, e, false, distanceToTarget(levelMap.y1(), pl.tile()))).collect(Collectors.toList()));
		}
		int n = moves.size();
		for(int i = 0; i < n; i++)
		{
			EnemyMove4 m1 = moves.get(i);
			if(m1.moveTo().cost() <= 0)
			{
				moves.addAll(locations.stream().map(e -> new EnemyMove4(character, e, m1.aI(), true, distanceToTarget(levelMap.y1(), e.tile()))).collect(Collectors.toList()));
			}
		}
		moves.addAll(locations.stream().map(e -> new EnemyMove4(character, e, null, false, distanceToTarget(levelMap.y1(), e.tile()))).collect(Collectors.toList()));
		return moves;
	}

	public EnemyMove4 preferredMove(XCharacter character, LevelMap4 levelMap)
	{
		return possibleMoves(character, levelMap).stream().sorted().findFirst().orElseThrow();
	}

	private int distanceToTarget(TileType y1, Tile tile)
	{
		if(targetTile != null)
			return y1.distance(tile, targetTile);
		else
			return -1;
	}

	public static EnemyAI4 load(JrsObject data, TileType y1)
	{
		Tile targetTile = data.get("sx") != null ? XSaveableY.loadLocation(data, y1) : null;
		return new EnemyAI4(targetTile);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, TileType y1) throws IOException
	{
		if(targetTile != null)
			XSaveableY.saveLocation(targetTile, a1, y1);
	}
}