package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;

public interface ItemLoader
{
	Item loadItem(JrsObject data);

	<T extends ComposerBase> void saveItem(ObjectComposer<T> a1, Item item, boolean end) throws IOException;
}