/**
 * @author Michael Yang
 * @since   7/28/2019
 */

import java.util.ArrayList;

public class Ship extends Craft {

	private boolean isPlayer;
	private boolean isDodging;
	private boolean pdActive;
	private double heat;
	private double heatCapacity;
	private double coolingAmount;
	private int[] munitions;
	private String[] weapons;
	private ArrayList<Drone> drones;
	private boolean dronesLaunched;
	private boolean pdJustRepaired;
	private ArrayList<StatusEffect> effects;
	private double chanceToFail;
	
	public Ship(boolean isPlayerI, double hullI, double armorI, double shieldI, double shieldRegenI, double[] armorResistI, double[] shieldResistI, double evasionI, double heatCapacityI, double coolingAmountI, int[] munitionsI, String[] weaponsI, int engineHealthI, int shieldHealthI) {
		super(hullI, armorI, shieldI, shieldRegenI, armorResistI, shieldResistI, evasionI, engineHealthI, shieldHealthI);
		isPlayer = isPlayerI;
		isDodging = false;
		pdActive = true;
		heat = 0;
		heatCapacity = heatCapacityI;
		coolingAmount = coolingAmountI;
		munitions = munitionsI;
		weapons = weaponsI;
		drones = new ArrayList<Drone>();
		setDrones(5);
		dronesLaunched = false;
		pdJustRepaired = false;
		effects = new ArrayList<StatusEffect>();
		chanceToFail = .05;
	}
	
	public boolean regenShield() {
		if(hasStatusEffect("EMPSE")) {
			return false;
		}
		return super.regenShield();
	}
	
	public void toggleDodging() {
		isDodging = !isDodging;
	}
	
	public boolean getPDState() {
		return pdActive;
	}
	
	public void togglePD() {
		pdActive = !pdActive;
	}
	
	public double getHeat() {
		return heat;
	}
	
	public void setHeat(double newHeat) {
		heat = newHeat;
	}
	
	public void addHeat(double heatAmount) {
		heat += heatAmount;
	}
	
	public void removeHeat(double heatAmount) {
		if(getHeat() - heatAmount <= 0) {
			heat = 0;
			return;
		}
		heat -= heatAmount;
	}
	
	public void coolShip() {
		removeHeat(coolingAmount);
	}
	
	public double getHeatCapacity() {
		return heatCapacity;
	}
	
	public int getMunitions(int i) {
		return munitions[i];
	}
	
	public void setMunitions(int i, int munitionCount) {
		munitions[i] = munitionCount;
	}
	
	public void consumeMunitions(int i) {
		if(munitions[i] == 0) {
			return;
		}
		munitions[i]--;
	}
	
	public String getWeapon(int i) {
		return weapons[i];
	}
	
	public void setWeapons(String[] newWeapons) {
		weapons = newWeapons;
	}
	
