## Data access and initial content.

Apache Sling's persistence layer is based on the Java Content Respository (JCR). It uses the JackRabbit implemention of specification. The JCR data store handles unstructured as well as structured data. In this guide we'll focus on the unstructured part. This allows maximum flexibility in the process of developing, avoiding a schema while being able to change the data when required.

### About initial content creation and content loading.

Having a running Sling instance, creating content is fairly easy. The most handy way would be a cURL POST from the command line to server.

    #!/bin/sh
    curl -FfirstName="George" \
         -FlastName="Franklin" \
         -Faddress="Madison, 110 W. Liberty St." \
         -Fcity="Madison" \
         -Ftelephone="6085551023" \
         http://localhost:8080/sling/content/owners/georgefranklin 

This will create or modify the resource ```/sling/content/owners/georgefranklin```, setting the properties passed accordingly. When working on a application, it would be kind of troublesome not having a place to store the content to make demo content available for each developer. And it would also be very convenient to have initial content provided as well, when the application starts.

Sling provides the way to load content from JSON or XML. The an example for the XML syntax looks like

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

      <!-- child nodes -->

    </node>

whereas the JSON equivalent looks a bit more expressive

    {
      "jcr:primaryType": "nt:unstructured",
      "firstName": "George",
      "lastName": "Franklin",
      "address": "110 W. Liberty St.",
      "city": "Madison",
      "telephone": "6085551023",
      "pets": {
        "jcr:primaryType": "nt:unstructured",
        "jenny": {
          "jcr:primaryType": "nt:unstructured",
          "sling:resourceType": "petclinic/components/pages/owners/pets",
          "name": "Jenny",
          "typeId": "/sling/content/petTypes/hamster",
          "birthDate": "08/02/2014"
        },
        "leo": {
          "jcr:primaryType": "nt:unstructured",
          "sling:resourceType": "petclinic/components/pages/owners/pets",
          "name": "Leo",
          "typeId": "/sling/content/petTypes/cat",
          "birthDate": "07/09/2000"
        },
        "shaka": {
          "jcr:primaryType": "nt:unstructured",
          "sling:resourceType": "petclinic/components/pages/owners/pets",
          "name": "Shaka",
          "typeId": "/sling/content/petTypes/dog",
          "birthDate": "10/03/2000"
        }
      }
    }

This makes it fairly easy to provide accessible data stored in the source code repository. There is already demo as well as initial content provided the repository, deploying it is the last piece of information to know.

    mvn clean install -PautoInstallBundle

will deploy any bundle in the project from the bundles directory, calling it from the parent will deploy the all bundles.

The structure of the initial and demo content provided looks like

    project
    |-- pom.xml
    `-- src
        `-- main
            `-- resources
                `-- SLING-INF
                    |-- content
                    |   `-- petclinic
                    |       `-- en
                    |           |-- owners.xml
                    |           |-- pets.xml
                    |           |-- petTypes.xml
                    |           |-- specialities.xml
                    |           |-- vets.xml
                    |           `-- vets.xml
                    `-- sling
                        `-- content
                            |-- owners
                            |   |-- georgefranklin.json
                            |   |-- jeancoleman.json
                            |   |-- jeffreyblack.json
                            |   |-- mariaescobito.json
                            |   |-- petermctavish.json
                            |   `-- robertschroeder.json
                            |-- petTypes
                            |   |-- bird.xml
                            |   |-- cat.xml
                            |   |-- dog.xml
                            |   |-- hamster.xml
                            |   |-- lizard.xml
                            |   `-- snake.xml
                            |-- specialities
                            |   |-- dentistry.xml
                            |   |-- radiology.xml
                            |   `-- surgery.xml
                            `-- veterinarians
                                |-- helenleary.json
                                |-- henrystevens.json
                                |-- jamescarter.json
                                |-- lindadouglas.json
                                |-- rafaelortega.json
                                `-- sharonjenkins.json

There are already some design decision made. Every resource under ```/content/petclinic/en``` is a page with a destinct resource type. The directory structure itself is following some Sling confentions like starting with ```/content``` and ending with the language ```/en```. Information about localization can be found [here](http://sling.apache.org/site/internationalization-support.html). Everything under ```/sling/content``` is content in the terms of data.

### Owners, pets and types

Looking into one of the JSON files, not only data about an owner can be found, but about their pets as well. There can't be a pet without an owner. Thats why a pet resource in a child of the owner resource and visits are children of the pet. The type is a different kind of resource though. That's why the pet types hava destinct location in the repository. Similar relationships can be seen between veterinarians and specialities. The consistent referencing of non-child resource is done via paths. A path is the path to the resource so it be easily resolved. The design might be very pragmatic, but serves pretty well for the scope of the guide.


http://sling.apache.org/site/internationalization-support.html 



