package visual.keybind;

import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import javafx.scene.input.*;
import logic.*;

public class KeybindFile
{
	public static final XKey NONE = new FXKey();
	public final Map<KeyCode, XKey> keyboardKeys;
	public final Map<MouseButton, XKey> mouseKeys;

	public KeybindFile(String input)
	{
		keyboardKeys = new HashMap<>();
		mouseKeys = new HashMap<>();
		try
		{
			var a1 = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
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
				key.functions.add(t2);
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
				key.functions.add(t2);
			else if(d)
				key.canDrag = true;
			else
				key.canClick = true;
		}
	}

	/*
	CanClick:
	[
	Ka Md Kf
	]
	CanDrag:
	[
	Md
	]
	Functions:
	[
		"Ka,b"
		"Ka,c"
		"Md,e"
	]
	 */
}