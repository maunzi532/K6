package load;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import system4.*;

public class LoadHelper
{
	private LoadHelper(){}

	public static int asInt(JrsValue value)
	{
		return ((JrsNumber) value).getValue().intValue();
	}

	public static String asOptionalString(JrsValue value)
	{
		return value != null ? value.asText() : null;
	}

	public static Stream<JrsValue> asStream(JrsValue value)
	{
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(((JrsArray) value).elements(), Spliterator.ORDERED), false);
	}

	public static int[] asIntArray(JrsValue value)
	{
		return asStream(value).mapToInt(LoadHelper::asInt).toArray();
	}

	public static void saveObject(String key, XSaveable save,
			ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		var a2 = a1.startObjectField(key);
		save.save(a2, systemScheme);
		a2.end();
	}

	public static void saveList(String key, List<? extends XSaveable> save,
			ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		var a2 = a1.startArrayField(key);
		for(XSaveable saveable : save)
		{
			var a3 = a2.startObject();
			saveable.save(a3, systemScheme);
			a3.end();
		}
		a2.end();
	}
}