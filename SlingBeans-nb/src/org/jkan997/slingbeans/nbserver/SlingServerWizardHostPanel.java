package org.jkan997.slingbeans.nbserver;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import org.jkan997.slingbeans.dialogs.host.SlingHostPanel;
import org.openide.WizardDescriptor.Panel;
import org.openide.util.HelpCtx;

class SlingServerWizardHostPanel implements Panel {

    private final String name;
    private JPanel panel;

    public SlingServerWizardHostPanel(String name) {
        this.name = name;
    }

    @Override
    public synchronized Component getComponent() {
        if (panel == null) {
            panel = new SlingHostPanel();
        }
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(Object settings) {
    }

    @Override
    public void storeSettings(Object settings) {
    }

}
