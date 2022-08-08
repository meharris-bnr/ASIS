package asis;

import ASIS_Controller.*;
import org.ini4j.Config;
import org.ini4j.Ini;
import skyproc.*;
import skyproc.Condition.CondFlag;
import skyproc.Condition.Operator;
import skyproc.Condition.P_Int;
import skyproc.genenums.ActorValue;
import skyproc.genenums.Skill;
import skyproc.gui.SPProgressBarPlug;
import skyprocstarter.ASISPatcherConfig;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author David Tynan
 */
public class AutomaticPerksModule extends Framework_Module_Abstract {

    private final String iniName = "AutomaticPerks.ini";

    private static final String patchSettings = "PatchSettings";
    private static final String NPCInclusions = "NPCInclusions";
    private static final String NPCExclusions = "NPCExclusions";
    private static final String KeywordExclusions = "NPCKeywordExclusions";
    private static final String NPCModExclusions = "NPCModExclusions";

    private static final String PerkInclusions = "PerkInclusions";
    private static final String PERKEXCLUSIONSCONTAINS = "PERKEXCLUSIONSCONTAINS";
    private static final String PERKEXCLUSIONSSTARTSWITH = "PERKEXCLUSIONSSTARTSWITH";

    //    private static final String PerkModExclusions = "PerkModExclusions";
    private static final String PerkModInclusions = "PerkModInclusions";

    private static final String FollowerFactions = "FollowersFactions";
    private static final String FollowersForced = "ForcedFollowers";

    private static final String thisIniName = "AutomaticPerks.ini";

    public AutomaticPerksModule(Ini ini) throws IOException {
        name = "Automatic Perks";
        Ini thisIni = new Ini();
        Config c = thisIni.getConfig();
        c.setEmptyOption(true);
        c.setEmptySection(true);
        thisIni.load(new File(thisIniName));

        RecordSelector npcsToGetPerks = new RecordSelectorBuilder().type(GRUP_TYPE.NPC_).RecordValidator(ASIS.validNPCs_NoSpellsPerks).build();
        npcsToGetPerks.addInclusion(new SelectionSet(thisIni.get(NPCInclusions), new Matcher.EDIDStartsWith()));
        npcsToGetPerks.addExclusion(new SelectionSet(thisIni.get(NPCExclusions), new Matcher.EDIDContains()));
        npcsToGetPerks.addExclusion(new SelectionSet(thisIni.get(NPCModExclusions), new Matcher.ModContains()));
        npcsToGetPerks.addExclusion(new SelectionSet(ini.get("MODEXCLUSIONS"), new Matcher.ModContains()));
        npcsToGetPerks.addExclusion(new SelectionSet(thisIni.get(KeywordExclusions), new Matcher.HasKYWD()));
        this.addRecordSelector("npcsToGetPerks", npcsToGetPerks);

        RecordSelector perksToDistribute = new RecordSelectorBuilder().type(GRUP_TYPE.PERK).RecordValidator(RecordSelector.USE_NON_DELETED).build();
        perksToDistribute.addInclusion(new SelectionSet(thisIni.get(PerkInclusions), new Matcher.EDIDContainsOrBlank()));
        perksToDistribute.addExclusion(new SelectionSet(thisIni.get(PerkModInclusions), new Matcher.NotModContains()));
        perksToDistribute.addExclusion(new SelectionSet(thisIni.get(PERKEXCLUSIONSCONTAINS), new Matcher.EDIDContains()));
        perksToDistribute.addExclusion(new SelectionSet(thisIni.get(PERKEXCLUSIONSSTARTSWITH), new Matcher.EDIDStartsWith()));
        perksToDistribute.addExclusion(new SelectionSet(ini.get("MODEXCLUSIONS"), new Matcher.ModContains()));
        this.addRecordSelector("perksToDistribute", perksToDistribute);

        RecordSelector followersSelector = new RecordSelectorBuilder().type(GRUP_TYPE.NPC_).RecordValidator(ASIS.validNPCs_NoSpellsPerks).build();
        followersSelector.addInclusion(new SelectionSet(thisIni.get(FollowerFactions), new Matcher.HasFaction()));
        followersSelector.addInclusion(new SelectionSet(thisIni.get(FollowersForced), new Matcher.EDIDEquals()));
        followersSelector.addExclusion(new SelectionSet(thisIni.get(NPCModExclusions), new Matcher.ModContains()));
        followersSelector.addExclusion(new SelectionSet(ini.get("MODEXCLUSIONS"), new Matcher.ModContains()));
        this.addRecordSelector("followersToGetPerks", followersSelector);
    }

