package event.event;

import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.tile.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import event.events.*;

public final class EventPack
{
	private final List<NEvent> events;

	public EventPack(JrsArray data, TileType y1)
	{
		events = new ArrayList<>();
		data.elements().forEachRemaining(e -> addEvent((JrsObject) e, y1));
	}

	private void addEvent(JrsObject element, TileType y1)
	{
		events.add(switch(element.get("EventType").asText())
				{
					case "Text" -> new TextEvent(element);
					default -> throw new IllegalArgumentException("Unexpected event type: " + element.get("EventType").asText());
				});
	}

	public List<? extends NEvent> events()
	{
		return events;
	}

	public static Map<String, EventPack> read(CharSequence world, CharSequence level, String language, TileType y1)
	{
		try
		{
			String input = Files.readString(new File(world.toString(), level + language).toPath());
			var data = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
			if(((JrsNumber) data.get("code")).getValue().intValue() == 0xA4D2839F)
			{
				Map<String, JrsArray> all = new HashMap<>();
				data.fieldNames().forEachRemaining(e ->
				{
					if(data.get(e) instanceof JrsArray arr)
						all.put(e, arr);
				});
				return all.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new EventPack(e.getValue(), y1)));
			}
			else
				throw new RuntimeException("No code");
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}