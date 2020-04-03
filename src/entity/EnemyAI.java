package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.tile.*;
import item.*;
import java.io.*;
import levelmap.*;
import statsystem.analysis.*;

public interface EnemyAI
{
	EnemyMove preferredMove(XCharacter user, boolean canMove, boolean hasToMove, int moveAway);

	EnemyAI copy();

	static EnemyAI load(JrsObject data, ItemLoader itemLoader, LevelMap levelMap)
	{
		return switch(data.get("Type").asText())
		{
			case "NoAI" -> new NoAI();
			case "StandardAI" -> new StandardAI(levelMap);
			default -> throw new IllegalArgumentException("Unrecognized EnemyAI type");
		};
	}

	<T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException;
}