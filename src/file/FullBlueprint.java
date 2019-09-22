package file;

import com.fasterxml.jackson.jr.ob.comp.*;
import item.*;
import java.io.*;

public interface FullBlueprint
{
	<T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException;
}