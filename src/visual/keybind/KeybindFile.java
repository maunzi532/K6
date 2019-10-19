package visual.keybind;

import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import javafx.scene.input.*;
import logic.*;

public class KeybindFile
{
	private Map<KeyCode, FXKey> keyboardKeys;
	private Map<MouseButton, FXKey> mouseKeys;

	public XKey getKeyForKeyboard(KeyCode keyCode)
	{
		return keyboardKeys.get(keyCode);
	}

	public XKey getKeyForMouse(MouseButton keyCode)
	{
		return mouseKeys.get(keyCode);
	}

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
			if(t2 != null)
				keyboardKeys.get(keyCode).functions.add(t2);
			else if(d)
				keyboardKeys.get(keyCode).canDrag = true;
			else
				keyboardKeys.get(keyCode).canClick = true;
		}
		else if(input.charAt(0) == 'M')
		{
			MouseButton keyCode = MouseButton.valueOf(input.substring(1));
			if(!mouseKeys.containsKey(keyCode))
				mouseKeys.put(keyCode, new FXKey());
			if(t2 != null)
				mouseKeys.get(keyCode).functions.add(t2);
			else if(d)
				mouseKeys.get(keyCode).canDrag = true;
			else
				mouseKeys.get(keyCode).canClick = true;
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