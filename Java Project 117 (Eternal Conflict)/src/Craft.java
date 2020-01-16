/**
 * @author Michael Yang
 * @since   7/28/2019
 */

public abstract class Craft {
	
	private double hull;
	private double armor;
	private double shield;
	private double shieldRegen;
	private double originalShieldRegen;
	private double[] armorResist;
	private double[] originalArmorResist;
	private double[] shieldResist;
	private double[] originalShieldResist;
	private double evasion;
	private double originalEvasion;
	private double[] maxHealth;
	private boolean engineState;
	private int engineHealth;
	private int maxEngineHealth;
	private boolean enginesJustRepaired;
	private boolean shieldState;
	private int shieldHealth;
	private int maxShieldHealth;
	private boolean shieldsJustRepaired;
	
	public Craft(double hullI, double armorI, double shieldI, double shieldRegenI, double[] armorResistI, double [] shieldResistI, double evasionI, int engineHealthI, int shieldHealthI) {
		hull = hullI;
		armor = armorI;
		shield = shieldI;
		shieldRegen = shieldRegenI;
		originalShieldRegen = shieldRegenI;
		armorResist = armorResistI;
		originalArmorResist = copyArray(armorResist);
		shieldResist = shieldResistI;
		originalShieldResist = copyArray(shieldResist);
		evasion = evasionI;
		originalEvasion = evasionI;
		maxHealth = new double[] {hullI, armorI, shieldI};
		engineState = true;
		engineHealth = engineHealthI;
		maxEngineHealth = engineHealthI;
		enginesJustRepaired = false;
		shieldState = true;
		shieldHealth = shieldHealthI;
		maxShieldHealth = shieldHealthI;
		shieldsJustRepaired = false;
	}
	
	public double getHull() {
		return hull;
	}
	
	public void setHull(double newHull) {
		hull = newHull;
	}
	
	public void damageHull(double damage) {
		hull -= damage;
	}
	
	public void healHull(double healAmount) {
		if(hull + healAmount > maxHealth[0]) {
			hull = maxHealth[0];
		} else {
			hull += healAmount;
		}
	}
	
	public double getArmor() {
		return armor;
	}
	
	public void setArmor(double newArmor) {
		armor = newArmor;
	}
	
	public void damageArmor(double damage, int type) {
		armor = armor - damage * (1 - armorResist[type]);
	}
	
	public void healArmor(double healAmount) {
		if(armor + healAmount > maxHealth[1]) {
			armor = maxHealth[1];
		} else {
			armor += healAmount;
		}
	}
	
	public double getArmorResist(int type) {
		return armorResist[type];
	}
	
	public void setArmorResist(int type, double newResist) {
		armorResist[type] = newResist;
	}
	
	public void revertArmorResist(int type) {
		armorResist[type] = originalArmorResist[type];
	}
	
	public double getOriginalArmorResist(int type) {
		return originalArmorResist[type];
	}
	
	public double getShield() {
		return shield;
	}
	
	public void setShield(double newShield) {
		shield = newShield;
	}
	
	public void damageShield(double damage, int type) {
		shield = shield - damage * (1 - shieldResist[type]);
	}
	
	public void healShield(double healAmount) {
		if(shield + healAmount > maxHealth[2]) {
			shield = maxHealth[2];
		} else {
			shield += healAmount;
		}
	}
	
	public boolean regenShield() {
		if(!getShieldState()) {
			return false;
		}
		if(shield + shieldRegen > maxHealth[2]) {
			shield = maxHealth[2];
		} else {
			shield += shieldRegen;
		}
		return true;
	}
	
	public double getMaxHealth(int index) {
		return maxHealth[index];
	}
	
	public double getShieldResist(int type) {
		return shieldResist[type];
	}
	
	public void setShieldResist(int type, double newResist) {
		shieldResist[type] = newResist;
	}
	
	public void revertShieldResist(int type) {
		shieldResist[type] = originalShieldResist[type];
	}
	
	public double getOriginalShieldResist(int type) {
		return originalShieldResist[type];
	}
	
	public double getRegen() {
		return shieldRegen;
	}
	
	public void setRegen(double newRegen) {
		shieldRegen = newRegen;
	}
	
	public void revertRegen() {
		shieldRegen = originalShieldRegen;
	}
	
	public double getEvasion() {
		return evasion;
	}
	
	public void setEvasion(double newEvasion) {
		evasion = newEvasion;
	}
	
	public void revertEvasion() {
		evasion = originalEvasion;
	}
	
	public boolean getEngineState() {
		return engineState;
	}
	
	public void toggleEngines() {
		engineState = !engineState;
	}
	
	public boolean getEngineRepairStatus() {
		return enginesJustRepaired;
	}
	
	public void toggleEngineRepair() {
		enginesJustRepaired = !enginesJustRepaired;
	}
	
	public void repairEngines() {
		if(enginesJustRepaired && !getEngineState()) {
			toggleEngines();
			toggleEngineRepair();
		}
	}
	
	public boolean getShieldState() {
		return shieldState;
	}
	
	public void toggleShields() {
		shieldState = !shieldState;
	}
	
	public boolean getShieldRepairStatus() {
		return shieldsJustRepaired;
	}
	
	public void toggleShieldRepair() {
		shieldsJustRepaired = !shieldsJustRepaired;
	}
	
	public void repairShields() {
		if(shieldsJustRepaired && !getShieldState()) {
			toggleShields();
			toggleShieldRepair();
		}
	}
	
	public int getEngineHealth() {
		return engineHealth;
	}
	
	public int getEngineMaxHealth() {
		return maxEngineHealth;
	}
 	
	public void setEngineHealth(int newPDHealth) {
		engineHealth = newPDHealth;
	}
	
	public void damageEngines(int damage) {
		engineHealth -= damage;
	}
	
	public void healEngines(int healAmount) {
		if(engineHealth + healAmount > maxEngineHealth) {
			engineHealth = maxEngineHealth;
		} else {
			engineHealth += healAmount;
		}
	}
	
	public int getShieldHealth() {
		return shieldHealth;
	}
	
	public int getShieldMaxHealth() {
		return maxShieldHealth;
	}
	
	public void setShieldHealth(int newPDHealth) {
		shieldHealth = newPDHealth;
	}
	
	public void damageShields(int damage) {
		shieldHealth -= damage;
	}
	
	public void healShields(int healAmount) {
		if(shieldHealth + healAmount > maxShieldHealth) {
			shieldHealth = maxShieldHealth;
		} else {
			shieldHealth += healAmount;
		}
	}
	
	public double[] copyArray(double[] original) {
		double[] duplicate = new double[original.length];
		for(int i = 0; i < original.length; i++) {
			duplicate[i] = original[i];
		}
		return duplicate;
	}
}
