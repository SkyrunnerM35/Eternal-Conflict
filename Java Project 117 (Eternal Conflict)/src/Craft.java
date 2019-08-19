
public abstract class Craft {
	
	private double hull;
	private double armor;
	private double shield;
	private double shieldRegen;
	private double[] armorResist;
	private double[] shieldResist;
	private double evasion;
	private double[] maxHealth;
	
	public Craft(double hullI, double armorI, double shieldI, double shieldRegenI, double[] armorResistI, double [] shieldResistI, double evasionI) {
		hull = hullI;
		armor = armorI;
		shield = shieldI;
		shieldRegen = shieldRegenI;
		armorResist = armorResistI;
		shieldResist = shieldResistI;
		evasion = evasionI;
		maxHealth = new double[] {hullI, armorI, shieldI};
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
	
	public void regenShield() {
		if(shield + shieldRegen > maxHealth[2]) {
			shield = maxHealth[2];
		} else {
			shield += shieldRegen;
		}
	}
	
	public double getShieldResist(int type) {
		return shieldResist[type];
	}
	
	public double getRegen() {
		return shieldRegen;
	}
	
	public void setRegen(double newRegen) {
		shieldRegen = newRegen;
	}
	
	public double getEvasion() {
		return evasion;
	}
	
	public void setEvasion(double newEvasion) {
		evasion = newEvasion;
	}
}
