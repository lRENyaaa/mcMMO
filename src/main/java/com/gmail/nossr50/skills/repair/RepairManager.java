package com.gmail.nossr50.skills.repair;

import com.gmail.nossr50.datatypes.interactions.NotificationType;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.datatypes.skills.SubSkillType;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.skills.SkillManager;
import com.gmail.nossr50.util.random.RandomChanceSkillStatic;
import com.gmail.nossr50.util.skills.SkillActivationType;
import com.gmail.nossr50.util.sounds.SoundType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Map.Entry;

public class RepairManager extends SkillManager {
    private boolean placedAnvil;
    private int lastClick;

    public RepairManager(mcMMO pluginRef,  McMMOPlayer mcMMOPlayer) {
        super(pluginRef, mcMMOPlayer, PrimarySkillType.REPAIR);
    }

    /**
     * Handles notifications for placing an anvil.
     */
    public void placedAnvilCheck() {
        Player player = getPlayer();

        if (getPlacedAnvil()) {
            return;
        }

        if (pluginRef.getConfigManager().getConfigRepair().getRepairGeneral().isAnvilMessages()) {
            pluginRef.getNotificationManager().sendPlayerInformation(player, NotificationType.SUBSKILL_MESSAGE, "Repair.Listener.Anvil");
        }

        if (pluginRef.getConfigManager().getConfigRepair().getRepairGeneral().isAnvilPlacedSounds()) {
            pluginRef.getSoundManager().sendSound(player, player.getLocation(), SoundType.ANVIL);
        }

        togglePlacedAnvil();
    }


    public void handleRepair(ItemStack item) {
//        Player player = getPlayer();
//        Repairable repairable = mcMMO.getRepairableManager().getRepairable(item.getType());
//
//        if(item.getItemMeta() != null && item.getItemMeta().isUnbreakable()) {
//            mcMMO.getNotificationManager().sendPlayerInformation(player, NotificationType.SUBSKILL_MESSAGE_FAILED, "Anvil.Unbreakable");
//            return;
//        }
//
//        // Permissions checks on material and item types
////        if (!Permissions.repairMaterialType(player, repairable.getRepairItemMaterialCategory())) {
////            mcMMO.getNotificationManager().sendPlayerInformation(player, NotificationType.NO_PERMISSION, "mcMMO.NoPermission");
////            return;
////        }
////
////        if (!Permissions.repairItemType(player, repairable.getRepairItemType())) {
////            mcMMO.getNotificationManager().sendPlayerInformation(player, NotificationType.NO_PERMISSION, "mcMMO.NoPermission");
////            return;
////        }
//
//        int skillLevel = getSkillLevel();
//        int minimumRepairableLevel = repairable.getMinimumLevel();
//
//        // Level check
//        if (skillLevel < minimumRepairableLevel) {
//            mcMMO.getNotificationManager().sendPlayerInformation(player, NotificationType.SUBSKILL_MESSAGE_FAILED, "Repair.Skills.Adept", String.valueOf(minimumRepairableLevel), StringUtils.getPrettyItemString(item.getType()));
//            return;
//        }
//
//        PlayerInventory inventory = player.getInventory();
//        Material repairMaterial = null;
//        boolean foundNonBasicMaterial = false;
//
//        //Find the first compatible repair material
//        for (Material repairMaterialCandidate : repairable.getRepairMaterials()) {
//            for (ItemStack is : player.getInventory().getContents()) {
//                if(is == null)
//                    continue; //Ignore IntelliJ this can be null
//
//                //Match to repair material
//                if (is.getType() == repairMaterialCandidate) {
//                    //Check for item meta
//                    if(is.getItemMeta() != null) {
//                        //Check for lore
//                        if(is.getItemMeta().getLore() != null) {
//                            if(is.getItemMeta().getLore().isEmpty()) {
//                                //Lore is empty so this item is fine
//                                repairMaterial = repairMaterialCandidate;
//                                break;
//                            } else {
//                                foundNonBasicMaterial = true;
//                            }
//                        } else {
//                            //No lore so this item is fine
//                            repairMaterial = repairMaterialCandidate;
//                            break;
//                        }
//                    } else {
//                        //No Item Meta so this item is fine
//                        repairMaterial = repairMaterialCandidate;
//                        break;
//                    }
//                }
//            }
//        }
//
//        /* Abort the repair if no compatible basic repairing item found */
//        if (repairMaterial == null && foundNonBasicMaterial == true) {
//            player.sendMessage(pluginRef.getLocaleManager().getString("Repair.NoBasicRepairMatsFound"));
//            return;
//        }
//
//        ItemStack toRemove = new ItemStack(repairMaterial);
//        toRemove.setAmount(1);
//
//        short startDurability = item.getDurability();
//
//        // Do not repair if at full durability
//        if (startDurability <= 0) {
//            mcMMO.getNotificationManager().sendPlayerInformation(player, NotificationType.SUBSKILL_MESSAGE_FAILED, "Repair.Skills.FullDurability");
//            return;
//        }
//
//        // Clear ability buffs before trying to repair.
//        SkillUtils.removeAbilityBuff(item);
//
//        // Lets get down to business,
//        // To defeat, the huns.
//        int baseRepairAmount = repairable.getBaseRepairDurability(); // Did they send me daughters?
//        short newDurability = repairCalculate(startDurability, baseRepairAmount); // When I asked for sons?
//
//        // Call event
//        if (EventUtils.callRepairCheckEvent(player, (short) (startDurability - newDurability), toRemove, item).isCancelled()) {
//            return;
//        }
//
//        // Handle the enchants
//        if (mcMMO.getConfigManager().getConfigRepair().getArcaneForging().isMayLoseEnchants() && !Permissions.hasRepairEnchantBypassPerk(player)) {
//            addEnchants(item);
//        }
//
//        // Remove the item
//        inventory.removeItem(toRemove);
//
//        // Give out XP like candy
//        applyXpGain(((getPercentageRepaired(startDurability, newDurability, repairable.getMaximumDurability())
//                * repairable.getXpMultiplier())
//                * mcMMO.getConfigManager().getConfigExperience().getRepairXPBase())
//                * mcMMO.getConfigManager().getConfigExperience().getExperienceRepair().getItemMaterialXPMultiplier(repairable.getRepairItemMaterialCategory()), XPGainReason.PVE);
//
//        // BWONG BWONG BWONG
//        if (mcMMO.getConfigManager().getConfigRepair().getRepairGeneral().isAnvilUseSounds()) {
//            pluginRef.getSoundManager().sendSound(player, player.getLocation(), SoundType.ANVIL);
//            pluginRef.getSoundManager().sendSound(player, player.getLocation(), SoundType.ITEM_BREAK);
//        }
//
//        // Repair the item!
//        item.setDurability(newDurability);
    }

