package load;

import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class LoadHelper
{
	private LoadHelper(){}

	public static int asInt(JrsValue value)
	{
		return ((JrsNumber) value).getValue().intValue();
	}

	public static boolean asBoolean(JrsValue value)
	{
		return ((JrsBoolean) value).booleanValue();
	}

	public static String asOptionalString(JrsValue value)
	{
		return value != null ? value.asText() : null;
	}

	public static Stream<JrsValue> asStream(JrsValue value)
	{
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(((JrsArray) value).elements(), Spliterator.ORDERED), false);
	}

	public static Stream<Map.Entry<String, JrsValue>> asStreamObject(JrsValue value)
	{
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(((JrsObject) value).fields(), Spliterator.ORDERED), false);
	}

	public static <T> List<T> asList(JrsValue value, Function<? super JrsObject, ? extends T> load)
	{
		return asStream(value).map(e -> load.apply((JrsObject) e)).collect(Collectors.toList());
	}

	public static List<String> asStringList(JrsValue value)
	{
		return asStream(value).map(JrsValue::asText).collect(Collectors.toList());
	}

	public static int[] asIntArray(JrsValue value)
	{
		return asStream(value).mapToInt(LoadHelper::asInt).toArray();
	}

	public static Map<String, Integer> asIntMap(JrsValue value)
	{
		return asStreamObject(value).filter(e -> e.getValue() instanceof JrsNumber)
				.collect(Collectors.toMap(Map.Entry::getKey, e -> asInt(e.getValue())));
	}

	public static Map<String, String> asStringMap(JrsValue value)
	{
		return asStreamObject(value).filter(e -> e.getValue() instanceof JrsString)
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().asText()));
	}

	public static JrsObject startLoad(Path path) throws IOException
	{
		return JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(Files.readString(path));
	}

	public static ObjectComposer<JSONComposer<String>> startSave() throws IOException
	{
		return JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
				.composeString()
				.startObject();
	}

	public static String endSave(ObjectComposer<? extends JSONComposer<String>> a1) throws IOException
	{
		return a1.end().finish();
	}
}