package com.chipotlebanditos.spacebanditos.model.systems;

public abstract class SystemUpgradeSequence {
    
    private final int[] upgradeCosts;
    
    public SystemUpgradeSequence(int... upgradeCosts) {
        this.upgradeCosts = upgradeCosts;
    }
    
    public int getMaxUpgradeLevel() {
        return upgradeCosts.length;
    }
    
    public int getUpgradeCost(int level) {
        return upgradeCosts[level - 1];
    }
    
    public abstract String getUpgradeDescription(int level);
}
