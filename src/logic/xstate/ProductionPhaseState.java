package logic.xstate;

import logic.*;

public class ProductionPhaseState implements NClickState
{
	public static final ProductionPhaseState INSTANCE = new ProductionPhaseState();

	@Override
	public String text()
	{
		return "Production";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}

	@Override
	public void onMenuClick(int key, MainState mainState)
	{
		mainState.levelMap.productionPhase();
	}
}