package system;

import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.util.*;
import java.util.stream.*;
import load.*;

public record EquipableItem4(CharSequence name, String image, CharSequence info, int stackLimit,
                             Set<String> tags, Map<Stats4, List<Modifier4>> modifiers,
                             Map<String, Integer> additional, Ranges4 attackRanges, Ranges4 abilityRanges, Ranges4 allyRanges,
                             Ranges4 defendRanges) implements Item4, ModifierProvider4
{
	public static final EquipableItem4 NO_EQUIP_ITEM = new EquipableItem4("", null, "",
			0, Set.of("Defend"), Map.of(), Map.of("AttackCount", 0),
			null, null, null, new Ranges4(""));

	public static EquipableItem4 load(JrsObject data)
	{
		CharSequence name = data.get("Name").asText();
		String image = LoadHelper.asOptionalString(data.get("Image"));
		CharSequence info = LoadHelper.asOptionalString(data.get("Info"));
		int stackLimit = LoadHelper.asInt(data.get("StackLimit"));
		Set<String> tags = new HashSet<>(LoadHelper.asStringList(data.get("Tags")));
		Map<Stats4, List<Modifier4>> modifiers = LoadHelper.asList(data.get("Modifiers"), Modifier4::load)
				.stream().collect(Collectors.groupingBy(Modifier4::stat));
		Map<String, Integer> additional = LoadHelper.asIntMap(data.get("Additional"));
		Ranges4 attackRanges = Ranges4.load(LoadHelper.asOptionalString(data.get("AttackRanges")));
		Ranges4 abilityRanges = Ranges4.load(LoadHelper.asOptionalString(data.get("AbilityRanges")));
		Ranges4 allyRanges = Ranges4.load(LoadHelper.asOptionalString(data.get("AllyRanges")));
		Ranges4 defendRanges = Ranges4.load(LoadHelper.asOptionalString(data.get("DefendRanges")));
		return new EquipableItem4(name, image, info, stackLimit, tags, modifiers,
				additional, attackRanges, abilityRanges, allyRanges, defendRanges);
	}
}