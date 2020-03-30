package statsystem.analysis;

import statsystem.*;

public record SidedAttackAnalysisEvent(AttackAnalysisEvent event, AttackSide side){}