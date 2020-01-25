package entity;

public record EnemyMove(XEnemy entity, int initiative, PathLocation moveTo, AttackInfo attackInfo, int tileAdvantage){}