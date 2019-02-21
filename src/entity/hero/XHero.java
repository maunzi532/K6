package entity.hero;

import building.*;
import building.blueprint.*;
import entity.*;
import geom.hex.*;
import item.*;
import item.inv.*;
import item.inv.transport.*;
import java.util.*;
import javafx.scene.image.*;
import logic.*;

public class XHero extends XEntity implements DoubleInv
{
	private static final Image CL3 = new Image("S.png");

	private MainState mainState;
	private Inv inv;

	public XHero(Hex location, MainState mainState)
	{
		super(location);
		this.mainState = mainState;
		inv = new WeightInv(20);
	}

	@Override
	public Image getImage()
	{
		return CL3;
	}

	@Override
	public String name()
	{
		return "XHero";
	}

	@Override
	public Inv inputInv()
	{
		return inv;
	}

	@Override
	public Inv outputInv()
	{
		return inv;
	}

	@Override
	public int inputPriority()
	{
		return -1;
	}

	@Override
	public int outputPriority()
	{
		return -1;
	}

	public void addItems(ItemList itemList)
	{
		inv.tryAdd(itemList, false, CommitType.COMMIT);
	}

	public Optional<ItemList> tryBuildingCosts(CostBlueprint cost)
	{
		//check floor tiles
		if(inv.tryProvide(cost.costs, false, CommitType.LEAVE).isEmpty())
			return Optional.empty();
		return inv.tryProvide(cost.refundable, false, CommitType.COMMIT);
	}

	public void buildBuilding(CostBlueprint costs, ItemList refundable, BuildingBlueprint blueprint)
	{
		mainState.levelMap.addBuilding(blueprint.type == 0 ? new ProductionBuilding(location, costs, refundable, blueprint)
				: new Transporter(location, costs, refundable, blueprint));
	}

	public void removeBuilding(Buildable building)
	{
		mainState.levelMap.removeBuilding(building);
	}
}