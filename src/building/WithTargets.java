package building;

import inv.*;
import java.util.*;

public interface WithTargets
{
	int range();

	void addTarget(DoubleInv target);

	void removeTarget(DoubleInv target);

	List<DoubleInv> targets();
}