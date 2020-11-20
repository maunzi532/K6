package load;

import com.fasterxml.jackson.jr.stree.*;
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
}