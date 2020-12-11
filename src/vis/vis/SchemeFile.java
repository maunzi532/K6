package vis.vis;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import text.*;
import vis.keybind.*;

public final class SchemeFile implements Scheme
{
	private final HashMap<String, Color> colors;
	private final HashMap<String, Image> images;
	private final HashMap<String, String> settings;
	private final Locale localeFormat;
	private final HashMap<String, String> locale;
	private final KeybindFile keybindFile;

	public SchemeFile(String input, Function<String, String> readFile)
	{
		colors = new HashMap<>();
		images = new HashMap<>();
		settings = new HashMap<>();
		locale = new HashMap<>();
		try
		{
			TreeNode a1 = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
			((JrsObject) a1.get("Colors")).fields().forEachRemaining(e -> colors.put(e.getKey(), Color.web(e.getValue().asText())));
			((JrsObject) a1.get("Images")).fields().forEachRemaining(e -> images.put(e.getKey(), new Image(e.getValue().asText())));
			((JrsObject) a1.get("Settings")).fields().forEachRemaining(e -> settings.put(e.getKey(), e.getValue().asText()));
			TreeNode a2 = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(readFile.apply(setting("file.locale")));
			localeFormat = Locale.forLanguageTag(((JrsValue) a2.get("LocaleFormat")).asText());
			((JrsObject) a2.get("Locale")).fields().forEachRemaining(e -> locale.put(e.getKey(), e.getValue().asText()));
			keybindFile = new KeybindFile(readFile.apply(setting("file.keybind")));
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public KeybindFile keybindFile()
	{
		return keybindFile;
	}

	@Override
	public Color color(String key)
	{
		Color color = colors.get(key);
		if(color == null)
			throw new RuntimeException("Color key \"" + key + "\" missing");
		return color;
	}

	@Override
	public Image image(String key)
	{
		Image image = images.get(key);
		if(image == null)
			throw new RuntimeException("Image key \"" + key + "\" missing or missing Image");
		return image;
	}

	@Override
	public String setting(String key)
	{
		String setting = settings.get(key);
		if(setting == null)
			throw new RuntimeException("Setting key \"" + key + "\" missing");
		return setting;
	}

	@Override
	public int intSetting(String key)
	{
		String setting = settings.get(key);
		if(setting == null)
			throw new RuntimeException("Setting key \"" + key + "\" missing");
		return Integer.parseInt(setting);
	}

	@Override
	public double doubleSetting(String key)
	{
		String setting = settings.get(key);
		if(setting == null)
			throw new RuntimeException("Setting key \"" + key + "\" missing");
		return Double.parseDouble(setting);
	}

	@Override
	public String localXText(CharSequence xText)
	{
		if(xText instanceof String key)
		{
			return localizeAndFormat(key);
		}
		else if(xText instanceof LocaleText localeText)
		{
			return localeText(localeText);
		}
		else if(xText instanceof ArgsText argsText)
		{
			return argsText(argsText);
		}
		else if(xText instanceof NameText nameText)
		{
			return nameText.name();
		}
		else if(xText instanceof MultiText multiText)
		{
			return multiText(multiText);
		}
		else if(xText instanceof KeyFunction keyFunction)
		{
			return keyText(keybindFile.info(keyFunction.function()));
		}
		else
		{
			throw new IllegalArgumentException("Wrong type of CharSequence");
		}
	}

	private String localizeAndFormat(String key)
	{
		return String.format(localeFormat, localize(key));
	}

	private String localize(String key)
	{
		if(!key.isEmpty() && key.charAt(0) == '_')
			return key.substring(1);
		if(!locale.containsKey(key))
			throw new RuntimeException("Localization key \"" + key + "\" missing");
		return locale.getOrDefault(key, "X_" + key);
	}

	private String localeText(LocaleText localeText)
	{
		if(localeText.key() instanceof String text)
		{
			return localizeAndFormat(text);
		}
		else if(localeText.key() instanceof NameText nameText)
		{
			return nameText.name();
		}
		else
		{
			throw new IllegalArgumentException("Wrong type of CharSequence");
		}
	}

	private String argsText(ArgsText argsText)
	{
		return String.format(localeFormat, localize(argsText.key()), Arrays.stream(argsText.args()).map(this::localArg).toArray());
	}

	private Object localArg(Object arg)
	{
		if(arg instanceof String)
			return arg;
		if(arg instanceof CharSequence localeArg)
			return localXText(localeArg);
		return arg;
	}

	private String multiText(MultiText multiText)
	{
		return multiText.parts().stream().map(this::localXText).collect(Collectors.joining(localXText(multiText.connect().text)));
	}

	private String keyText(MultiText keyText)
	{
		return keyText.parts().stream().map(this::localArg2).collect(Collectors.joining(localXText(keyText.connect().text)));
	}

	private String localArg2(CharSequence arg)
	{
		if(arg instanceof String text)
			return text;
		return localXText(arg);
	}
}