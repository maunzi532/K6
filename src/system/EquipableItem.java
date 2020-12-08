package system;

import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.util.*;
import java.util.stream.*;
import load.*;
import text.*;

public record EquipableItem(int num, CharSequence name, String image, CharSequence info, int stackLimit, boolean ghost,
                            Set<String> tags, Map<Stats4, List<Modifier4>> modifiers,
                            Map<String, Integer> additional, Ranges4 attackRanges, Ranges4 abilityRanges, Ranges4 allyRanges,
                            Ranges4 defendRanges) implements Item, ModifierProvider4
{
	public static final EquipableItem NO_EQUIP_ITEM = new EquipableItem(-1, "", null, "",
			0, true, Set.of("Defend"), Map.of(), Map.of("AttackCount", 0),
			null, null, null, new Ranges4(""));

	public static EquipableItem load(JrsObject data, int num)
	{
		CharSequence name = data.get("Name").asText();
		String image = LoadHelper.asOptionalString(data.get("Image"));
		CharSequence info = LoadHelper.asOptionalString(data.get("Info"));
		int stackLimit = LoadHelper.asInt(data.get("StackLimit"));
		boolean ghost = LoadHelper.asBoolean(data.get("Ghost"));
		Set<String> tags = new HashSet<>(LoadHelper.asStringList(data.get("Tags")));
		Map<Stats4, List<Modifier4>> modifiers = LoadHelper.asList(data.get("Modifiers"), Modifier4::load)
				.stream().collect(Collectors.groupingBy(Modifier4::stat));
		Map<String, Integer> additional = LoadHelper.asIntMap(data.get("Additional"));
		Ranges4 attackRanges = Ranges4.load(LoadHelper.asOptionalString(data.get("AttackRanges")));
		Ranges4 abilityRanges = Ranges4.load(LoadHelper.asOptionalString(data.get("AbilityRanges")));
		Ranges4 allyRanges = Ranges4.load(LoadHelper.asOptionalString(data.get("AllyRanges")));
		Ranges4 defendRanges = Ranges4.load(LoadHelper.asOptionalString(data.get("DefendRanges")));
		return new EquipableItem(num, name, image, info, stackLimit, ghost, tags, modifiers,
				additional, attackRanges, abilityRanges, allyRanges, defendRanges);
	}

	public List<CharSequence> statsInfo()
	{
		return List.of
				(
						viewRanges("item.ranges.attack", attackRanges),
						viewRanges("item.ranges.ability", abilityRanges),
						viewRanges("item.ranges.ally", allyRanges),
						viewRanges("item.ranges.defend", defendRanges),
						viewAdditional("item.attackcount", "AttackCount"),
						viewAdditional("item.attackpower", "AttackPower"),
						viewAdditional("item.abilitypower", "AbilityPower"),
						viewAdditional("item.magic", "Magic")
				);
	}

	private CharSequence viewRanges(String formatKey, Ranges4 ranges)
	{
		if(ranges == null)
			return "_";
		else
			return new ArgsText(formatKey, ranges.view());
	}

	private CharSequence viewAdditional(String formatKey, String key)
	{
		if(additional.containsKey(key))
			return new ArgsText(formatKey, additional.get(key));
		else
			return "_";
	}
}