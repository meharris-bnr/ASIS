package skyprocstarter;

import ASIS_Controller.Controller;
import asis.*;
import asis.gui.*;
import asis.ini_asis.TweakIniList;
import lev.gui.LTextPane;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import skyproc.*;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPProgressBarPlug;
import skyproc.gui.SPSettingPanel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The important functions to change are:
 * - welcomePanelFactory, otherSettingsPanelFactory, where you set up the GUI
 * - customPatcher, where you put all the processing code and add records to the output patch.
 */
@Slf4j
@Configuration
public class ASISPatcherConfig {

    @Bean(name = "welcomePanelFactory")
    Function<SPMainMenuPanel, SPSettingPanel> getWelcomePanelFactory() {
        return parent -> new SPSettingPanel(parent, SkyProcStarter.myPatchName, SkyProcStarter.headerColor) {

            LTextPane introText;

            @Override
            protected void initialize () {
                super.initialize();

                introText = new LTextPane(settingsPanel.getWidth() - 40, 400, SkyProcStarter.settingsColor);
                introText.setText(SkyProcStarter.welcomeText);
                introText.setEditable(false);
                introText.setFont(SkyProcStarter.settingsFont);
                introText.setCentered();
                setPlacement(introText);
                Add(introText);

                alignRight();
            }
        };
    }

    @Bean(name = "otherSettingsPanelFactory")
    Function<SPMainMenuPanel, List<SkyProcStarter.SettingsPanelDescriptor>> getOtherSettingsPanelFactory() {
        return parent -> {
            List<SkyProcStarter.SettingsPanelDescriptor> settingsPanelDescriptors = new ArrayList<>();
            settingsPanelDescriptors.add(new SkyProcStarter.SettingsPanelDescriptor(new SettingsIncreasedSpawns(parent, saveFile()), true, ASISSaveFile.GUISettings.INCREASEDSPAWNS_ON));
            settingsPanelDescriptors.add(new SkyProcStarter.SettingsPanelDescriptor(new SettingsAutomaticPerks(parent), true, ASISSaveFile.GUISettings.AUTOMATICPERKS_ON));
            settingsPanelDescriptors.add(new SkyProcStarter.SettingsPanelDescriptor(new SettingsAutomaticSpells(parent), true, ASISSaveFile.GUISettings.AUTOMATICSPELLS_ON));
            settingsPanelDescriptors.add(new SkyProcStarter.SettingsPanelDescriptor(new SettingsCustomizedAI(parent), true, ASISSaveFile.GUISettings.CUSTOMIZEDAI_ON));
            settingsPanelDescriptors.add(new SkyProcStarter.SettingsPanelDescriptor(new SettingsCustomizedGMSTs(parent), true, ASISSaveFile.GUISettings.CUSTOMIZEDGMSTS_ON));
            settingsPanelDescriptors.add(new SkyProcStarter.SettingsPanelDescriptor(new SettingsNPCPotions(parent, saveFile()), true, ASISSaveFile.GUISettings.NPCPOTIONS_ON));
            settingsPanelDescriptors.add(new SkyProcStarter.SettingsPanelDescriptor(new SettingsSpawnRandomizer(parent, saveFile()), true, ASISSaveFile.GUISettings.SPAWNRANDOMIZER_ON));
            settingsPanelDescriptors.add(new SkyProcStarter.SettingsPanelDescriptor(new SettingsNPCEnchantFix(parent), true, ASISSaveFile.GUISettings.NPCENCHANTFIX_ON));

            return settingsPanelDescriptors;
        };
    }

    @Bean(name = "save")
    public SkyProcSave saveFile() {
        return new ASISSaveFile();
    }

    @Bean(name = "npsToWrite")
    public Set<FormID> npcsToWrite() {
        return new HashSet<>();
    }

    private String ASISlanguage;
    private Ini asisIni;
    private String finishedSound;

    @Bean(name = "preProcessor")
    Runnable preProcessor() {
        return () -> {
            SPGlobal.Language lang;
            try {
                processINI();
                lang = SPGlobal.Language.valueOf(ASISlanguage);
            } catch (EnumConstantNotPresentException | IOException e) {
                lang = SPGlobal.Language.English;
            }
            SPGlobal.language = lang;
        };
    }

    private void processINI() throws IOException {
        SPProgressBarPlug.setStatus("ASIS: Processing INI");
        SPProgressBarPlug.incrementBar();
        //Sets up the file reader for the ini file.
        asisIni = new Ini();
        Config c = asisIni.getConfig();
        c.setEmptyOption(true);
        c.setEmptySection(true);
        c.setEscape(false);
        asisIni.load(new File("ASIS.ini"));

        Ini.Section lang = asisIni.get("LANGUAGE");
        try {
            Set<String> langKeys = lang.keySet();
            ASISlanguage = langKeys.iterator().next();
        } catch (Exception e) {
            ASISlanguage = "English";
        }

        //noinspection unused
        Ini.Section exclusions = asisIni.get("ModExclusions");

        Ini.Section settings = asisIni.get("Settings");

        try {
            finishedSound = settings.get("finishedSound");
        } catch (Exception e) {
            finishedSound = "";
        }
    }

