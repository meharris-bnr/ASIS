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
import java.util.*;
import java.util.Map.Entry;

/**
 * @author David Tynan
 */
public class RandomSpawnsModule extends Framework_Module_Abstract {

    final Map<NPC_, FLST> spawnAssignmentsFLST = new HashMap<>();
    final Set<NPC_> validActors = new LinkedHashSet<>(0);
    private static final int MAX_SPAWNS_DEFAULT = 9;
    private static final int REDUCED_SPAWNS_DEFAULT = -1;
    private static final int INCREASED_SPAWNS_DEFAULT = -1;
    private static final String patchSettings = "PatchSettings";
    private static final String NPCInclusions = "NPCInclusions";
    private static final String NPCExclusions = "NPCExclusions";
    private static final String NPCExclusionsStartsWith = "NPCExclusionsStartsWith";
    private static final String KeywordExclusions = "KeywordExclusions";
    private static final String NPCMaxSpawnSettings = "NPCMaxSpawnSettings";
    private static final String NPCReducedSpawnSettings = "NPCReducedSpawnSettings";
    private static final String NPCIncreasedSpawnSettings = "NPCIncreasedSpawnSettings";
    private static final String NPCSpawnNumbers = "SpawnNumbers";
    private static final String NPCSpawnAssignments = "SpawnAssignments";
    private static final String CellExclusions = "CellExclusions";
    private static final String FactionExclusions = "FactionExclusions";
    private static final String modExclusions = "ModExclusions";
    private static final String thisIniName = "RandomSpawns.ini";

    private final Ini thisIni;

    public RandomSpawnsModule(Ini ini) throws IOException {
        name = "Random Spawns";
        thisIni = new Ini();
        Config c = thisIni.getConfig();
        c.setEmptyOption(true);
        c.setEmptySection(true);
        thisIni.load(new File(thisIniName));

        RecordSelector npcsToGetRandomSpawns = new RecordSelectorBuilder().type(GRUP_TYPE.NPC_).RecordValidator(ASIS.validNPCs_NoScripts).build();
        npcsToGetRandomSpawns.addInclusion(new SelectionSet(thisIni.get(NPCInclusions), new Matcher.EDIDContains()));
        npcsToGetRandomSpawns.addExclusion(new SelectionSet(thisIni.get(NPCExclusions), new Matcher.EDIDContains()));
        npcsToGetRandomSpawns.addExclusion(new SelectionSet(thisIni.get(NPCExclusionsStartsWith), new Matcher.EDIDStartsWith()));
        npcsToGetRandomSpawns.addExclusion(new SelectionSet(thisIni.get(modExclusions), new Matcher.ModContains()));
        npcsToGetRandomSpawns.addExclusion(new SelectionSet(ini.get("MODEXCLUSIONS"), new Matcher.ModContains()));
        npcsToGetRandomSpawns.addExclusion(new SelectionSet(thisIni.get(KeywordExclusions), new Matcher.HasKYWD()));
        this.addRecordSelector("npcsToGetRandomSpawns", npcsToGetRandomSpawns);
    }

