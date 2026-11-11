package org.jkan997.slingbeans.vlt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.jackrabbit.vault.davex.DAVExRepositoryFactory;
import org.apache.jackrabbit.vault.fs.Mounter;
import org.apache.jackrabbit.vault.fs.api.PathFilterSet;
import org.apache.jackrabbit.vault.fs.api.RepositoryAddress;
import org.apache.jackrabbit.vault.fs.api.RepositoryFactory;
import org.apache.jackrabbit.vault.fs.api.VaultFile;
import org.apache.jackrabbit.vault.fs.api.VaultFileSystem;
import org.apache.jackrabbit.vault.fs.config.DefaultWorkspaceFilter;
import org.apache.jackrabbit.vault.fs.io.FileArchive;
import org.apache.jackrabbit.vault.fs.io.ImportOptions;
import org.apache.jackrabbit.vault.fs.io.Importer;
import org.apache.jackrabbit.vault.fs.io.PlatformExporter;
import org.apache.jackrabbit.vault.util.Constants;
import org.jkan997.slingbeans.helper.IOHelper;
import org.jkan997.slingbeans.helper.LogHelper;
import static org.jkan997.slingbeans.helper.StringHelper.normalizePath;
import org.jkan997.slingbeans.helper.VltPath;

public class VltManager {

    private String repository;
    private String user;
    private String password;
    private Session vltSession;

    private final static RepositoryFactory FACTORY = new DAVExRepositoryFactory();
    private RepositoryAddress vltRepositoryAddress;
    private Repository vltRepository;

    public VltManager(String repository, String user, String password) {
        this.repository = repository;
        this.user = user;
        this.password = password;
        LogHelper.logInfo(this, "VltManager init %s %s %s", repository, user, password);
    }

    public File getFilterFile(String localContentPath) {
        File metaInfDir = new File(localContentPath + "/" + Constants.META_INF);
        if (metaInfDir.isDirectory()) {
            File vaultDir = new File(metaInfDir, Constants.VAULT_DIR);
            if (vaultDir.isDirectory()) {
                File filterFile = new File(vaultDir, Constants.FILTER_VLT_XML);
                if (filterFile.isFile()) {
                    return filterFile;
                } else {
                    filterFile = new File(vaultDir, Constants.FILTER_XML);
                    if (filterFile.isFile()) {
                        return filterFile;
                    }
                }
            }
        }
        return null;
    }

    /*InputStream is = file.getArtifact().getInputStream();
     ArtifactSet as = file.getAggregate().getArtifacts();
        
     for (Artifact a1 : as.values()){
     System.out.println("A1 " + a1);
     }
     FileOutputStream fos = new FileOutputStream("/tmp/aaa");
     IOHelper.readInputStreamToOutputStream(is, fos);
     fos.close();*/
    /*FileArchive fa = new FileArchive(testFile);
     fa.open(false);
     this.archive = fa;
     System.out.println(fa.getJcrRoot());
     printEntry(fa.getRoot(), 0);*/
    /* Importer imp = new Importer();
     imp.run(fa, session.getRootNode());*/
    private Session getSession() throws URISyntaxException, RepositoryException {
        if ((vltSession != null) && (vltSession.isLive())) {
     //       return vltSession;
        }
        if (vltSession!=null){
        try{
            vltSession.logout();
        } catch (Exception ex){};
        }
        vltRepositoryAddress = new RepositoryAddress(repository);
        vltRepository = FACTORY.createRepository(vltRepositoryAddress);
        vltSession = vltRepository.login(new SimpleCredentials(user, password.toCharArray()));
        return vltSession;
    }

    private boolean lockUpdates = false;

