package ASIS_Controller;

import skyproc.MajorRecord;
import skyproc.Mod;
import skyproc.SPGlobal;
import skyproc.gui.SPProgressBarPlug;
import skyprocstarter.ASISPatcherConfig;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author David Tynan
 */
public class Controller {

    static final Set<Framework_Module> modules = new LinkedHashSet<>();
    private static final Mod AllRecords = new Mod("Controller_temp", false);
    static Map<RecordSelector, Set<MajorRecord>> validRecordsMap = new HashMap<>();

    private final ASISPatcherConfig asis;

    public Controller(ASISPatcherConfig asis) {
        this.asis = asis;
        AllRecords.addAsOverrides(SPGlobal.getDB());
    }

    public static Mod getAllRecords() {
        return AllRecords;
    }

    public void addModule(Framework_Module m) {
        modules.add(m);
    }

    public void removeModule(Framework_Module m) {
        modules.remove(m);
    }

    public void runAllModule() {
        for (Framework_Module m : modules) {
            runModule(m);
        }
    }

    public void runModule(Framework_Module toRun) {
        SPProgressBarPlug.setStatus(toRun.getName() + ": Starting Patch");
        SPProgressBarPlug.incrementBar();

        processInput(toRun);
        setValidRecords(toRun);
        toRun.runModuleChanges(asis);
        AllRecords.addAsOverrides(SPGlobal.getGlobalPatch());
    }

    protected void processInput(Framework_Module runningModule) {
        for (RecordSelector selector : runningModule.getRecordSelectors()) {
            for (SelectionSet inclusions : selector.getInclusionsSet()) {
                inclusions.setup();
            }
            for (SelectionSet exclusions : selector.getExclusionsSet()) {
                exclusions.setup();
            }
        }

    }

    protected void setValidRecords(Framework_Module runningModule) {
        for (RecordSelector selector : runningModule.getRecordSelectors()) {
            Set<MajorRecord> sourceRecords = selector.getRecordSource().getRecords(selector.getGRUP());

            for (MajorRecord r : sourceRecords) {
                selector.validate(r);
            }
        }
    }
}