    static Map<PERK, PerkInfo> getPerkLists(String name, Collection<MajorRecord> source) {
        SPProgressBarPlug.setStatus(name + ": Mapping Perks");
        SPProgressBarPlug.incrementBar();

        Map<PERK, PerkInfo> perkMap = new HashMap<>();

        for (MajorRecord r : source) {
            PERK p = (PERK) r;
            if (p != null) {
                PerkInfo theInfo = new PerkInfo();
                boolean passed = false;
                boolean failed = false;
                for (Condition c : p.getConditions()) {
                    boolean global = c.get(CondFlag.UseGlobal);
                    Enum func = c.getFunction();
                    Operator o = c.getOperator();
                    // add if check actor value >= number and not comparing to a GLOB
                    if ((func == P_Int.GetBaseActorValue) && (o == Operator.GreaterThanOrEqual) && (!global)) {
//                        FormID nextPerkForm = p.getNextPerk();
//                        if (!nextPerkForm.equals(FormID.NULL)) {
//                            nextPerksMap.put(nextPerkForm, p.getForm());  // shouldn't matter ??
//                        } else {
//                            nextPerkForm = p.getForm();
//                        }

                        int val = (Integer) c.getParam1();
                        ActorValue av = skyproc.genenums.ActorValue.value(val);
                        Skill skill = getAVasSkill(av);
                        if (skill != null) {
                            theInfo.addCondition(skill, c.getValueFloat());
                            passed = true;
                        } else {
                            failed = true;
                        }
                    }
                    // exclude conditions?
                }
                if (passed && !failed) {
                    perkMap.put(p, theInfo);
                }
            }
        }
        return perkMap;
    }

    static Skill getAVasSkill(ActorValue av) {
        String avs = av.name().toUpperCase();
        try {
            return Skill.valueOf(avs);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void runModuleChanges(ASISPatcherConfig asis) {
        Map<PERK, PerkInfo> perkMap = getPerkLists(name, getRecordSelector("perksToDistribute").getValidRecords());
        addPerks(getRecordSelector("npcsToGetPerks"), perkMap, asis);
        addPerks(getRecordSelector("followersToGetPerks"), perkMap, asis);
    }

    private void addPerks(RecordSelector recordSelector, Map<PERK, PerkInfo> perkMap, ASISPatcherConfig asis) {
        try {
            Mod patch = SPGlobal.getGlobalPatch();
            Mod merger = Controller.getAllRecords();
            for (MajorRecord r : recordSelector.getValidRecords()) {
                NPC_ n = (NPC_) r;
                if (n != null) {
                    boolean changed = false;

                    ArrayList<SubFormInt> npcPerks = n.getPerks();
                    ArrayList<FormID> npcPerkForms = new ArrayList<>(npcPerks.size());
                    for (SubFormInt s : npcPerks) {
                        npcPerkForms.add(s.getForm());
                    }
                    for (Entry<PERK, PerkInfo> e : perkMap.entrySet()) {
                        boolean failedCondition = npcPerkForms.contains(e.getKey().getForm());
                        // ranks disabled since no way to test how many times they should take it
//                        int rank = 0;
                        //                            rank = npcPerks.get(npcPerkForms.indexOf(e.getKey().getForm())).getNum();
                        if (!failedCondition) {
                            NPC_ temp = n;

                            while (temp.isTemplated() && temp.get(NPC_.TemplateFlag.USE_STATS)) {
                                NPC_ nTemp = (NPC_) merger.getMajor(temp.getTemplate(), GRUP_TYPE.NPC_);
                                if (nTemp == null) {
                                    LVLN lTemp = (LVLN) merger.getMajor(temp.getTemplate(), GRUP_TYPE.LVLN);
                                    if (lTemp == null) {
                                        SPGlobal.logError(name, "NPC: " + n
                                                + ", has template FormID entry: " + temp.getTemplate()
                                                + ", which cannot be resolved to an NPC or Leveled NPC.\n"
                                                + "Skipping for patch but this is probably a very bad thing");
                                    }
                                    failedCondition = true;
                                    break;
                                }
                                temp = nTemp;
                            }
                            if (!failedCondition) {
                                for (PerkCondition cond : e.getValue().getConditions()) {
                                    Skill theSkill = cond.getSkill();
                                    if (temp.get(theSkill) < cond.getValue()) {
                                        failedCondition = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!failedCondition) {
                            n.addPerk(e.getKey().getForm(), 1);
                            changed = true;
                        }
                    }
                    if (changed) {
                        asis.npcsToWrite().add(n.getForm());
                        patch.addRecord(n);
                    }
                }
            }
        } catch (Exception exception) {
            SPGlobal.logException(exception);
            JOptionPane.showMessageDialog(null, "Something terrible happened running " + name
                    + ": " + exception + " Check the readme and debug logs.");
            System.exit(0);
        }
    }

    static class PerkInfo {

        private final ArrayList<PerkCondition> conditions = new ArrayList<>();

        public PerkInfo() {
        }

        private void addCondition(Skill sk, float valueFloat) {
            conditions.add(new PerkCondition(sk, valueFloat));
        }

        ArrayList<PerkCondition> getConditions() {
            return conditions;
        }
    }

    static class PerkCondition {

        final Skill sk;
        final float value;

        public PerkCondition(Skill sk, float valueFloat) {
            this.sk = sk;
            this.value = valueFloat;
        }

        public Skill getSkill() {
            return sk;
        }

        float getValue() {
            return value;
        }
    }
}
