package entity;

import statsystem.*;

public record EnemyMove(XCharacter entity, int initiative, PathLocation moveTo, AttackInfo attackInfo, int tileAdvantage){}