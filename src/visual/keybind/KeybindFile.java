package visual.keybind;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import javafx.scene.input.*;
import logic.*;

public class KeybindFile implements XKeyMap
{
	public static final XKey NONE = new FXKey();
	private static final String[] mouseButtonNames = new String[]
			{
					"None",
					"Left Click",
					"Middle Click",
					"Right Click",
					"Mouse Back",
					"Mouse Forward"
			};

	public final Map<KeyCode, XKey> keyboardKeys;
	public final Map<MouseButton, XKey> mouseKeys;
	private final Map<String, String> info;

	public KeybindFile(String input)
	{
		keyboardKeys = new HashMap<>();
		mouseKeys = new HashMap<>();
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
	}

	private void decipher1(JrsValue e)
	{
		String s = e.asText();
		int index = s.indexOf(',');
		decipher2(s.substring(0, index), s.substring(index + 1), false);
	}

	private void decipher2(String input, String t2, boolean d)
	{
		if(input.charAt(0) == 'K')
		{
			KeyCode keyCode = KeyCode.valueOf(input.substring(1));
			if(!keyboardKeys.containsKey(keyCode))
				keyboardKeys.put(keyCode, new FXKey());
			FXKey key = (FXKey) keyboardKeys.get(keyCode);
			if(t2 != null)
			{
				key.functions.add(t2);
				infoAdd(t2, keyCode.getName());
			}
			else if(d)
				key.canDrag = true;
			else
				key.canClick = true;
		}
		else if(input.charAt(0) == 'M')
		{
			MouseButton keyCode = MouseButton.valueOf(input.substring(1));
			if(!mouseKeys.containsKey(keyCode))
				mouseKeys.put(keyCode, new FXKey());
			FXKey key = (FXKey) mouseKeys.get(keyCode);
			if(t2 != null)
			{
				key.functions.add(t2);
				infoAdd(t2, mouseButtonNames[keyCode.ordinal()]);
			}
			else if(d)
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

	@Override
	public String info(String function)
	{
		return info.get(function);
	}
}