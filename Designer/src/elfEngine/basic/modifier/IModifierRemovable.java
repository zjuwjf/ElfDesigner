package elfEngine.basic.modifier;

public interface IModifierRemovable {
	/**
	 * @return
	 * is staying in modifierList or going away ,when died.
	 */
	public boolean isRemovable();
	public void setRemovable(boolean isRemovable);
}
