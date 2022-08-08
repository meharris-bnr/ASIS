package ASIS_Controller;

import skyprocstarter.ASISPatcherConfig;

import java.util.Set;

/**
 * @author David Tynan
 */
public interface Framework_Module {

    String getName();

    Set<RecordSelector> getRecordSelectors();

    RecordSelector getRecordSelector(String name);

    void addRecordSelector(String name, RecordSelector recordSelector);

    void runModuleChanges(ASISPatcherConfig asis);
}
