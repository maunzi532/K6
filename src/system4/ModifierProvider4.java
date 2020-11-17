package system4;

import java.util.*;

public interface ModifierProvider4
{
	Map<String, List<Modifier4>> allModifiers();

	List<Modifier4> getModifiers(String name);
}