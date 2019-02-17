package building;

import item.inv.transport.DoubleInv;
import java.util.*;

public interface WithTargets
{
	int range();

	void addTarget(DoubleInv target);

	void removeTarget(DoubleInv target);

	void toggleTarget(DoubleInv target);

	List<DoubleInv> targets();
}