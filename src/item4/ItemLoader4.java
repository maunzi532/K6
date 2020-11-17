package item4;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;

public interface ItemLoader4
{
	Item4 loadItem(JrsObject data);

	<T extends ComposerBase> void saveItem(ObjectComposer<T> a1, Item4 item) throws IOException;
}