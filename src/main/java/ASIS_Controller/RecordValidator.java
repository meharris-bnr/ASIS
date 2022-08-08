package ASIS_Controller;

import skyproc.MajorRecord;

/**
 * @author David Tynan
 */
public interface RecordValidator {
    boolean useDeleted();

    boolean isRejectedTemplate(MajorRecord rec);
}
