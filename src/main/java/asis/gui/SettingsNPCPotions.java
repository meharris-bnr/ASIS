package asis.gui;

import asis.ASIS;
import asis.ASISSaveFile;
import lev.gui.LNumericSetting;
import skyproc.SkyProcSave;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;
import skyproc.gui.SUMGUI;

/**
 * @author pc tech
 */
public class SettingsNPCPotions extends SPSettingPanel {

    LNumericSetting numPotions;
    LNumericSetting numChance;

    private final SkyProcSave save;

    public SettingsNPCPotions(SPMainMenuPanel parent_, SkyProcSave save) {
        super(parent_, "NPC Potions", ASIS.blue);
        this.save = save;
    }

    @Override
    protected void initialize() {
        super.initialize();


        numPotions = new LNumericSetting("Number of Potions", ASIS.settingsFont, ASIS.blue,
                0, 100, 1);
        numPotions.tie(ASISSaveFile.GUISettings.NUMBER_OF_POTIONS, save, SUMGUI.helpPanel, true);
        setPlacement(numPotions);
        AddSetting(numPotions);

        numChance = new LNumericSetting("Chance Per Potion", ASIS.settingsFont, ASIS.blue,
                0, 100, 1);
        numChance.tie(ASISSaveFile.GUISettings.CHANCE_PER_POTION, save, SUMGUI.helpPanel, true);
        setPlacement(numChance);
        AddSetting(numChance);

        alignRight();
    }
}
