package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import item.*;
import java.io.*;
import java.util.*;

public interface Stats
{
	int getVisualStat(int num);

	int getMaxVisualStat(int num);

	int getRegenerateChange();

	void regenerating();

	void autoEquip(InvEntity entity);

	void afterTrading(InvEntity entity);

	Item getItemFilter();

	String getName();

	String imagePath();

	Stats copy();

	List<String> info();

	List<String> levelup();

	String[] sideInfoText();

	<T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException;

	List<String> infoEdit();

	List<String> editOptions(int num);

	void applyEditOption(int num, int option, XEntity entity);
}