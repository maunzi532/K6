package system4;

import com.fasterxml.jackson.jr.ob.comp.*;
import java.io.*;

@FunctionalInterface
public interface XSaveable
{
	void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException;
}