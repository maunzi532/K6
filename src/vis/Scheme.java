package vis;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import text.*;

public interface Scheme
{
	Color color(String key);

	Image image(String key);

	String setting(String key);

	int intSetting(String key);

	double doubleSetting(String key);

	String local(String key, Object... args);

	String localXText(XText xText);
}