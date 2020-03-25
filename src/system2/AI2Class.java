package system2;

import java.util.*;
import system2.content.*;

public interface AI2Class
{
	String image();

	int adaptive();

	AdaptiveType adaptiveType();

	AdvantageType advType();

	boolean magical();

	int[] ranges();

	int[] counterR();

	List<Ability2> abilities();

	List<AM2Type> attackModes();
}