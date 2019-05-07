package io.server.game.world.entity.combat.attack.listener.npc;

public class Vorkath {// extends SimplifiedListener<Npc> {
	/*
	 * 
	 * @Override public boolean canAttack(Character entity, Character victim) {
	 * return true; }
	 * 
	 * @Override public CombatContainer attack(Character entity, Character victim) {
	 * return null; }
	 * 
	 * private static final Animation shoot = new Animation(7952); private static
	 * final Animation up = new Animation(7960);
	 * 
	 * private static final int blue = 1479; private static final int blueBlast =
	 * 1480; private static final int pink = 1471; private static final int
	 * pinkBlast = 1473; private static final int green = 1470; private static final
	 * int greenBlast = 1472; private static final int red = 1481; private static
	 * final int redBlast = 157;
	 * 
	 * private static final int fire = 393;
	 * 
	 * private static final int ranged = 1477;
	 * 
	 * @Override public boolean customContainerAttack(Character entity, Character
	 * victim) { Npc dragon = (Npc) entity; if (dragon.isChargingAttack() ||
	 * dragon.getConstitution() <= 0) { dragon.getCombatBuilder().setAttackTimer(4);
	 * return true; } Player target = (Player) victim;
	 * 
	 * int random = Misc.getRandom(4);
	 * 
	 * if (random == 0) { dragon.setChargingAttack(true);
	 * dragon.performAnimation(shoot); dragon.getCombatBuilder() .setContainer(new
	 * CombatContainer(dragon, victim, 1, 3, CombatType.DRAGON_FIRE, true));
	 * TaskManager.submit(new Task(1, dragon, false) { int tick = 0;
	 * 
	 * @Override public void execute() {
	 * 
	 * switch (tick) { case 0: new Projectile(dragon, victim, fire, 44, 3, 31, 31,
	 * 0).sendProjectile(); break; case 2:
	 * dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6); stop();
	 * break; } tick++; } });
	 * 
	 * // } else if (random == 1) { dragon.setChargingAttack(true);
	 * dragon.performAnimation(shoot); dragon.getCombatBuilder().setContainer(new
	 * CombatContainer(dragon, victim, 1, 3, CombatType.RANGED, true));
	 * TaskManager.submit(new Task(1, dragon, true) { int tick = 0;
	 * 
	 * @Override public void execute() {
	 * 
	 * switch (tick) { case 0: new Projectile(dragon, victim, ranged, 44, 3, 25, 31,
	 * 0).sendProjectile(); break; case 2:
	 * dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6); stop();
	 * break; } tick++; } });
	 * 
	 * } else if (random == 2) { dragon.setChargingAttack(true);
	 * dragon.performAnimation(shoot); dragon.getCombatBuilder().setContainer(new
	 * CombatContainer(dragon, victim, 1, 3, CombatType.MAGIC, true));
	 * TaskManager.submit(new Task(1, dragon, true) { int tick = 0;
	 * 
	 * @Override public void execute() {
	 * 
	 * switch (tick) { case 0: new Projectile(dragon, victim, pink, 44, 3, 31, 31,
	 * 0).sendProjectile(); break; case 2:
	 * target.sendMessage("@red@Your prayers have been disabled!");
	 * CurseHandler.deactivateAll(target); PrayerHandler.deactivateAll(target);
	 * victim.performGraphic(new Graphic(pinkBlast, GraphicHeight.HIGH));
	 * dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6); stop();
	 * break; } tick++; } });
	 * 
	 * } else if (random == 3) { dragon.setChargingAttack(true);
	 * dragon.performAnimation(shoot); dragon.getCombatBuilder().setContainer(new
	 * CombatContainer(dragon, victim, 1, 3, CombatType.MAGIC, true));
	 * TaskManager.submit(new Task(1, dragon, true) { int tick = 0;
	 * 
	 * @Override public void execute() {
	 * 
	 * switch (tick) { case 0: new Projectile(dragon, victim, green, 44, 3, 31, 31,
	 * 0).sendProjectile(); break; case 2: target.venomVictim(target,
	 * CombatType.MAGIC); victim.performGraphic(new Graphic(greenBlast,
	 * GraphicHeight.HIGH));
	 * dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6); stop();
	 * break; } tick++; } });
	 * 
	 * } else if (random >= 4) { dragon.setChargingAttack(true);
	 * dragon.performAnimation(up); Position savew = victim.getPosition(); Npc
	 * position = new Npc(1, victim.getPosition()); World.register(target,
	 * position); TaskManager.submit(new Task(1, dragon, false) { int tick = 0;
	 * 
	 * @Override public void execute() {
	 * 
	 * switch (tick) { case 0: new Projectile(dragon, position, red, 80, 1, 100, 31,
	 * 0).sendProjectile(); break; case 3:
	 * target.getPacketSender().sendGlobalGraphic(new Graphic(redBlast,
	 * GraphicHeight.HIGH), savew); if (target.getPosition() == savew)
	 * target.dealDamage( new Hit(400 + RandomUtility.getRandom(300), Hitmask.RED,
	 * CombatIcon.BLOCK));
	 * 
	 * dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6); stop();
	 * break; } tick++; } }); } else { dragon.setChargingAttack(true);
	 * dragon.performAnimation(shoot); dragon.getCombatBuilder().setContainer(new
	 * CombatContainer(dragon, victim, 1, 3, CombatType.MAGIC, true));
	 * TaskManager.submit(new Task(1, dragon, true) { int tick = 0;
	 * 
	 * @Override public void execute() {
	 * 
	 * switch (tick) { case 0: new Projectile(dragon, victim, blue, 44, 3, 31, 31,
	 * 0).sendProjectile(); break; case 2: victim.performGraphic(new
	 * Graphic(blueBlast, GraphicHeight.HIGH));
	 * dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6); stop();
	 * break; } tick++; }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * return true; }
	 * 
	 * @Override public int attackDelay(Character entity) { return
	 * entity.getAttackSpeed(); }
	 * 
	 * @Override public int attackDistance(Character entity) { return 50; }
	 * 
	 * @Override public CombatType getCombatType() { return CombatType.MIXED; }
	 */
}
