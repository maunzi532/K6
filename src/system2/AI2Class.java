package system2;

import java.util.*;
import javafx.scene.image.*;
import system2.content.*;

public interface AI2Class
{
	Image image();

	int adaptive();

	AdaptiveType adaptiveType();

	AdvantageType advType();

	boolean magical();

	int[] ranges();

	int[] counterR();

	List<Ability2> abilities();

	List<AM2Type> attackModes();
}