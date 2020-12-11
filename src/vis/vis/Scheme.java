package vis.vis;

import javafx.scene.image.*;
import javafx.scene.paint.*;

public interface Scheme
{
	Color color(String key);

	Image image(String key);

	String setting(String key);

	int intSetting(String key);

	double doubleSetting(String key);

	String localXText(CharSequence xText);
}