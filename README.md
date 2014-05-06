# The Sling PetClinic Application

This application is intended to provide a fast and easy access to Apache Sling using Groovy as a scripting language.

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