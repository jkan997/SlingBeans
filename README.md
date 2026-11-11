SlingBeans
----------
SlingBeans is a plugin for NetBeans IDE (Apache NetBeans 12 and newer, running on JDK 11+) for AEM and Sling developers.

Download beta NBM (NetBeans plugin) file for testing [HERE](https://github.com/jkan997/SlingBeans/blob/master/dist/org-jkan997-slingbeans.nbm?raw=true) . 
For old version of NetBeans you have to download older file and build the plugin yourself. The plugin requires JDK >= 11.

* Watch screencast on SlingBeans installation - [HERE](http://youtu.be/uZPBTn3Ho7E)
* Watch screencast about editing remote AEM repository with SlingBeans - [HERE](https://youtu.be/8X9r-6w2FM4)
* Watch screencast about editing local Maven AEM Content Project with Synchronization - COMMING SOON


Please consult [User Manual](https://github.com/jkan997/SlingBeans/wiki/User-manual) for detailed instructions


Main features:
* Remote resource tree directly available in IDE with both attribute, binary content, upload functionalities
* It is possible to edit local VLT exported tree as original structure (with attributes etc.)
* It is possible to automatically invoke sync actions (bi-directional based on Apache Vault)

![screen shot 2015-05-23 at 01 37 12](https://cloud.githubusercontent.com/assets/2896358/7781298/6b6fcfea-00ec-11e5-90d2-71ea33f4464d.png)

Building
----------
The project is built with Maven as a multi-module build. The former Ant / NetBeans project
setup (`build.xml`, `nbproject/`) and the bundled jars (`libs/`, `release/modules/ext/`) were
removed: all dependencies are now resolved from Maven repositories.

Requirements:
* JDK 11 or newer to build (tested with JDK 11, 17 and 21); classes are compiled for Java 11
* Maven 3 (tested with 3.10.0)

```sh
mvn clean install
```

The plugin is generated at `SlingBeans-nb/target/org-jkan997-slingbeans-<version>.nbm`.

Project structure:

```
SlingBeans/
├── pom.xml              parent POM (dependency and plugin versions)
├── SlingBeans-lib/      core library: Sling/JCR client, FileVault sync, dialogs (jar)
├── SlingBeans-utils/    auxiliary utilities (jar)
├── SlingBeans-nb/       NetBeans module (nbm packaging, nbm-maven-plugin)
│   └── src/main/nbm/manifest.mf
└── dist/                published NBM files and update info
```

Each module follows the standard Maven layout (`src/main/java`, `src/main/resources`,
`src/test/java`). The NetBeans module is compiled against the Apache NetBeans 12 APIs
(`RELEASE120`); `SlingBeans-lib` and its third-party libraries are bundled inside the NBM.

The project can be opened directly in NetBeans as a Maven project.


AEM Multimodule Project in SlingBeans
----------
To use AMC archetype in NetBeans you have just to install it in local repo

```sh
git clone https://github.com/Adobe-Marketing-Cloud/aem-project-archetype.git aem-project-archetype
cd aem-project-archetype
mvn install
```


Changelog
----------
Version 1.2 (2020-10-20)
* Added support for Apache NetBeans 12

Version 1.1 (2016-01-29)
* Fixed node refresh issues after import export
* Added 'Open filter file...' command inside project 'Sling Content' 
* Local Sling tree not collapsing after export to server
* Logging turned off by default
* Update available notification from the next version
* It is possible to create new files inside the project (but not others node -> create on remote and then export)