    private double getPercentageRepaired(short startDurability, short newDurability, short totalDurability) {
        return ((startDurability - newDurability) / (float) totalDurability);
    }

    /**
     * Check if the player has tried to use an Anvil before.
     *
     * @return true if the player has confirmed using an Anvil
     */
    public boolean checkConfirmation(boolean actualize) {
        Player player = getPlayer();
        long lastUse = getLastAnvilUse();

        if (!pluginRef.getSkillTools().cooldownExpired(lastUse, 3) || !pluginRef.getConfigManager().getConfigRepair().getRepairGeneral().isEnchantedItemsRequireConfirm()) {
            return true;
        }

        if (!actualize) {
            return false;
        }

        actualizeLastAnvilUse();
        pluginRef.getNotificationManager().sendPlayerInformation(player, NotificationType.SUBSKILL_MESSAGE, "Skills.ConfirmOrCancel", pluginRef.getLocaleManager().getString("Repair.Pretty.Name"));

        return false;
    }

    /**
     * Gets the Arcane Forging rank
     *
     * @return the current Arcane Forging rank
     */
    public int getArcaneForgingRank() {
        return pluginRef.getRankTools().getRank(getPlayer(), SubSkillType.REPAIR_ARCANE_FORGING);
    }

    /**
     * Gets chance of keeping enchantment during repair.
     *
     * @return The chance of keeping the enchantment
     */
    public double getKeepEnchantChance() {
        return pluginRef.getConfigManager().getConfigRepair().getArcaneForging().getKeepEnchantChanceMap().get(getArcaneForgingRank());
    }

    /**
     * Gets chance of enchantment being downgraded during repair.
     *
     * @return The chance of the enchantment being downgraded
     */
    public double getDowngradeEnchantChance() {
        return pluginRef.getConfigManager().getConfigRepair().getArcaneForging().getDowngradeChanceMap().get(getArcaneForgingRank());
    }

    /**
     * Computes repair bonuses.
     *
     * @param durability   The durability of the item being repaired
     * @param repairAmount The base amount of durability repaired to the item
     * @return The final amount of durability repaired to the item
     */
    private short repairCalculate(short durability, int repairAmount) {
        Player player = getPlayer();

        if (pluginRef.getPermissionTools().isSubSkillEnabled(player, SubSkillType.REPAIR_REPAIR_MASTERY)
                && pluginRef.getRankTools().hasUnlockedSubskill(getPlayer(), SubSkillType.REPAIR_REPAIR_MASTERY)) {

            double maxBonusCalc = pluginRef.getDynamicSettingsManager().getSkillPropertiesManager().getMaxBonus(SubSkillType.REPAIR_REPAIR_MASTERY) / 100.0D;
            double skillLevelBonusCalc = (maxBonusCalc / pluginRef.getDynamicSettingsManager().getSkillPropertiesManager().getMaxBonusLevel(SubSkillType.REPAIR_REPAIR_MASTERY)) * (getSkillLevel() / 100.0D);
            double bonus = repairAmount * Math.min(skillLevelBonusCalc, maxBonusCalc);

            repairAmount += bonus;
        }

        if (pluginRef.getPermissionTools().isSubSkillEnabled(player, SubSkillType.REPAIR_SUPER_REPAIR) && checkPlayerProcRepair()) {
            repairAmount *= 2.0D;
        }

        if (repairAmount <= 0 || repairAmount > Short.MAX_VALUE) {
            repairAmount = Short.MAX_VALUE;
        }

        return (short) Math.max(durability - repairAmount, 0);
    }

