package vis.keybind;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import logic.*;

public final class KeybindFile implements XKeyMap
{
	public static final XKey NONE = new XKey(List.of(), false, false);
	private static final String[] mouseButtonNames =
			{
					"None",
					"Left Click",
					"Middle Click",
					"Right Click",
					"Mouse Back",
					"Mouse Forward"
			};

	private final Map<KeyCode, XKeyBuilder> keyboardKeyBuilders;
	private final Map<MouseButton, XKeyBuilder> mouseKeyBuilders;
	private final Map<KeyCode, XKey> keyboardKeys;
	private final Map<MouseButton, XKey> mouseKeys;
	private final Map<String, String> info;

	public KeybindFile(String input)
	{
		keyboardKeyBuilders = new EnumMap<>(KeyCode.class);
		mouseKeyBuilders = new EnumMap<>(MouseButton.class);
		info = new HashMap<>();
		try
		{
			TreeNode a1 = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
			((JrsArray) a1.get("CanClick")).elements().forEachRemaining(e -> decipher2(e.asText(), null, false));
			((JrsArray) a1.get("CanDrag")).elements().forEachRemaining(e -> decipher2(e.asText(), null, true));
			((JrsArray) a1.get("Functions")).elements().forEachRemaining(this::decipher1);
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		keyboardKeys = keyboardKeyBuilders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().finish()));
		mouseKeys = mouseKeyBuilders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().finish()));
	}

	private void decipher1(JrsValue e)
	{
		String s = e.asText();
		int index = s.indexOf(',');
		decipher2(s.substring(0, index), s.substring(index + 1), false);
	}

	private void decipher2(String input, String t2, boolean drag)
	{
		if(input.charAt(0) == 'K')
		{
			KeyCode keyCode = KeyCode.valueOf(input.substring(1));
			if(!keyboardKeyBuilders.containsKey(keyCode))
				keyboardKeyBuilders.put(keyCode, new XKeyBuilder());
			XKeyBuilder key = keyboardKeyBuilders.get(keyCode);
			if(t2 != null)
			{
				key.functions.add(t2);
				infoAdd(t2, keyCode.getName());
			}
			else if(drag)
				key.canDrag = true;
			else
				key.canClick = true;
		}
		else if(input.charAt(0) == 'M')
		{
			MouseButton keyCode = MouseButton.valueOf(input.substring(1));
			if(!mouseKeyBuilders.containsKey(keyCode))
				mouseKeyBuilders.put(keyCode, new XKeyBuilder());
			XKeyBuilder key = mouseKeyBuilders.get(keyCode);
			if(t2 != null)
			{
				key.functions.add(t2);
				infoAdd(t2, mouseButtonNames[keyCode.ordinal()]);
			}
			else if(drag)
				key.canDrag = true;
			else
				key.canClick = true;
		}
	}

	private void infoAdd(String function, String text)
	{
		if(info.containsKey(function))
		{
			info.put(function, info.get(function) + ", " + text);
		}
		else
		{
			info.put(function, text);
		}
	}

	public XKey keyboardKey(KeyCode keyCode)
	{
		return keyboardKeys.getOrDefault(keyCode, NONE);
	}

	public XKey mouseKey(MouseButton mouseButton)
	{
		return mouseKeys.getOrDefault(mouseButton, NONE);
	}

	@Override
	public String info(String function)
	{
		return info.get(function);
	}
}