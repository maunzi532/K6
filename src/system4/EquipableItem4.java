package system4;

import com.fasterxml.jackson.jr.stree.*;
import item4.*;
import java.util.*;
import load.*;

public record EquipableItem4(CharSequence name, String image, CharSequence info, int stackLimit, Set<String> tags, Map<Stats4, List<Modifier4>> modifiers) implements Item4, ModifierProvider4
{
	@Override
	public Map<Stats4, List<Modifier4>> modifiers()
	{
		return null;
	}

	public static EquipableItem4 load(JrsObject data)
	{
		CharSequence name = data.get("Name").asText();
		String image = LoadHelper.asOptionalString(data.get("Image"));
		CharSequence info = LoadHelper.asOptionalString(data.get("Info"));
		int stackLimit = LoadHelper.asInt(data.get("StackLimit"));
		Set<String> tags = new HashSet<>(LoadHelper.asStringList(data.get("Tags")));
		//List<Modifier4> modifiers1 = LoadHelper.asList(data.get("Modifiers"))
		Map<Stats4, List<Modifier4>> modifiers = Map.of(); //TODO
		return new EquipableItem4(name, image, info, stackLimit, tags, modifiers);
	}
}