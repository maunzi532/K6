package entity;

import java.util.*;
import javafx.scene.image.*;

public interface XMode
{
	XMode shortVersion();

	Image image();

	String tile();

	List<String> modeInfo();
}