package load;

import com.fasterxml.jackson.jr.ob.comp.*;
import java.io.*;
import system4.*;

public interface XSaveable
{
	void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException;
}