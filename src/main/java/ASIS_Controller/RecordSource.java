package ASIS_Controller;

import skyproc.GRUP_TYPE;
import skyproc.MajorRecord;

import java.util.Set;

/**
 * @author David Tynan
 */
public interface RecordSource {
    Set<MajorRecord> getRecords(GRUP_TYPE type);
}
