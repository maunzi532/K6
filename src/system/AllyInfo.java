package system;

import entity.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;

public record AllyInfo(XCharacter character, EquipableItem item, XCharacter target, int distance)
{
	public static List<AllyInfo> allyOptions(XCharacter initiator, XCharacter target, TileType y1)
	{
		int distance = y1.distance(initiator.location(), target.location());
		return allyOptions(initiator, target, distance);
	}

	public static List<AllyInfo> allyOptions(XCharacter initiator, XCharacter target, int distance)
	{
		return initiator.systemChar().possibleAllyItems(distance, target.systemChar()).stream()
				.map(e -> new AllyInfo(initiator, e, target, distance))
				.collect(Collectors.toList());
	}
}