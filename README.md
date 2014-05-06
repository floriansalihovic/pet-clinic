# The Sling PetClinic Application

This application is intended to provide a fast and easy access to Apache Sling using Groovy as a scripting language.

## Before getting started - getting Sling

I highly recommend checking the Sling sources out from the repository (GIT or SVN) and compiling it by hand. The launchpad
provided by the apache project side is out date.

## Getting started

The parent project will be created from a maven-archetype-quickstart.

    #!/bin/sh
    mvn archetype:generate \
        -DarchetypeGroupId=org.apache.maven.archetypes \
        -DarchetypeArtifactId=maven-archetype-quickstart \
        -DgroupId=io.github.floriansalihovic.sling \
        -DartifactId=pet-clinic \
        -Dpackage=io.github.floriansalihovic.petclinic \
        -Dversion=1.0.0-SNAPSHOT

It is the parent project for modules, which make up the application. After the project was created, a few first changes
should be made to the pom.

    <properties>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      <sling.user>admin</sling.user>
      <sling.password>admin</sling.password>
      <sling.port>8080</sling.port>
    </properties>

Adding the properties ```sling.user```, ``sling.password``` and ```sling.port``` provides easy access to the basic
configuration of Sling. These configuration properties are needed in modules to access Sling for deployments.

The main module needed for this project is a ```ui``` project. It contains all aspects of the project necessary for
displaying data.

    mvn archetype:generate \
        -DarchetypeGroupId=org.apache.sling \
        -DarchetypeArtifactId=sling-initial-content-archetype \
        -DarchetypeVersion=1.0.0 \
        -DgroupId=io.github.floriansalihovic.petclinic \
        -DartifactId=pet-clinic-ui \
        -Dversion=1.0.0-SNAPSHOT

This command will create the module along with some initial data (definitions, content and a script). The data is
stored under ```pet-clinic-ui/src/main/SLING-INF```.

- ```content/```contains the initial content provided by the archetype.
- ```nodetypes/```provides and example node type, which is basically a data type definition for the Java Content Repository.
- ```scripts/``` provides a basic example for a Sling resource renderer.

When the module is added, some first changes should be made.

- Using the properties defined in the parent ```pom```.
- Updating the plugin versions.
- Renaming ```SLING-INF/scripts``` to ```SLING-INF/apps``` and also adding the directories ```my/node```.
  This is a very sensitive change, because also the the ```pom```'s content needs to be changed accordingly.


```
    <!-- project.build.plugins -->
    <plugin>
      <groupId>org.apache.felix</groupId>
      <artifactId>maven-bundle-plugin</artifactId>
      <extensions>true</extensions>
      <version>2.4.0</version>
      <configuration>
        <instructions>
          <Sling-Nodetypes>SLING-INF/nodetypes/nodetypes.cnd</Sling-Nodetypes>
          <Sling-Initial-Content>
            SLING-INF/apps/my/node;overwrite:=true;uninstall:=true;path:=/apps/my/node,
            SLING-INF/content;overwrite:=true;uninstall:=true;path:=/content
          </Sling-Initial-Content>
        </instructions>
      </configuration>
    </plugin>
```

The change made was very subtle. From ```SLING-INF/scripts;``` to ```SLING-INF/apps/my/node;```. This was done mainly
for initial consistency of the project. The way scripts (components) are resolved in the repository follows a strict
scheme, which needs to be understood in order to work with sling efficiently. A detailed guide on components follows
later in the tutorial.

## Initial content

Before working efficiently on the application, providing some data to work with is needed. In Sling, adding content can
be done in a variety of ways. One would be a simple POST via ```curl```from the command line:

    #!/bin/sh
    curl -FfirstName="George" \
         -FlastName="Franklin" \
         -Faddress="Madison, 110 W. Liberty St." \
         -Ftelephone="6085551023" \
         http://localhost:8080/sling/content/owners/georgefranklin

This would create the node ```/sling/content/owners/georgefranklin``` with the given properties. A more reusable
approach is providing a module with demo content, which can be refined as the application matures. Using the same Maven
command used when creating the ```pet-clinic-ui``` with a different ```archetypeId``` generates the appropriate module.

    mvn archetype:generate \
        -DarchetypeGroupId=org.apache.sling \
        -DarchetypeArtifactId=sling-initial-content-archetype \
        -DarchetypeVersion=1.0.0 \
        -DgroupId=io.github.floriansalihovic.petclinic \
        -DartifactId=pet-clinic-demo-content \
        -Dversion=1.0.0-SNAPSHOT

The module's layout equals the one of the ```pet-clinic-ui``` so we make the same modifications. But since this module
is used as a container for content only, the folders ```scripts/``` and ```nodetypes/``` can be deleted right away. In
```pet-clinic-ui```, the folder ```content/``` can be deleted - the content will be stored in ```pet-clinic-demo-content```.

Running ```mvn install -PautoInstallBundle``` will install the project in the local repository and deploy it to the
running Sling instance. When calling ```http://localhost:8080/my-first-node.html``` a simple representation of
```my-first-node.xml``` is displayed. Similar to my-first-node.xml the application's initial content is set up. By
providing content under ```SLING-INF/sling/content/owners``` like

    <node>
      <primaryNodeType>nt:unstructured</primaryNodeType>

      <property>
        <name>firstName</name>
        <type>String</type>
        <value>George</value>
      </property>

      <property>
        <name>lastName</name>
        <type>String</type>
        <value>Franklin</value>
      </property>

      <property>
        <name>address</name>
        <type>String</type>
        <value>110 W. Liberty St.</value>
      </property>

      <property>
        <name>city</name>
        <type>String</type>
        <value>Madison</value>
      </property>

      <property>
        <name>telephone</name>
        <type>String</type>
        <value>6085551023</value>
      </property>

    </node>

test data is added. The initial content and renderer is kept in order to have reference data throughout the tutorial -
until dedicated render components are provided.