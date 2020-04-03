package statsystem;

import java.util.*;
import statsystem.content.*;

public interface AI2Class
{
	CharSequence name();

	String image();

	int adaptive();

	AdaptiveType adaptiveType();

	AdvantageType advType();

	DefenseType defenseType();

	int[] ranges();

	int[] counterR();

	List<XAbility> abilities();

	List<AM2Type> attackModes();
}