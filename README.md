SlingBeans
----------
SlingBeans is a plugin for NetBeans 8 IDE for AEM and Sling developers.

Download beta NBM (NetBeans plugin) file for testing [HERE](https://github.com/jkan997/SlingBeans/blob/master/dist/org-jkan997-slingbeans.nbm?raw=true)

* Watch screencast on SlingBeans installation - [HERE](http://youtu.be/uZPBTn3Ho7E)
* Watch screencast about editing remote AEM repository with SlingBeans - [HERE](https://youtu.be/8X9r-6w2FM4)
* Watch screencast about editing local Maven AEM Content Project with Synchronization - COMMING SOON


Please consult [User Manual](https://github.com/jkan997/SlingBeans/wiki/User-manual) for detailed instructions


Main features:
* Remote resource tree directly available in IDE with both attribute, binary content, upload functionalities
* It is possible to edit local VLT exported tree as original structure (with attributes etc.)
* It is possible to automatically invoke sync actions (bi-directional based on Apache Vault)

![screen shot 2015-05-23 at 01 37 12](https://cloud.githubusercontent.com/assets/2896358/7781298/6b6fcfea-00ec-11e5-90d2-71ea33f4464d.png)

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
Version 1.1 (2016-01-29)
* Fixed node refresh issues after import export
* Added 'Open filter file...' command inside project 'Sling Content' 
* Local Sling tree not collapsing after export to server
* Logging turned off by default
* Update available notification from the next version
* It is possible to create new files inside the project (but not others node -> create on remote and then export)
