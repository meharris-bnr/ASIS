package asis;

import lev.gui.LCheckBox;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;

/**
 * @author Justin Swanson
 */
public class OtherSettingsPanel extends SPSettingPanel {

    LCheckBox noBOSS;

    public OtherSettingsPanel(SPMainMenuPanel parent_) {
        super(parent_, "Other Settings", ASIS.headerColor);
    }

    @Override
    protected void initialize() {
        super.initialize();

        noBOSS = new LCheckBox("Skip BOSS", ASIS.settingsFont, ASIS.settingsColor);
        noBOSS.setOffset(2);
        noBOSS.addShadow();
        setPlacement(noBOSS);
        AddSetting(noBOSS);

        alignRight();
    }
}
