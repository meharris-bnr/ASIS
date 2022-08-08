package ASIS_Controller;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public abstract class Framework_Module_Abstract implements Framework_Module {
    protected final Map<String, RecordSelector> recordSelectors = new LinkedHashMap<>();
    protected String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addRecordSelector(String name, RecordSelector recordSelector) {
        recordSelectors.put(name, recordSelector);
    }

    @Override
    public RecordSelector getRecordSelector(String name) {
        return recordSelectors.get(name);
    }

    @Override
    public Set<RecordSelector> getRecordSelectors() {
        Set<RecordSelector> toReturn = new LinkedHashSet<>();
        for (Entry<String, RecordSelector> e : recordSelectors.entrySet()) {
            toReturn.add(e.getValue());
        }
        return toReturn;
    }
}