    @Override
    public void runModuleChanges(ASISPatcherConfig asis) {
        try {
            Mod merger = Controller.getAllRecords();
            Mod patch = SPGlobal.getGlobalPatch();

            setupGlobalStuff(thisIni);

            float[] spawnweight = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            spawnweight[0] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN0WEIGHT));
            spawnweight[1] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN1WEIGHT));
            spawnweight[2] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN2WEIGHT));
            spawnweight[3] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN3WEIGHT));
            spawnweight[4] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN4WEIGHT));
            spawnweight[5] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN5WEIGHT));
            spawnweight[6] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN6WEIGHT));
            spawnweight[7] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN7WEIGHT));
            spawnweight[8] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN8WEIGHT));
            spawnweight[9] = Float.parseFloat(asis.saveFile().getStr(ASISSaveFile.GUISettings.ISSPAWN9WEIGHT));

            String scriptName = "randomizedSpawner";

            FormID[] rsSpawnWeight = new FormID[10];

            FLST listOfValidNPCs = new FLST("ASIS_ListOfValidNPCsRandomSpawns");
            FLST listOfInvalidCells = new FLST("ASIS_ListOfInvalidCellsRandomSpawns");
            FormID uniqueFaction = new FormID("000802", SPDatabase.getMod(new ModListing("ASIS-Dependency", false)).getInfo());
            FormID uniqueFaction2 = new FormID("000800", SPDatabase.getMod(new ModListing("ASIS-Dependency", false)).getInfo());

            GLOB rsRandomSpawnChanceGLOB = (GLOB) merger.getMajor("ASISRsRandomSpawnChance", GRUP_TYPE.GLOB);
            rsRandomSpawnChanceGLOB.setValue((float) asis.saveFile().getInt(ASISSaveFile.GUISettings.SRRANDOMSPAWNCHANCE));
            FormID rsRandomSpawnChance = rsRandomSpawnChanceGLOB.getForm();
            patch.addRecord(rsRandomSpawnChanceGLOB);

            GLOB rsMultiGroupChanceGLOB = (GLOB) merger.getMajor("ASISRsMultiGroupChance", GRUP_TYPE.GLOB);
            rsMultiGroupChanceGLOB.setValue((float) asis.saveFile().getInt(ASISSaveFile.GUISettings.SRMULTIGROUPCHANCE));
            FormID rsMultiGroupChance = rsMultiGroupChanceGLOB.getForm();
            patch.addRecord(rsMultiGroupChanceGLOB);

            GLOB rsUseInteriorSpawnsGLOB = (GLOB) merger.getMajor("ASISRsUseInteriorSpawns", GRUP_TYPE.GLOB);
            rsUseInteriorSpawnsGLOB.setValue(asis.saveFile().getBool(ASISSaveFile.GUISettings.SRUSEINTERIORSPAWNS));
            FormID rsUseInteriorSpawns = rsUseInteriorSpawnsGLOB.getForm();
            patch.addRecord(rsUseInteriorSpawnsGLOB);

            for (int i = 0; i < 10; i++) {
                GLOB spawnWeightGLOB = (GLOB) merger.getMajor("ASISRsSpawnWeight" + i, GRUP_TYPE.GLOB);
                spawnWeightGLOB.setValue(spawnweight[i]);
                rsSpawnWeight[i] = spawnWeightGLOB.getForm();
                patch.addRecord(spawnWeightGLOB);
            }

            SPProgressBarPlug.setStatus("Random Spawns: Adding Random Spawn Scripts");
            SPProgressBarPlug.incrementBar();

            ArrayList<ModListing> plugins = SPImporter.getActiveModList();
            Ini.Section cellExculsionsSection = thisIni.get(CellExclusions);
            for (Entry<String, String> e : cellExculsionsSection.entrySet()) {
                try {
                    ModListing master = new ModListing(e.getValue());
                    boolean active = false;
                    for (ModListing ml : plugins) {
                        if (ml.equals(master)) {
                            active = true;
                            break;
                        }
                    }
                    if (active) {
                        FormID f = new FormID(e.getKey(), e.getValue());
                        listOfInvalidCells.addFormEntry(f);
                    }
                } catch (Exception ex) {
                    String error = "Error reading RandomSpawns.ini [CellExclusions]."
                            + " Please make sure the ini file is up to date and correctly formated.";
                    SPGlobal.logError("ASIS Random Spawns", error);
                    SPGlobal.logError("ASIS Random Spawns", ex.getMessage());
                    JOptionPane.showMessageDialog(null, error);
                    System.exit(0);
                }
            }

            Ini.Section factionExculsionsSection = thisIni.get(FactionExclusions);
            Set<FormID> excludeFactionsSet = new HashSet<>();
            for (Entry<String, String> e : factionExculsionsSection.entrySet()) {
                try {
                    FACT fa = (FACT) merger.getMajor(e.getKey(), GRUP_TYPE.FACT);
                    FormID f = fa.getForm();
                    excludeFactionsSet.add(f);
                } catch (NullPointerException ex) {
                    String error = "Error reading RandomSpawns.ini [FactionExclusions], " + e.getKey()
                            + " could not be resolved to a faction. Ignoring.";
//                    JOptionPane.showMessageDialog(null, error);
                    SPGlobal.logError("Error - " + name, error);
                }
            }

            Map<String, Integer> maxSpawnSet = new HashMap<>();
            IniSectionToMap(thisIni, NPCMaxSpawnSettings, maxSpawnSet);

            Map<String, Integer> reducedSpawnSet = new HashMap<>();
            IniSectionToMap(thisIni, NPCReducedSpawnSettings, reducedSpawnSet);

            Map<String, Integer> increasedSpawnSet = new HashMap<>();
            IniSectionToMap(thisIni, NPCIncreasedSpawnSettings, increasedSpawnSet);

            FLST npcsWithRandomSpawns = new FLST("ASIS_npcsToGetRandomSpawns");

            for (NPC_ n : validActors) {
                npcsWithRandomSpawns.addFormEntry(n.getForm());

                String edidLower = n.getEDID().toLowerCase();

                int max = MAX_SPAWNS_DEFAULT;
                for (Entry<String, Integer> e : maxSpawnSet.entrySet()) {
                    if (edidLower.contains(e.getKey())) {
                        max = e.getValue();
                        break;
                    }
                }

                float reduced = REDUCED_SPAWNS_DEFAULT;
                for (Entry<String, Integer> e : reducedSpawnSet.entrySet()) {
                    if (edidLower.contains(e.getKey())) {
                        reduced = e.getValue();
                        break;
                    }
                }

                float increased = INCREASED_SPAWNS_DEFAULT;
                for (Entry<String, Integer> e : increasedSpawnSet.entrySet()) {
                    if (edidLower.contains(e.getKey())) {
                        increased = e.getValue();
                        break;
                    }
                }

                //Actual script addition is done here.
                ScriptRef script = new ScriptRef(scriptName);
                script.setProperty("listOfInvalidCells", listOfInvalidCells.getForm());
                script.setProperty("listOfNPCs", spawnAssignmentsFLST.get(n).getForm());
                script.setProperty("uniqueSpawnFaction", uniqueFaction);
                script.setProperty("uniqueSpawnFaction2", uniqueFaction2);
                script.setProperty("ASISRsUseInteriorSpawns", rsUseInteriorSpawns);
                script.setProperty("isAmbushSpawn", n.getEDID().toUpperCase().contains("AMBUSH"));
                script.setProperty("numMaxSpawns", max);
                script.setProperty("numReducedSpawns", reduced);
                script.setProperty("numIncreasedSpawns", increased);
                script.setProperty("ASISRsRandomSpawnChance", rsRandomSpawnChance);
                script.setProperty("ASISRsMultiGroupChance", rsMultiGroupChance);
                script.setProperty("numMaxGroups", asis.saveFile().getInt(ASISSaveFile.GUISettings.SRNUMMAXGROUPS));
                for (int i = 0; i < 10; i++) {
                    script.setProperty("ASISRsSpawnWeight" + i, rsSpawnWeight[i]);
                }
                script.setProperty("npcsToGetRandomSpawns", npcsWithRandomSpawns.getForm());
                script.setProperty("SelfForm", n.getForm());
                script.setProperty("modExcludeFactions", excludeFactionsSet.toArray(new FormID[0]));
                n.getScriptPackage().addScript(script);

                asis.npcsToWrite().add(n.getForm());
                patch.addRecord(n);

            }
            //Add the NPC to the patch.
            patch.addRecord(npcsWithRandomSpawns);
            patch.addRecord(listOfValidNPCs);
        } catch (IOException | NumberFormatException exception) {
            SPGlobal.logException(exception);
            JOptionPane.showMessageDialog(null, "Something terrible happened running " + name
                    + ": " + exception + " Check the readme and debug logs.");
            System.exit(0);
        }
    }

    private void setupGlobalStuff(Ini tempIni) {
        Map<Integer, LVLN> assignments = new HashMap<>();
        Map<Integer, String> assignmentsStr = new HashMap<>();
        Mod patch = SPGlobal.getGlobalPatch();
        Mod merger = Controller.getAllRecords();

        Ini.Section NPCSpawnNumbersSection = tempIni.get(NPCSpawnNumbers);
        for (Entry<String, String> e : NPCSpawnNumbersSection.entrySet()) {
            assignmentsStr.put(Integer.parseInt(e.getKey()), e.getValue());
        }

        for (Integer entry : assignmentsStr.keySet()) {
            LVLN li = new LVLN("ASIS_RandomSpawns_LVLN_" + entry);
            li.set(LeveledRecord.LVLFlag.CalcAllLevelsEqualOrBelowPC, true);
            li.set(LeveledRecord.LVLFlag.CalcForEachItemInCount, true);
            assignments.put(entry, li);
        }

        for (MajorRecord r : getRecordSelector("npcsToGetRandomSpawns").getValidRecords()) {
            NPC_ n = (NPC_) r;
            if (n != null) {
                String edid = n.getEDID().toUpperCase();
                for (Map.Entry<Integer, String> entry : assignmentsStr.entrySet()) {
                    try (Scanner s = new Scanner(entry.getValue())) {
                        s.useDelimiter(" ");
                        while (s.hasNext()) {
                            String val = s.next().toUpperCase();
                            if (edid.contains(val)) {
                                LVLN li = assignments.get(entry.getKey());
                                li.addEntry(n.getForm(), 1, 1);
                                patch.addRecord(li);
                            }
                        }
                    }
                }
            }
        }

        for (Entry<Integer, LVLN> entry : assignments.entrySet()) {
            String assignment = assignmentsStr.get(entry.getKey());
            try (Scanner s = new Scanner(assignment)) {
                s.useDelimiter(" ");
                while (s.hasNext()) {
                    String val = s.next();
                    MajorRecord rec = merger.getMajor(val);
                    if ((rec != null) && (!entry.getValue().contains(rec))) {
                        entry.getValue().addEntry(rec.getForm(), 1, 1);
                    }
                }
            }
        }

        Ini.Section NPCSpawnAssignmentsSection = tempIni.get(NPCSpawnAssignments);
        Map<String, String> spawnAssignments = new HashMap<>(NPCSpawnAssignmentsSection);

        ArrayList<RandomActor> randomActors = new ArrayList<>(0);

        for (MajorRecord r : getRecordSelector("npcsToGetRandomSpawns").getValidRecords()) {
            NPC_ n = (NPC_) r;
            if (n != null) {
                String edid = n.getEDID().toUpperCase();
                for (Map.Entry<String, String> entry : spawnAssignments.entrySet()) {
                    String key = entry.getKey().toUpperCase();
                    if (edid.contains(key)) {
                        ArrayList<Integer> value = new ArrayList<>();
                        try (Scanner s = new Scanner(entry.getValue())) {
                            s.useDelimiter(" ");
                            while (s.hasNext()) {
                                value.add(s.nextInt());
                            }
                        }
                        randomActors.add(new RandomActor(n, value));
                        validActors.add(n);
                    }
                }
            }
        }

        Map<Set<Integer>, FLST> m = new HashMap<>();

        for (RandomActor a : randomActors) {
            Set<Integer> s = new TreeSet<>(a.getGroupList());
            FLST temp = m.get(s);
            if (temp == null) {
                temp = new FLST("ASIS_RandomSpawns_" + a.getFormStr());
                m.put(s, temp);

                for (int i : s) {
                    temp.addFormEntry(assignments.get(i).getForm());
                    patch.addRecord(temp);
                }
            }

            spawnAssignmentsFLST.put(a.getNPC(), temp);
        }
    }

    private void IniSectionToMap(Ini theIni, String sectionName, Map<String, Integer> theMap) {
        Ini.Section iniSection = theIni.get(sectionName);
        for (Entry<String, String> e : iniSection.entrySet()) {
            try {
                theMap.put(e.getKey().toLowerCase(), Integer.parseInt(e.getValue()));
            } catch (NumberFormatException ex) {
                String error = "Error reading RandomSpawns.ini ["
                        + sectionName + "] entry: " + e.getKey()
                        + ", value after the = is not a number";
                SPGlobal.logError("ASIS Random Spawns", error);
            }
        }
    }
}
