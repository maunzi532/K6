package entity;

import item.*;
import java.util.*;
import javafx.scene.image.*;

public interface Stats
{
	int getStat(int num);

	int getMaxStat(int num);

	void change(int change);

	int getRegenerateChange();

	void regenerating();

	Item getItemFilter();

	boolean removeEntity();

	String getName();

	List<String> info();

	Image image();
}