package asis;

import ASIS_Controller.*;
import org.ini4j.Config;
import org.ini4j.Ini;
import skyproc.*;
import skyproc.gui.SPProgressBarPlug;
import skyprocstarter.ASISPatcherConfig;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ASISnpcPotionsModule extends Framework_Module_Abstract {

    private static final String patchSettings = "PatchSettings";
    private static final String NPCInclusions = "NPCInclusions";
    private static final String NPCExclusions = "NPCExclusions";
    private static final String modExclusions = "ModExclusions";
    private static final String hpPotions = "HP_Potions";
    private static final String mpPotions = "MP_Potions";
    private static final String spPotions = "SP_Potions";

    private static final String thisIniName = "NPCPotions.ini";

    private final Ini thisIni;

    public ASISnpcPotionsModule(Ini ini) throws IOException {
        name = "NPC Potions";
        thisIni = new Ini();
        Config c = thisIni.getConfig();
        c.setEmptyOption(true);
        c.setEmptySection(true);
        thisIni.load(new File(thisIniName));

        RecordSelector npcsToGetPotions = new RecordSelectorBuilder().type(GRUP_TYPE.NPC_).RecordValidator(ASIS.validNPCs_NoScripts).build();
        npcsToGetPotions.addInclusion(new SelectionSet(thisIni.get(NPCInclusions), new Matcher.EDIDContains()));
        npcsToGetPotions.addExclusion(new SelectionSet(thisIni.get(NPCExclusions), new Matcher.EDIDContains()));
        npcsToGetPotions.addExclusion(new SelectionSet(thisIni.get(modExclusions), new Matcher.ModContains()));
        npcsToGetPotions.addExclusion(new SelectionSet(ini.get("MODEXCLUSIONS"), new Matcher.ModContains()));
        this.addRecordSelector("npcsToGetPotions", npcsToGetPotions);
    }

    @Override
    public void runModuleChanges(ASISPatcherConfig asis) {
        Mod merger = Controller.getAllRecords();
        Mod patch = SPGlobal.getGlobalPatch();

        String scriptName = "NPCPotions";

        FormID hpotions = null;
        Ini.Section hpPotionSection = thisIni.get(hpPotions);
        for (String e : hpPotionSection.keySet()) {
//            FormID tempHPForm = new FormID(e.getKey(), e.getValue());
            LVLI hpPotionsList = (LVLI) merger.getMajor(e, GRUP_TYPE.LVLI);
            if (hpPotionsList != null) {
                hpotions = hpPotionsList.getForm();
            }
        }
//        FormID hpotions = merger.getMajor("LItemPotionRestoreHealth", GRUP_TYPE.LVLI).getForm();
        FormID mpotions = null;
        Ini.Section mpPotionSection = thisIni.get(mpPotions);
        for (String e : mpPotionSection.keySet()) {
//            FormID tempMPForm = new FormID(e.getKey(), e.getValue());
            LVLI mpPotionsList = (LVLI) merger.getMajor(e, GRUP_TYPE.LVLI);
            if (mpPotionsList != null) {
                mpotions = mpPotionsList.getForm();
            }
        }
//        FormID mpotions = merger.getMajor("LItemPotionRestoreMagicka", GRUP_TYPE.LVLI).getForm();
        FormID spotions = null;
        Ini.Section spPotionSection = thisIni.get(spPotions);
        for (String e : spPotionSection.keySet()) {
//            FormID tempSPForm = new FormID(e.getKey(), e.getValue());
            LVLI spPotionsList = (LVLI) merger.getMajor(e, GRUP_TYPE.LVLI);
            if (spPotionsList != null) {
                spotions = spPotionsList.getForm();
            }
        }
//        FormID spotions = merger.getMajor("LItemPotionRestoreStamina", GRUP_TYPE.LVLI).getForm();
        if (hpotions == null || mpotions == null || spotions == null) {
            String error = "The following leveled list(s) for potions was not found: ";
            if (hpotions == null) {
                error = error + " Health Potions";
            }
            if (mpotions == null) {
                error = error + " Magicka Potions";
            }
            if (spotions == null) {
                error = error + " Stamina Potions";
            }
            SPGlobal.logError("NPC Potions Error", error);
            JOptionPane.showMessageDialog(null, "Something terrible happened running " + name
                    + ": " + error);
            System.exit(0);
        }

        int numItems = asis.saveFile().getInt(ASISSaveFile.GUISettings.NUMBER_OF_POTIONS);
        int numChance = asis.saveFile().getInt(ASISSaveFile.GUISettings.CHANCE_PER_POTION);

        GLOB NPCPotionsnumItemsGLOB = (GLOB) merger.getMajor("ASISNPCPotionsnumItems", GRUP_TYPE.GLOB);
        NPCPotionsnumItemsGLOB.setValue((float) numItems);
        SPGlobal.getGlobalPatch().addRecord(NPCPotionsnumItemsGLOB);
        FormID NPCPotionsnumItems = NPCPotionsnumItemsGLOB.getForm();
        GLOB NPCPotionsnumChanceGLOB = (GLOB) merger.getMajor("ASISNPCPotionsnumChance", GRUP_TYPE.GLOB);
        NPCPotionsnumChanceGLOB.setValue((float) numChance);
        SPGlobal.getGlobalPatch().addRecord(NPCPotionsnumChanceGLOB);
        FormID NPCPotionsnumChance = NPCPotionsnumChanceGLOB.getForm();

        SPProgressBarPlug.setStatus("NPC Potions: Adding Potions");
        SPProgressBarPlug.incrementBar();
        for (MajorRecord r : getRecordSelector("npcsToGetPotions").getValidRecords()) {
            NPC_ n = (NPC_) r;
            if (n != null) {
                ScriptRef script = new ScriptRef(scriptName);
                script.setProperty("useNPCPotions", true);
                script.setProperty("ASISNPCPotionsnumItems", NPCPotionsnumItems);
                script.setProperty("ASISNPCPotionsnumChance", NPCPotionsnumChance);
                script.setProperty("potionsList1", hpotions);
                if (n.getEDID().toUpperCase().contains("MISSILE") || n.getEDID().toUpperCase().contains("MELEE")) {
                    script.setProperty("potionsList2", spotions);
                } else {
                    script.setProperty("potionsList2", mpotions);
                }
                n.getScriptPackage().addScript(script);

                asis.npcsToWrite().add(n.getForm());
                patch.addRecord(n);
            }
        }
    }
}