    @Bean(name = "postProcessor")
    Consumer<Boolean> postProcessor() {
        return (patchWasGenearted) -> {
            if ((finishedSound != null) && !finishedSound.isEmpty()) {
                try {
                    File soundFile = new File(finishedSound);
                    playClip(soundFile);
                } catch (IOException | InterruptedException | LineUnavailableException | UnsupportedAudioFileException ex) {
                    SPGlobal.log("OnExit", "Could not play sound: " + ex);
                }
            }
            SPGlobal.logMain("EXIT", "Closing ASIS.");
        };
    }

    private static void playClip(File clipFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        class AudioListener implements LineListener {

            private boolean done = false;

            @Override
            public synchronized void update(LineEvent event) {
                LineEvent.Type eventType = event.getType();
                if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                    done = true;
                    notifyAll();
                }
            }

            public synchronized void waitUntilDone() throws InterruptedException {
                while (!done) {
                    wait();
                }
            }
        }
        AudioListener listener = new AudioListener();
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile)) {
            Clip clip = AudioSystem.getClip();
            try {
                clip.addLineListener(listener);
                clip.open(audioInputStream);
                clip.start();
                listener.waitUntilDone();
            } finally {
                clip.close();
            }
        }
    }

    final static int numSteps = 30;

    @Bean(name = "customPatcher")
    Consumer<Mod> getCustomerPatcher() {
        return mod -> {
            SPProgressBarPlug.setMax(numSteps);
            SPProgressBarPlug.setStatus("Initializing ASIS");

            try {
                asis();
            } catch (Exception e) {
                log.error("ASIS FATAL: {}", e.getMessage(), e);
            }
        };
    }
    //
    // ASIS main point of entry
    //
    public void asis() throws Exception {
        getTweakIniSetup();

        runClasses();

        SPProgressBarPlug.incrementBar();
    }

    public TweakIniList tweakInis;

    public TweakIniList getTweakIni() {
        return tweakInis;
    }

    private void getTweakIniSetup() {
        try {
            tweakInis = new TweakIniList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was an error with Tweak Inis.  Please contact the author.");
        }
    }

    public Ini getAsisIni() {
        return asisIni;
    }

    private void runClasses() throws IOException {
        Controller asisController = new Controller(this);
        UnTemplate unTemplateNpcs = new UnTemplate();

        Set<FormID> unTemplated = new HashSet<>();
        Mod patch = SPGlobal.getGlobalPatch();
        for (NPC_ n : patch.getNPCs()) {
            unTemplated.add(n.getForm());
        }

        if (saveFile().getBool(ASISSaveFile.GUISettings.AUTOMATICSPELLS_ON)) {
            SPProgressBarPlug.setStatus("Processing Automatic Spells");
            AutomaticSpellsModule spells = new AutomaticSpellsModule(asisIni);
            asisController.runModule(spells);
        }
        SPProgressBarPlug.incrementBar();

        if (saveFile().getBool(ASISSaveFile.GUISettings.INCREASEDSPAWNS_ON)) {
            SPProgressBarPlug.setStatus("Processing Increased Spawns");
            IncreasedSpawnsModule increasedSpawns = new IncreasedSpawnsModule(asisIni);
            asisController.runModule(increasedSpawns);
        }
        SPProgressBarPlug.incrementBar();

        if (saveFile().getBool(ASISSaveFile.GUISettings.NPCPOTIONS_ON)) {
            SPProgressBarPlug.setStatus("Processing NPCPotions");
            ASISnpcPotionsModule npcPotions = new ASISnpcPotionsModule(asisIni);
            asisController.runModule(npcPotions);
        }
        SPProgressBarPlug.incrementBar();

        if (saveFile().getBool(ASISSaveFile.GUISettings.CUSTOMIZEDAI_ON)) {
            SPProgressBarPlug.setStatus("Processing Customized AI");
            CustomizedAI myCustomizedAI = new CustomizedAI();
            myCustomizedAI.runCustomizedAI(this);
        }
        SPProgressBarPlug.incrementBar();

        if (saveFile().getBool(ASISSaveFile.GUISettings.AUTOMATICPERKS_ON)) {
            SPProgressBarPlug.setStatus("Processing Automatic Perks");
            AutomaticPerksModule perks = new AutomaticPerksModule(asisIni);
            asisController.runModule(perks);
        }
        SPProgressBarPlug.incrementBar();

        if (saveFile().getBool(ASISSaveFile.GUISettings.SPAWNRANDOMIZER_ON)) {
            SPProgressBarPlug.setStatus("Processing Spawn Randomizer");
            RandomSpawnsModule randomSpawns = new RandomSpawnsModule(asisIni);
            asisController.runModule(randomSpawns);
        }
        SPProgressBarPlug.incrementBar();

        if (saveFile().getBool(ASISSaveFile.GUISettings.CUSTOMIZEDGMSTS_ON)) {
            SPProgressBarPlug.setStatus("Processing Customized GMSTs");
            customizedGMSTs customGMSTs = new customizedGMSTs();
            customGMSTs.runCustomizedGMSTs(this);
        }
        SPProgressBarPlug.incrementBar();

        if (saveFile().getBool(ASISSaveFile.GUISettings.NPCENCHANTFIX_ON)) {
            SPProgressBarPlug.setStatus("Processing NPC Enchantment Fix");
            npcEnchantModule npcEnchantFix = new npcEnchantModule();
            asisController.runModule(npcEnchantFix);
        }
        SPProgressBarPlug.incrementBar();

        unTemplated.removeAll(npcsToWrite());
        for (FormID f : unTemplated) {
            patch.remove(f);
        }
    }
}
