package asis;

import ASIS_Controller.RecordValidator;
import skyproc.MajorRecord;
import skyproc.NPC_;

import java.awt.*;

/**
 * ASIS hard-coded settings
 *
 * @author pc tech
 */
public class ASIS {

    public static final RecordValidator validNPCs_NoScripts = new RecordValidator() {
        @Override
        public boolean useDeleted() {
            return false;
        }

        @Override
        public boolean isRejectedTemplate(MajorRecord rec) {
            NPC_ n = (NPC_) rec;
            if (n != null) {
                return n.isTemplated() && n.get(NPC_.TemplateFlag.USE_SCRIPTS);
            }
            return true;
        }
    };

    public static final RecordValidator validNPCs_NoSpellsPerks = new RecordValidator() {
        @Override
        public boolean useDeleted() {
            return false;
        }

        @Override
        public boolean isRejectedTemplate(MajorRecord rec) {
            NPC_ n = (NPC_) rec;
            if (n != null) {
                return n.isTemplated() && n.get(NPC_.TemplateFlag.USE_SPELL_LIST);
            }
            return true;
        }
    };

    //Colors used in the GUI.
    static public final Color blue = new Color(0, 147, 196);
    public static final Color headerColor = new Color(66, 181, 184);  // Teal
    public static final Color settingsColor = new Color(72, 179, 58);  // Green
    //Fonts used in the GUI.
    static public final Font settingsFont = new Font("Serif", Font.BOLD, 14);
    static public final Font settingsFontSmall = new Font("Serif", Font.BOLD, 12);
}
