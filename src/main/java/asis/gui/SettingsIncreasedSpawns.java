package asis.gui;

import asis.ASIS;
import asis.ASISSaveFile;
import lev.gui.LCheckBox;
import lev.gui.LDoubleSetting;
import skyproc.SkyProcSave;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;
import skyproc.gui.SUMGUI;

/**
 * @author pc tech
 */
public class SettingsIncreasedSpawns extends SPSettingPanel {

    LDoubleSetting spawn0weight;
    LDoubleSetting spawn1weight;
    LDoubleSetting spawn2weight;
    LDoubleSetting spawn3weight;
    LDoubleSetting spawn4weight;
    LDoubleSetting spawn5weight;
    LDoubleSetting spawn6weight;
    LDoubleSetting spawn7weight;
    LDoubleSetting spawn8weight;
    LDoubleSetting spawn9weight;
    LCheckBox useInterior;
    LDoubleSetting reducedInteriorSpawns;

    private final SkyProcSave saveFile;

    public SettingsIncreasedSpawns(SPMainMenuPanel parent_, SkyProcSave saveFile) {
        super(parent_, "Increased Spawns", ASIS.blue);
        this.saveFile = saveFile;
    }

    @Override
    protected void initialize() {
        super.initialize();

        spawn0weight = new LDoubleSetting("Weighting to have NO Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn0weight.tie(ASISSaveFile.GUISettings.ISSPAWN0WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn0weight);
        AddSetting(spawn0weight);

        spawn1weight = new LDoubleSetting("Spawn Weight for 1 Spawn", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn1weight.tie(ASISSaveFile.GUISettings.ISSPAWN1WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn1weight);
        AddSetting(spawn1weight);

        spawn2weight = new LDoubleSetting("Spawn Weight for 2 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn2weight.tie(ASISSaveFile.GUISettings.ISSPAWN2WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn2weight);
        AddSetting(spawn2weight);

        spawn3weight = new LDoubleSetting("Spawn Weight for 3 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn3weight.tie(ASISSaveFile.GUISettings.ISSPAWN3WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn3weight);
        AddSetting(spawn3weight);

        spawn4weight = new LDoubleSetting("Spawn Weight for 4 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn4weight.tie(ASISSaveFile.GUISettings.ISSPAWN4WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn4weight);
        AddSetting(spawn4weight);

        spawn5weight = new LDoubleSetting("Spawn Weight for 5 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn5weight.tie(ASISSaveFile.GUISettings.ISSPAWN5WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn5weight);
        AddSetting(spawn5weight);

        spawn6weight = new LDoubleSetting("Spawn Weight for 6 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn6weight.tie(ASISSaveFile.GUISettings.ISSPAWN6WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn6weight);
        AddSetting(spawn6weight);

        spawn7weight = new LDoubleSetting("Spawn Weight for 7 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn7weight.tie(ASISSaveFile.GUISettings.ISSPAWN7WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn7weight);
        AddSetting(spawn7weight);

        spawn8weight = new LDoubleSetting("Spawn Weight for 8 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn8weight.tie(ASISSaveFile.GUISettings.ISSPAWN8WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn8weight);
        AddSetting(spawn8weight);

        spawn9weight = new LDoubleSetting("Spawn Weight for 9 Spawns", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 1);
        spawn9weight.tie(ASISSaveFile.GUISettings.ISSPAWN9WEIGHT, saveFile, SUMGUI.helpPanel, true);
        setPlacement(spawn9weight);
        AddSetting(spawn9weight);

        useInterior = new LCheckBox("Use Interior Spawns", ASIS.settingsFontSmall, ASIS.blue);
        useInterior.tie(ASISSaveFile.GUISettings.ISUSEINTERIORSPAWNS, saveFile, SUMGUI.helpPanel, true);
        useInterior.setOffset(2);
        useInterior.addShadow();
        setPlacement(useInterior);
        AddSetting(useInterior);

        reducedInteriorSpawns = new LDoubleSetting("Reduce Interior Spawn Chance", ASIS.settingsFontSmall, ASIS.blue,
                0, 1000, 2);
        reducedInteriorSpawns.tie(ASISSaveFile.GUISettings.ISREDUCEDINTERIORSPAWNS, saveFile, SUMGUI.helpPanel, true);
        setPlacement(reducedInteriorSpawns);
        AddSetting(reducedInteriorSpawns);

        alignRight();
    }
}
