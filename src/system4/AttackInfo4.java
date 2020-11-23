package system4;

import entity.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;

public record AttackInfo4(XCharacter initiator, EquipableItem4 initiatorItem,
                          XCharacter target, EquipableItem4 targetItem, int distance, boolean abilityAttack)
{
	public static List<AttackInfo4> attackOptions(XCharacter initiator, XCharacter target,
			TileType y1, boolean abilityAttack)
	{
		int distance = y1.distance(initiator.location(), target.location());
		System.out.println(initiator.systemChar().possibleAttackItems(distance));
		System.out.println(initiator.systemChar().inv().viewItems());
		System.out.println(distance);
		EquipableItem4 defendItem = target.systemChar().currentDefendItem(distance);
		return initiator.systemChar().possibleAttackItems(distance).stream()
				.map(e -> new AttackInfo4(initiator, e, target, defendItem, distance, abilityAttack))
				.collect(Collectors.toList());
	}
}