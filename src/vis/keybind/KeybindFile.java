package vis.keybind;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import logic.*;
import text.*;

public final class KeybindFile
{
	public static final XKey NONE = new XKey(List.of(), false, false);
	private static final char INPUT_SEPARATOR = ',';
	private static final CharSequence[] mouseButtonNames =
			{
					new LocaleText("mouse.0"),
					new LocaleText("mouse.1"),
					new LocaleText("mouse.2"),
					new LocaleText("mouse.3"),
					new LocaleText("mouse.4"),
					new LocaleText("mouse.5")
			};

	private final Map<KeyCode, XKeyBuilder> keyboardKeyBuilders;
	private final Map<MouseButton, XKeyBuilder> mouseKeyBuilders;
	private final Map<String, List<CharSequence>> infoBuilders;
	private final Map<KeyCode, XKey> keyboardKeys;
	private final Map<MouseButton, XKey> mouseKeys;
	private final Map<String, MultiText> info;

	public KeybindFile(String input)
	{
		keyboardKeyBuilders = new EnumMap<>(KeyCode.class);
		mouseKeyBuilders = new EnumMap<>(MouseButton.class);
		infoBuilders = new HashMap<>();
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
		info = infoBuilders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new MultiText(e.getValue(), MultiTextConnect.LISTED)));
	}

	private void decipher1(JrsValue e)
	{
		String s = e.asText();
		int index = s.indexOf(INPUT_SEPARATOR);
		decipher2(s.substring(0, index), s.substring(index + 1), false);
	}

	private void decipher2(String input, String t2, boolean drag)
	{
		if(input.startsWith("key."))
		{
			KeyCode keyCode = KeyCode.valueOf(input.substring(4));
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
		else if(input.startsWith("mouse."))
		{
			MouseButton keyCode = MouseButton.valueOf(input.substring(6));
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

	private void infoAdd(String function, CharSequence text)
	{
		if(!infoBuilders.containsKey(function))
			infoBuilders.put(function, new ArrayList<>());
		infoBuilders.get(function).add(text);
	}

	public XKey keyboardKey(KeyCode keyCode)
	{
		return keyboardKeys.getOrDefault(keyCode, NONE);
	}

	public XKey mouseKey(MouseButton mouseButton)
	{
		return mouseKeys.getOrDefault(mouseButton, NONE);
	}

	public MultiText info(String function)
	{
		return info.get(function);
	}
}