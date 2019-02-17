package building;

import arrow.*;
import geom.hex.*;
import item.ItemList;
import item.inv.transport.*;
import java.util.*;

public class Transporter extends Buildable implements WithTargets
{
	private List<DoubleInv> targets;
	private InvTransporter invTransporter;

	public Transporter(Hex location)
	{
		super(location, null, new ItemList());
		targets = new ArrayList<>();
		invTransporter = new InvTransporter(targets, targets);
	}

	@Override
	public void transportPhase(CanAddArrows canAddArrows)
	{
		Optional<PossibleTransport> transportOpt = invTransporter.transport();
		if(transportOpt.isPresent())
		{
			PossibleTransport transport = transportOpt.get();
			invTransporter.doTheTransport(transport);
			canAddArrows.addArrow(new VisualArrow(transport.from.location(),
					transport.to.location(), ArrowMode.TARROW, 60, transport.item.image()));
		}
	}

	@Override
	public int range()
	{
		return 2;
	}

	@Override
	public void addTarget(DoubleInv target)
	{
		targets.add(target);
	}

	@Override
	public void removeTarget(DoubleInv target)
	{
		targets.remove(target);
	}

	@Override
	public void toggleTarget(DoubleInv target)
	{
		if(targets.contains(target))
			targets.remove(target);
		else
			targets.add(target);
	}

	@Override
	public List<DoubleInv> targets()
	{
		return targets;
	}
}