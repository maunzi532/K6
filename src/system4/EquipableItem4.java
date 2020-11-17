package system4;

import item4.*;
import java.util.*;

public record EquipableItem4(CharSequence name, String image, CharSequence info, int stackLimit, Set<String> tags, List<Modifier4> modifiers) implements Item4
{

}