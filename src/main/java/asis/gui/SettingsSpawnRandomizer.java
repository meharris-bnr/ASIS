package asis.gui;

import asis.ASIS;
import asis.ASISSaveFile;
import lev.gui.LCheckBox;
import lev.gui.LNumericSetting;
import skyproc.SkyProcSave;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;
import skyproc.gui.SUMGUI;

/**
 * @author pc tech
 */
public class SettingsSpawnRandomizer extends SPSettingPanel {

    LNumericSetting spawn0weight;
    LNumericSetting spawn1weight;
    LNumericSetting spawn2weight;
    LNumericSetting spawn3weight;
    LNumericSetting spawn4weight;
    LNumericSetting spawn5weight;
    LNumericSetting spawn6weight;
    LNumericSetting spawn7weight;
    LNumericSetting spawn8weight;
    LNumericSetting spawn9weight;
    LCheckBox useInteriorSpawns;
    LNumericSetting multiGroupChance;
    LNumericSetting firstGroupChance;

    private final SkyProcSave save;

    public SettingsSpawnRandomizer(SPMainMenuPanel parent_, SkyProcSave save) {
        super(parent_, "Spawn Randomizer", ASIS.blue);
        this.save = save;
    }

    @Override
    protected void initialize() {
        super.initialize();


        spawn0weight = new LNumericSetting("Weighting to have NO Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn0weight.tie(ASISSaveFile.GUISettings.SRSPAWN0WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn0weight);
        AddSetting(spawn0weight);

        spawn1weight = new LNumericSetting("Spawn Weight for 1 Spawn", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn1weight.tie(ASISSaveFile.GUISettings.SRSPAWN1WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn1weight);
        AddSetting(spawn1weight);

        spawn2weight = new LNumericSetting("Spawn Weight for 2 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn2weight.tie(ASISSaveFile.GUISettings.SRSPAWN2WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn2weight);
        AddSetting(spawn2weight);

        spawn3weight = new LNumericSetting("Spawn Weight for 3 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn3weight.tie(ASISSaveFile.GUISettings.SRSPAWN3WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn3weight);
        AddSetting(spawn3weight);

        spawn4weight = new LNumericSetting("Spawn Weight for 4 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn4weight.tie(ASISSaveFile.GUISettings.SRSPAWN4WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn4weight);
        AddSetting(spawn4weight);

        spawn5weight = new LNumericSetting("Spawn Weight for 5 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn5weight.tie(ASISSaveFile.GUISettings.SRSPAWN5WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn5weight);
        AddSetting(spawn5weight);

        spawn6weight = new LNumericSetting("Spawn Weight for 6 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn6weight.tie(ASISSaveFile.GUISettings.SRSPAWN6WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn6weight);
        AddSetting(spawn6weight);

        spawn7weight = new LNumericSetting("Spawn Weight for 7 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn7weight.tie(ASISSaveFile.GUISettings.SRSPAWN7WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn7weight);
        AddSetting(spawn7weight);

        spawn8weight = new LNumericSetting("Spawn Weight for 8 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn8weight.tie(ASISSaveFile.GUISettings.SRSPAWN8WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn8weight);
        AddSetting(spawn8weight);

        spawn9weight = new LNumericSetting("Spawn Weight for 9 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn9weight.tie(ASISSaveFile.GUISettings.SRSPAWN9WEIGHT, save, SUMGUI.helpPanel, true);
        setPlacement(spawn9weight);
        AddSetting(spawn9weight);

        useInteriorSpawns = new LCheckBox("Use Interior Spawns", ASIS.settingsFontSmall, ASIS.blue);
        useInteriorSpawns.tie(ASISSaveFile.GUISettings.SRUSEINTERIORSPAWNS, save, SUMGUI.helpPanel, true);
        useInteriorSpawns.setOffset(2);
        useInteriorSpawns.addShadow();
        setPlacement(useInteriorSpawns);
        AddSetting(useInteriorSpawns);

        firstGroupChance = new LNumericSetting("Chance for Random Spawns", ASIS.settingsFont, ASIS.blue,
                0, 1000, 1);
        firstGroupChance.tie(ASISSaveFile.GUISettings.SRRANDOMSPAWNCHANCE, save, SUMGUI.helpPanel, true);
        setPlacement(firstGroupChance);
        AddSetting(firstGroupChance);

        spawn9weight = new LNumericSetting("Chance for Multiple Groups", ASIS.settingsFont, ASIS.blue,
                0, 1000, 1);
        spawn9weight.tie(ASISSaveFile.GUISettings.SRMULTIGROUPCHANCE, save, SUMGUI.helpPanel, true);
        setPlacement(spawn9weight);
        AddSetting(spawn9weight);

        alignRight();
    }
}
