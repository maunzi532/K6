package building;

import arrow.*;
import inv.*;
import java.util.*;

public class Transporter implements Building, WithTargets
{
	private List<DoubleInv> targets;
	private InvTransporter invTransporter;

	public Transporter()
	{
		targets = new ArrayList<>();
		invTransporter = new InvTransporter(targets, targets);
	}

	@Override
	public void transportPhase(CanAddArrows canAddArrows)
	{
		Optional<PossibleTransport> transportOpt = invTransporter.transport();
		if(transportOpt.isPresent())
		{
			invTransporter.doTheTransport(transportOpt.get());
			//canAddArrows.addArrow(new VisualArrow());
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
	public List<DoubleInv> targets()
	{
		return targets;
	}
}