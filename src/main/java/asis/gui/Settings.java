package asis.gui;

import asis.ASIS;
import asis.ASISSaveFile;
import lev.gui.LCheckBox;
import skyproc.SkyProcSave;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;
import skyproc.gui.SUMGUI;

/**
 * Possibly remove this; I can't find any references in the original code (ESJ).
 */
public class Settings extends SPSettingPanel {

    LCheckBox increasedSpawns;
    LCheckBox npcPotions;
    LCheckBox automaticPerks;
    LCheckBox automaticSpells;
    LCheckBox spawnRandomizer;
    LCheckBox customizedAI;
    LCheckBox customizedGMSTs;
    LCheckBox npcEnchantFix;

    private final SkyProcSave save;

    public Settings(SPMainMenuPanel parent_, SkyProcSave save) {
        super(parent_, "Settings", ASIS.blue);
        this.save = save;
    }

    @Override
    protected void initialize() {
        super.initialize();

        increasedSpawns = new LCheckBox("Increased Spawns", ASIS.settingsFont, ASIS.blue);
        increasedSpawns.tie(ASISSaveFile.GUISettings.INCREASEDSPAWNS_ON, save, SUMGUI.helpPanel, true);
        increasedSpawns.setOffset(2);
        increasedSpawns.addShadow();
        setPlacement(increasedSpawns);
        AddSetting(increasedSpawns);

        npcPotions = new LCheckBox("NPC Potions", ASIS.settingsFont, ASIS.blue);
        npcPotions.tie(ASISSaveFile.GUISettings.NPCPOTIONS_ON, save, SUMGUI.helpPanel, true);
        npcPotions.setOffset(2);
        npcPotions.addShadow();
        setPlacement(npcPotions);
        AddSetting(npcPotions);

        automaticPerks = new LCheckBox("Automatic Perks", ASIS.settingsFont, ASIS.blue);
        automaticPerks.tie(ASISSaveFile.GUISettings.AUTOMATICPERKS_ON, save, SUMGUI.helpPanel, true);
        automaticPerks.setOffset(2);
        automaticPerks.addShadow();
        setPlacement(automaticPerks);
        AddSetting(automaticPerks);

        automaticSpells = new LCheckBox("Automatic Spells", ASIS.settingsFont, ASIS.blue);
        automaticSpells.tie(ASISSaveFile.GUISettings.AUTOMATICSPELLS_ON, save, SUMGUI.helpPanel, true);
        automaticSpells.setOffset(2);
        automaticSpells.addShadow();
        setPlacement(automaticSpells);
        AddSetting(automaticSpells);

        customizedAI = new LCheckBox("Customized AI", ASIS.settingsFont, ASIS.blue);
        customizedAI.tie(ASISSaveFile.GUISettings.CUSTOMIZEDAI_ON, save, SUMGUI.helpPanel, true);
        customizedAI.setOffset(2);
        customizedAI.addShadow();
        setPlacement(customizedAI);
        AddSetting(customizedAI);

        customizedGMSTs = new LCheckBox("Customized GMST's", ASIS.settingsFont, ASIS.blue);
        customizedGMSTs.tie(ASISSaveFile.GUISettings.CUSTOMIZEDGMSTS_ON, save, SUMGUI.helpPanel, true);
        customizedGMSTs.setOffset(2);
        customizedGMSTs.addShadow();
        setPlacement(customizedGMSTs);
        AddSetting(customizedGMSTs);

        spawnRandomizer = new LCheckBox("Spawn Randomizer", ASIS.settingsFont, ASIS.blue);
        spawnRandomizer.tie(ASISSaveFile.GUISettings.SPAWNRANDOMIZER_ON, save, SUMGUI.helpPanel, true);
        spawnRandomizer.setOffset(2);
        spawnRandomizer.addShadow();
        setPlacement(spawnRandomizer);
        AddSetting(spawnRandomizer);

        npcEnchantFix = new LCheckBox("Skip BOSS", ASIS.settingsFont, ASIS.blue);
        npcEnchantFix.tie(ASISSaveFile.GUISettings.NPCENCHANTFIX_ON, save, SUMGUI.helpPanel, true);
        spawnRandomizer.setOffset(2);
        spawnRandomizer.addShadow();
        setPlacement(npcEnchantFix);
        AddSetting(npcEnchantFix);

        alignRight();
    }
}
