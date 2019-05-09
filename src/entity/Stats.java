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

	void autoEquip(InvEntity entity);

	Item getItemFilter();

	boolean removeEntity();

	String getName();

	Image image();

	Stats copy();

	List<String> info();

	List<Integer> save();

	List<String> infoEdit();

	List<String> editOptions(int num);

	void applyEditOption(int num, int option, XEntity entity);
}