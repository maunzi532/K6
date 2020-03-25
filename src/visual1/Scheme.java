package visual1;

import javafx.scene.image.*;
import javafx.scene.paint.*;

public interface Scheme
{
	Color color(String key);

	Image image(String key);
}