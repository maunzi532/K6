package system;

import entity.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;

public record AllyInfo4(XCharacter character, EquipableItem4 item, XCharacter target, int distance)
{
	public static List<AllyInfo4> allyOptions(XCharacter initiator, XCharacter target, TileType y1)
	{
		int distance = y1.distance(initiator.location(), target.location());
		return allyOptions(initiator, target, distance);
	}

	public static List<AllyInfo4> allyOptions(XCharacter initiator, XCharacter target, int distance)
	{
		return initiator.systemChar().possibleAllyItems(distance, target.systemChar()).stream()
				.map(e -> new AllyInfo4(initiator, e, target, distance))
				.collect(Collectors.toList());
	}
}