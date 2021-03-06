/**
 * @author Michael Yang
 * @since   7/28/2019
 */

public class Drone extends Craft {

	public Drone(double hullI, double armorI, double shieldI, double shieldRegenI, double[] armorResistI, double [] shieldResistI, double evasionI, int engineHealthI, int shieldHealthI) {
		super(hullI, armorI, shieldI, shieldRegenI, armorResistI, shieldResistI, evasionI, engineHealthI, shieldHealthI);
	}
	
	public String toString() {
		return String.format("[%3.2f/%3.2f]", getArmor(), getHull());
	}
}