	public boolean hasWeapon(String weaponID) {
		for(int i = 0; i < weapons.length; i++) {
			if(weapons[i].equals(weaponID)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean dronesActive() {
		return dronesLaunched;
	}
	
	public void toggleDrones() {
		dronesLaunched = !dronesLaunched;
	}
	
	public Drone getDrone(int i) {
		return drones.get(i);
	}
	
	public int getActiveDrones() {
		return drones.size();
	}
	
	public void setDrones(int droneCount) {
		drones.clear();
		for(int i = 0; i < droneCount; i++) {
			drones.add(new Drone(1, 2, 0, 0, new double[] {0, .25, .5, 0}, new double[] {.5, .25, 0, 0}, .75, 10, 10));
		}
	}
	
	public void removeDestroyedDrones() {
		for(int i = 0; i < drones.size(); i++) {
			if(drones.get(i).getHull() <= 0) {
				drones.remove(i);
				i--;
				System.out.println("    Drone Destroyed");
			}
		}
	}
	
	public String getDroneString() {
		String result = "";
		for(int i = 0; i < drones.size(); i++) {
			result += drones.get(i).toString() + " ";
		}
		return result;
	}
	
	
	public boolean getPDRepairStatus() {
		return pdJustRepaired;
	}
	
	public void togglePDRepair() {
		pdJustRepaired = !pdJustRepaired;
	}
	
	public void repairPD() {
		if(pdJustRepaired && !getPDState()) {
			togglePD();
			togglePDRepair();
		}
	}
	
	public void addStatusEffect(String type, int duration) {
		effects.add(new StatusEffect(type, duration));
	}
	
	public boolean hasStatusEffect(String type) {
		for(int i = 0; i < effects.size(); i++) {
			if(effects.get(i).getType().equals(type)) {
				return true;
			}
		}
		return false;
	}
	
	public void applyStatusEffects() {
		System.out.println("\nSTATUS EFFECTS");
		if(effects.isEmpty()) {
			System.out.println("    None");
			return;
		}
		
		for(int i = 0; i < effects.size(); i++) {
			applyEffect(effects.get(i).getType());
			System.out.printf("    Turns Left    -  %2d\n", effects.get(i).getTurns() - 1);
			effects.get(i).decrementTurns();
			if(effects.get(i).getTurns() <= 0) {
				revertEffect(effects.get(i).getType());
			}
			if(effects.get(i).getTurns() <= 0) {
				effects.remove(i);
				i--;
			}
		}
	}
	
	public void applyEffect(String type) {
		switch(type) {
		case "COROS":
			System.out.println("  CORROSION");
			damageShip(this, 0, 3, -1, true, true, false);
			if(isPlayer) {
				System.out.println("    Your Armor Resists Are Halved");
			} else {
				System.out.println("    Enemy Armor Resists Are Halved");
			}
			for(int i = 0; i <= 3; i++) {
				if(getArmorResist(i) == getOriginalArmorResist(i)) {
					setArmorResist(i, getArmorResist(i) / 2);
				}
			}
			break;
		case "AFBRN":
			System.out.println("  AFTERBURN");
			damageShip(this, 1, 4, -1, true, false, false);
			heatTarget(this, 5);
			break;
		case "EMPSE":
			System.out.println("  ELECTROMAGNETIC PULSE");
			if(isPlayer) {
				System.out.println("    Your Subsystems Have Been Paralyzed");
			} else {
				System.out.println("    Enemy Subsystems Have Been Paralyzed");
			}
			break;
		}
	}
	
	public void revertEffect(String type) {
		switch(type) {
		case "COROS":
			revertArmorResist(0);
			revertArmorResist(1);
			revertArmorResist(2);
			revertArmorResist(3);
			break;
		case "AFBRN":
			break;
		case "EMPSE":
			break;
		}
	}
	
	public boolean damageShip(Craft target, int damageType, double damage, double critRate, boolean alwaysAccurate, boolean ignoreShield, boolean ignoreArmor) {
		String type = "";
		boolean crit = Math.random() < critRate;
		if(crit) {
			damage *= 2;
		}
		switch(damageType) {
		case 0:
			type = "Kinetic";
			break;
		case 1:
			type = "Thermal";
			break;
		case 2:
			type = "EM";
			break;
		case 3:
			type = "Omni";
			break;
		}
		if(alwaysAccurate || !target.getEngineState() || Math.random() > target.getEvasion()) {
			if(ignoreShield || target.getShield() <= 0) {
				
			} else if(target.getShield() < (1 - target.getShieldResist(damageType)) * damage) {
				double shieldTemp = target.getShield();
				damage = damage - (shieldTemp / (1 - target.getShieldResist(damageType)));
				target.setShield(0);
				if(isPlayer) {
					System.out.printf("    Your Shield   -  %5.2f " + type, shieldTemp);
				} else {
					System.out.printf("    Enemy Shield  -  %5.2f " + type, shieldTemp);
				}
				if(crit) {
					System.out.print(" (Critical)");
				}
				System.out.println();
			} else {
				target.damageShield(damage, damageType);
				if(isPlayer) {
					System.out.printf("    Your Shield   -  %5.2f " + type, (1 - target.getShieldResist(damageType)) * damage);
				} else {
					System.out.printf("    Enemy Shield  -  %5.2f " + type, (1 - target.getShieldResist(damageType)) * damage);
				}
				if(crit) {
					System.out.print(" (Critical)");
				}
				System.out.println();
				return true;
			}
			if(ignoreArmor || target.getArmor() == 0) {
				
			} else if(target.getArmor() < (1 - target.getArmorResist(damageType)) * damage) {
				double armorTemp = target.getArmor();
				damage = damage - (target.getArmor() / (1 - target.getArmorResist(damageType)));
				target.setArmor(0);
				if(isPlayer) {
					System.out.printf("    Your Armor    -  %5.2f " + type, armorTemp);
				} else {
					System.out.printf("    Enemy Armor   -  %5.2f " + type, armorTemp);
				}
				if(crit) {
					System.out.print(" (Critical)");
				}
				System.out.println();
			} else {
				target.damageArmor(damage, damageType);
				if(isPlayer) {
					System.out.printf("    Your Armor    -  %5.2f " + type, (1 - target.getArmorResist(damageType)) * damage);
				} else {
					System.out.printf("    Enemy Armor   -  %5.2f " + type, (1 - target.getArmorResist(damageType)) * damage);
				}
				if(crit) {
					System.out.print(" (Critical)");
				}
				System.out.println();
				return true;
			}
			target.damageHull(damage);
			if(isPlayer) {
				System.out.printf("    Your Hull     -  %5.2f " + type, damage);
			} else {
				System.out.printf("    Enemy Hull    -  %5.2f " + type, damage);
			}
			if(crit) {
				System.out.print(" (Critical)");
			}
			System.out.println();
			return true;
		} else {
			System.out.println("    Miss");
			return false;
		}
	}
	
	public void heatTarget(Ship target, double heatAmount) {
		if(isPlayer) {
			System.out.printf("    Your Ship     -  %2.0f Heat", heatAmount);
			target.addHeat(heatAmount);
		} else {
			System.out.printf("    Enemy Ship    -  %2.0f Heat", heatAmount);
			target.addHeat(heatAmount);
		}
		System.out.println();
	}
	
	public double getChanceToFail() {
		return chanceToFail;
	}
	
	public void setChanceToFail(double newChance) {
		chanceToFail = newChance;
	}
	
	public void addChanceToFail(double additionalChance) {
		chanceToFail += additionalChance;
	}
}
