package org.jkan997.slingbeans.nbserver;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.netbeans.spi.server.ServerWizardProvider;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.InstantiatingIterator;
import org.openide.WizardDescriptor.Panel;
import org.openide.util.lookup.ServiceProvider;

//@ServiceProvider(service = ServerWizardProvider.class, path = "Servers")
public class SlingServerWizardProvider implements ServerWizardProvider {
    private static final String SLING_SERVER = "Sling/AEM Server";

    public SlingServerWizardProvider() {
    }

    @Override
    public String getDisplayName() {
        return SLING_SERVER;
    }

    @Override
    public InstantiatingIterator getInstantiatingIterator() {
        return new DemoWizardIterator(SLING_SERVER);
    }

    private static class DemoWizardIterator implements InstantiatingIterator {

        private final String name;
        private Panel panel;

        public DemoWizardIterator(String name) {
            this.name = name;
        }

        @Override
        public Set instantiate() throws IOException {
            return Collections.EMPTY_SET;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public synchronized Panel current() {
            if (panel == null) {
                panel = new SlingServerWizardHostPanel("Host");
            }
            return panel;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public void initialize(WizardDescriptor wizard) {
        }

        @Override
        public void uninitialize(WizardDescriptor wizard) {
        }

        @Override
        public void nextPanel() {
        }

        @Override
        public void previousPanel() {
        }

        @Override
        public void addChangeListener(ChangeListener l) {
        }

        @Override
        public void removeChangeListener(ChangeListener l) {
        }

    }
}
