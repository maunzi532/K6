package entity;

import javafx.scene.paint.*;

public interface AnimTimer
{
	Color HERO_HEALTH = Color.GREEN;
	Color ENEMY_HEALTH = Color.GRAY;
	Color EXP_COLOR = Color.PURPLE;
	Color ARROW_BACKGROUND = Color.BLACK;
	Color ARROW_TEXT = Color.WHITE;

	boolean finished();

	void tick();
}