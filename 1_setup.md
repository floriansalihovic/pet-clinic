## Setup - Obtaining Sling and creating a the project

### Obtaining Sling

Getting a running Sling instance up and running is fairly easy. It is highly recommended to checkout the latest version
from the SVN/GIT repository and build it. The downloads available are outdated and compatibility may not be given for
all the plugins. But [https://sling.apache.org/documentation/development/getting-and-building-sling.html](Getting and
building Sling) provides an extensive guide to follow.

### Setting up the scene

The application being developed has a discrete scope, providing some space to test and learn but also archive the small
goals in a short range of time. The application is a Sling flavoured of the [http://docs.spring.io/docs/petclinic.html]
(The Spring PetClinic Application), based on the actual html templates of [https://medium.com/@andrey_cheptsov/664d2e1242f]
(Andrey Cheptsov's Kotlin port).

The application will provide a user interface for adding and modifying content as well as some demo content for testing.

Starting with an empty [Maven Quickstart Archetype](http://maven.apache.org/archetype/maven-archetype-bundles/maven-archetype-quickstart/)
project, it will serve as a parent project for the various bundles which make up the application.

    #!/bin/sh
    mvn archetype:generate \
        -DarchetypeGroupId=org.apache.maven.archetypes \
        -DarchetypeArtifactId=maven-archetype-quickstart \
        -DgroupId=io.github.floriansalihovic.sling \
        -DartifactId=pet-clinic \
        -Dpackage=io.github.floriansalihovic.petclinic \
        -Dversion=1.0.0-SNAPSHOT

The Sling project provides some archetypes as well. Each with a slightly different purpose and content. The list of available archetypes can be found [here](http://sling.apache.org/site/maven-archetypes.html). For the scope of this guide two archetypes will be used.

- ```Sling Inititial Content Archetype```
- ```Sling Bundle Archetype```

The ```Sling Inititial Content Archetype``` will be used for two bundles. One providing the user interface components and the other one providing the demo content of the application, similar to the data found in the example applications. The bundle derived from the ```Sling Bundle Archetype``` will contain all services needed for data manipulation or features, which can not be implemented in the view. Even if the functionality can be added to the user interface bundle, separating the responsibilities is a good way to keep the sources transparent. Changing into the ```pet-clinic``` project and running the following commands will create the Maven modules.

    #!/bin/sh
    mvn archetype:generate \
        -DarchetypeGroupId=org.apache.sling \
        -DarchetypeArtifactId=sling-initial-content-archetype \
        -DarchetypeVersion=1.0.0 \
        -DgroupId=io.github.floriansalihovic.petclinic \
        -DartifactId=pet-clinic-ui \
        -Dversion=1.0.0-SNAPSHOT

will create the user interface bundle and

    #!/bin/sh
    mvn archetype:generate \
        -DarchetypeGroupId=org.apache.sling \
        -DarchetypeArtifactId=sling-initial-content-archetype \
        -DarchetypeVersion=1.0.0 \
        -DgroupId=io.github.floriansalihovic.petclinic \
        -DartifactId=pet-clinic-demo-content \
        -Dversion=1.0.0-SNAPSHOT

will provide a bundle for the demo content. The module for the services will be created with the following command.

    #!/bin/sh
    mvn archetype:generate \
        -DarchetypeGroupId=org.apache.sling \
        -DarchetypeArtifactId=sling-bundle-archetype \
        -DarchetypeVersion=1.0.0 \
        -DgroupId=io.github.floriansalihovic.petclinic \
        -DartifactId=pet-clinic-core \
        -Dversion=1.0.0-SNAPSHOT

The bundles all have some similar properties, like the usage of the ```autoInstallBundle``` Maven profile and using similar plugins.

The versions of the plugins used are slightly outdated, so we'll using the latest stable. At the time of writing, the ```org.apache.felix.maven-bundle-plugin-2.4.0``` and ```maven-sling-plugin-2.1.0``` are available.

The the parent pom, the introduction of properties is useful to share the same set of configuration values.

    <properties>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      <sling.user>admin</sling.user>
      <sling.password>admin</sling.password>
      <sling.port>8080</sling.port>
    </properties>

Those properties will be used in all ```autoinstallBundle``` profiles, resulting in the following profile.

    <profile>
      <id>autoInstallBundle</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.sling</groupId>
            <artifactId>maven-sling-plugin</artifactId>
            <version>2.1.0</version>
            <executions>
              <execution>
                <id>install-bundle</id>
                <goals>
                  <goal>install</goal>
                </goals>
                <configuration>
                  <slingUrl>http://localhost:${sling.port}/system/console</slingUrl>
                  <user>${sling.user}</user>
                  <password>${sling.password}</password>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>

This makes the code more flexible and clean and open for changes. The next thing to be added to ```${project.basedir}/pom.xml``` are the dependencies. Since the parent does not contain any code, it will reference to the dependencies in the ```dependencyManagement```. This will ensure having all modules referencing the same versions.

    <dependencyManagement>
      <dependencies>
        <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>servlet-api</artifactId>
          <version>2.5</version>
        </dependency>
        <dependency>
          <groupId>javax.jcr</groupId>
          <artifactId>jcr</artifactId>
          <version>2.0</version>
        </dependency>
        <dependency>
          <groupId>org.apache.sling</groupId>
          <artifactId>org.apache.sling.api</artifactId>
          <version>2.5.0</version>
        </dependency>
        <dependency>
          <groupId>org.apache.sling</groupId>
          <artifactId>org.apache.sling.scripting.api</artifactId>
          <version>2.1.4</version>
        </dependency>
        <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-api</artifactId>
          <version>1.7.5</version>
        </dependency>
        <dependency>
          <groupId>org.apache.felix</groupId>
          <artifactId>org.apache.felix.scr</artifactId>
          <version>1.8.2</version>
        </dependency>
        <dependency>
          <groupId>org.apache.felix</groupId>
          <artifactId>org.apache.felix.scr.annotations</artifactId>
          <version>1.9.8</version>
          <scope>provided</scope>
        </dependency>
        <dependency>
          <groupId>org.codehaus.groovy</groupId>
          <artifactId>groovy-all</artifactId>
          <version>1.8.2</version>
        </dependency>
      </dependencies>
    </dependencyManagement>

These are the dependencies used in the project. ```${project.basedir}/pet-clinic-core/pom.xml``` and ```${project.basedir}/pet-clinic-ui/pom.xml``` will get all dependencies with the scope ```provided```. Since the bundles are already deployed to the running Sling instance, it would be wrong to compile them into the resulting bundles.

    <dependencies>
      <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>javax.jcr</groupId>
        <artifactId>jcr</artifactId>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>org.apache.sling</groupId>
        <artifactId>org.apache.sling.api</artifactId>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>org.apache.sling</groupId>
        <artifactId>org.apache.sling.scripting.api</artifactId>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>org.apache.felix</groupId>
        <artifactId>org.apache.felix.scr</artifactId>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>org.apache.felix</groupId>
        <artifactId>org.apache.felix.scr.annotations</artifactId>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>org.codehaus.groovy</groupId>
        <artifactId>groovy-all</artifactId>
      </dependency>
    </dependencies>

The last piece to be added is the public Adobe Maven repository to the ```${project.basedir}/pom.xml```. It is used to resolve some of the Sling dependencies.

> This is actually a cheap trick, since I ran into some troubles when running the build .

    <profiles>
      <profile>
        <id>adobe-public</id>
        <activation>
          <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
          <releaseRepository-Id>adobe-public-releases</releaseRepository-Id>
          <releaseRepository-Name>Adobe Public Releases</releaseRepository-Name>
          <releaseRepository-URL>http://repo.adobe.com/nexus/content/groups/public</releaseRepository-URL>
        </properties>
        <repositories>
          <repository>
            <id>adobe-public-releases</id>
            <name>Adobe Public Repository</name>
            <url>http://repo.adobe.com/nexus/content/groups/public</url>
            <releases>
              <enabled>true</enabled>
              <updatePolicy>never</updatePolicy>
            </releases>
            <snapshots>
              <enabled>true</enabled>
            </snapshots>
          </repository>
        </repositories>
        <pluginRepositories>
          <pluginRepository>
            <id>adobe-public-releases</id>
            <name>Adobe Public Repository</name>
            <url>http://repo.adobe.com/nexus/content/groups/public</url>
            <releases>
              <enabled>true</enabled>
              <updatePolicy>never</updatePolicy>
            </releases>
            <snapshots>
              <enabled>true</enabled>
            </snapshots>
          </pluginRepository>
        </pluginRepositories>
      </profile>
    </profiles>

That's it for part one.