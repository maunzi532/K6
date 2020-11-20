package load;

import com.fasterxml.jackson.jr.stree.*;
import java.util.*;
import java.util.stream.*;

public class LoadHelper
{
	private LoadHelper(){}

	public static int asInt(JrsValue value)
	{
		return ((JrsNumber) value).getValue().intValue();
	}

	public static Stream<JrsValue> asStream(JrsValue value)
	{
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(((JrsArray) value).elements(), Spliterator.ORDERED), false);
	}

	public static int[] asIntArray(JrsValue value)
	{
		return asStream(value).mapToInt(LoadHelper::asInt).toArray();
	}
}