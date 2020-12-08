package system;

import entity.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;

public record AttackInfo4(XCharacter initiator, EquipableItem initiatorItem,
                          XCharacter target, EquipableItem targetItem,
                          int distance, boolean attack, boolean ability)
{
	public static List<AttackInfo4> attackOptions(XCharacter initiator, XCharacter target,
			TileType y1, boolean attack, boolean ability)
	{
		int distance = y1.distance(initiator.location(), target.location());
		return attackOptions(initiator, target, distance, attack, ability);
	}

	public static List<AttackInfo4> attackOptions(XCharacter initiator, XCharacter target,
			int distance, boolean attack, boolean ability)
	{
		EquipableItem defendItem = target.systemChar().defendItem();
		return initiator.systemChar().possibleAttackItems(distance, attack, ability).stream()
				.map(e -> new AttackInfo4(initiator, e, target, defendItem, distance, attack, ability))
				.collect(Collectors.toList());
	}
}