    /**
     * Checks for Super Repair bonus.
     *
     * @return true if bonus granted, false otherwise
     */
    private boolean checkPlayerProcRepair() {
        if (!pluginRef.getRankTools().hasUnlockedSubskill(getPlayer(), SubSkillType.REPAIR_SUPER_REPAIR))
            return false;

        if (pluginRef.getRandomChanceTools().isActivationSuccessful(SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP, SubSkillType.REPAIR_SUPER_REPAIR, getPlayer())) {
            pluginRef.getNotificationManager().sendPlayerInformation(getPlayer(), NotificationType.SUBSKILL_MESSAGE, "Repair.Skills.FeltEasy");
            return true;
        }

        return false;
    }

    /**
     * Handles removing & downgrading enchants.
     *
     * @param item Item being repaired
     */
    private void addEnchants(ItemStack item) {
        Player player = getPlayer();

        Map<Enchantment, Integer> enchants = item.getEnchantments();

        if (enchants.isEmpty()) {
            return;
        }

        if (pluginRef.getPermissionTools().arcaneBypass(player)) {
            pluginRef.getNotificationManager().sendPlayerInformation(getPlayer(), NotificationType.SUBSKILL_MESSAGE, "Repair.Arcane.Perfect");
            return;
        }

        if (getArcaneForgingRank() == 0 || !pluginRef.getPermissionTools().isSubSkillEnabled(player, SubSkillType.REPAIR_ARCANE_FORGING)) {
            for (Enchantment enchant : enchants.keySet()) {
                item.removeEnchantment(enchant);
            }

            pluginRef.getNotificationManager().sendPlayerInformation(getPlayer(), NotificationType.SUBSKILL_MESSAGE_FAILED, "Repair.Arcane.Lost");
            return;
        }

        boolean downgraded = false;

        for (Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            int enchantLevel = enchant.getValue();

            if(!pluginRef.getConfigManager().getConfigExploitPrevention().getConfigSectionExploitRepair().isAllowUnsafeEnchants()) {
                if(enchantLevel > enchant.getKey().getMaxLevel()) {
                    enchantLevel = enchant.getKey().getMaxLevel();

                    item.addEnchantment(enchant.getKey(), enchantLevel);
                }
            }

            Enchantment enchantment = enchant.getKey();

            if (pluginRef.getRandomChanceTools().checkRandomChanceExecutionSuccess(new RandomChanceSkillStatic(pluginRef, getKeepEnchantChance(), getPlayer(), SubSkillType.REPAIR_ARCANE_FORGING))) {

                if (pluginRef.getConfigManager().getConfigRepair().getArcaneForging().isDowngradesEnabled() && enchantLevel > 1
                        && (!pluginRef.getRandomChanceTools().checkRandomChanceExecutionSuccess(new RandomChanceSkillStatic(pluginRef,100 - getDowngradeEnchantChance(), getPlayer(), SubSkillType.REPAIR_ARCANE_FORGING)))) {
                    item.addUnsafeEnchantment(enchantment, enchantLevel - 1);
                    downgraded = true;
                }
            } else {
                item.removeEnchantment(enchantment);
            }
        }

        Map<Enchantment, Integer> newEnchants = item.getEnchantments();

        if (newEnchants.isEmpty()) {
            pluginRef.getNotificationManager().sendPlayerInformationChatOnly(getPlayer(),  "Repair.Arcane.Fail");
        }
        else if (downgraded || newEnchants.size() < enchants.size()) {
            pluginRef.getNotificationManager().sendPlayerInformationChatOnly(getPlayer(),  "Repair.Arcane.Downgrade");
        }
        else {
            pluginRef.getNotificationManager().sendPlayerInformationChatOnly(getPlayer(),  "Repair.Arcane.Perfect");
        }
    }

    /*
     * Repair Anvil Placement
     */

    public boolean getPlacedAnvil() {
        return placedAnvil;
    }

    public void togglePlacedAnvil() {
        placedAnvil = !placedAnvil;
    }

    /*
     * Repair Anvil Usage
     */

    public int getLastAnvilUse() {
        return lastClick;
    }

    public void setLastAnvilUse(int value) {
        lastClick = value;
    }

    public void actualizeLastAnvilUse() {
        lastClick = (int) (System.currentTimeMillis() / pluginRef.getMiscTools().TIME_CONVERSION_FACTOR);
    }
}