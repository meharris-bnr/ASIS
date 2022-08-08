package ASIS_Controller;

import org.ini4j.Ini;
import org.ini4j.Profile;
import skyproc.MajorRecord;

/**
 * @author David
 */
public class SelectionSet {
    private final Matcher theMatch;
    private final Ini.Section section;

    public SelectionSet(Profile.Section section, Matcher theMatch) {
        this.section = section;
        this.theMatch = theMatch;
    }

    public Profile.Section getSection() {
        return section;
    }

    public Matcher getTheMatch() {
        return theMatch;
    }


    public void setup() {
        theMatch.setup(section);
    }

    public boolean isMatch(MajorRecord rec) {
        return theMatch.isMatch(rec, section);
    }
}
