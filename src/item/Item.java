package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import java.io.*;
import java.util.*;
import javafx.scene.image.*;

public interface Item
{
	Image image();

	int weight();

	default boolean canContain(Item item)
	{
		return equals(item);
	}

	List<String> info();

	List<Integer> save();

	<T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException;
}