    public void exportContentFromRemote(String contentPath, String jcrPath) throws Exception {
        contentPath = normalizePath(contentPath,true);
        jcrPath = normalizePath(jcrPath, true);
        lockUpdates = true;
        LogHelper.logInfo(this, "exportContent(%s, %s)", contentPath, jcrPath);
        vltSession = getSession();
        DefaultWorkspaceFilter filter = null;
        //  VltPath vltPath = new VltPath(path);
        File filterFile = getFilterFile(contentPath);
        byte[] filterXmlBa = IOHelper.readInputStreamToBytes(new FileInputStream(filterFile));
        if (filterFile != null) {
            filter = new DefaultWorkspaceFilter();
            filter.load(filterFile);
        }
        
        VaultFileSystem fs = Mounter.mount(null, filter, vltRepositoryAddress, null, vltSession);
        System.out.println(jcrPath);
        VaultFile vaultFile = "".equals(jcrPath)?fs.getRoot():fs.getFile(jcrPath);
        File localContentFile = new File(contentPath);
        PlatformExporter pe = new PlatformExporter(localContentFile);
        pe.export(vaultFile);
        FileOutputStream fos = new FileOutputStream(filterFile);
        fos.write(filterXmlBa);
        fos.close();
        touchRecursively(new File(contentPath), System.currentTimeMillis());
        lockUpdates = false;
    }

    public void importContentToRemote(String contentPath, String jcrPath) throws Exception {
        importContentToRemote(contentPath, jcrPath, false);
    }

    public void importContentToRemote(String contentPath, String jcrPath, boolean nonRecursive) throws Exception {
        if (lockUpdates) {
            return;
        }
        contentPath = normalizePath(contentPath, true);
        jcrPath = normalizePath(jcrPath, true);
        LogHelper.logInfo(this, "importContent(%s, %s, %s)", contentPath, jcrPath, nonRecursive);
        //  File localFile = new File(localContentPath);
        File vltContent = new File(contentPath);
        FileArchive vltFileArchive = new FileArchive(vltContent);
        vltSession = getSession();
        vltFileArchive.open(false);
        ImportOptions impOpts = new ImportOptions();
        impOpts.setNonRecursive(false);
        DefaultWorkspaceFilter defaultWorkspaceFilter = new DefaultWorkspaceFilter();
        defaultWorkspaceFilter.add(new PathFilterSet(jcrPath));
        impOpts.setFilter(defaultWorkspaceFilter);
        Importer imp = new Importer(impOpts);
        Node remoteRootNode = null;
        remoteRootNode = vltSession.getRootNode();
        imp.run(vltFileArchive, remoteRootNode);
        vltFileArchive.close();
    }
    /*
     private void printEntry(Entry e, int level) throws IOException {
     Collection<? extends Entry> children = e.getChildren();
     print(level, e.getName());
     if (e.getName().endsWith(".xml")) {
     Artifact a = (new InputSourceArtifact(
     null,
     Constants.DOT_CONTENT_XML,
     "",
     ArtifactType.PRIMARY,
     archive.getInputSource(e),
     SerializationType.XML_DOCVIEW
     ));
     System.out.println(a.getClass());
     }
     if (children != null) {
     for (Entry ent : children) {
     printEntry(ent, level + 1);
     }
     }
     }

     private void print(int level, String name) {
     String s = "";
     int spacing = level * 2;
     for (int i = 0; i <= spacing; i++) {
     s += " ";
     }
     s += name;
     System.out.println(s);
     }*/

    public static void main(String[] args) throws Exception {
        String repo = "http://cq5linux:4502/crx/server/crx.default";
        VltManager vi = new VltManager(repo, "admin", "admin");
       // vi.exportContentFromRemote("/Volumes/MacData/jakaniew/git/test-content/src/main/content/","apps");
        //vi.importContentToRemote("/Volumes/MacData/jakaniew/git/test-content/src/main/content", "/apps/dsw/config/org.apache.sling.commons.log.LogManager.factory.config-dsw");
        //vi.importContent(null);
        String localPath = "/Volumes/MacData/jakaniew/git/Adobe/Custom-Demos/navy-dam/content/src/main/content";
        String remotePath = "/";
        vi.exportContentFromRemote(localPath, remotePath);

    }

    private void touchRecursively(File file, long timestamp) {
        if (file.lastModified() == 0) {
            file.setLastModified(timestamp);
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                touchRecursively(childFile, timestamp);
            }
        }
    }
}
