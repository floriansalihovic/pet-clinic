# The Sling PetClinic Application

This application is intended to provide a fast and easy access to Apache Sling using Groovy as a scripting language.

> This tutorial aims to provide an example of how the pieces are connected. Making it easier to start developing
> with Sling.




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
         -Fcity="Madison" \
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

test data is added. The archetype's initial content and script are kept in order to have reference data throughout the tutorial -
until dedicated render components are provided. To deploy the data properly, the ```SLING-INF/sling/content/owners```
directory has to be added to the plugins ```Sling-Initial-Content`` configuration.


    <plugin>
      <groupId>org.apache.felix</groupId>
      <artifactId>maven-bundle-plugin</artifactId>
      <extensions>true</extensions>
      <version>2.4.0</version>
      <configuration>
        <instructions>
          <Sling-Nodetypes>SLING-INF/nodetypes/nodetypes.cnd</Sling-Nodetypes>
          <Sling-Initial-Content>
            SLING-INF/apps;overwrite:=true;uninstall:=true;path:=/apps,
            SLING-INF/content;overwrite:=true;uninstall:=true;path:=/content,
            SLING-INF/sling/content/owners;overwrite:=true;uninstall:=true;path:=/sling/content/owners
          </Sling-Initial-Content>
        </instructions>
      </configuration>
    </plugin>

## Everything is a resource

The mantra of Sling application design is "Everything is a resource". The programming model allows addressing resources
rendered in various ways by providing a resource type. The resource type is used for script resolution, which is basically
the process of determining the correct way to render a resource.

In the PetClinic application, different types of resource are easily identified:

- Pages
- Owners (of pets)
- Pets
- Vetenarians
- Visits

JackRabbit - the underlying content repository (basically the persistence layer) - is an unstructured data store.
There is no schema and those resources can basically be stored at any location. The obvious differentiation made divides
resources in data and resource which render data - associated via resource type. Not going into the resolution in full
depth, the latter resource are stored in ```/libs``` and ```/apps```. The locations can be looked up in the [Apache Felix
OSGi](http://localhost:8080/system/console/jcrresolver) (Sling must be running) console. Those resources are refered to as scripts,
renderers or (view) components. In ```/libs``` standard Sling components are stored, whereas in ```/apps``` application
specific scripts are stored, which can also override components provided in ```/libs``` (by resource type as illustrated
in the following short example).

Having a resource with a resource type of ```owners/owner```, Sling will look into ```/apps/owners/owner``` for any kind
of component used for displaying and if none is found, ```/libs/owners/owner``` will be checked. That is the short story.

The data and the selected scripts for rendering have to be set in a broader context though. When working with data, or
resources in the context of Sling, there are always various resources involved in creating and modifying data.

The outline can be imagined as the following the user wants to create a visit for his pet ar the veterinarian. In order
to do so, he has to register himself and his pet at for a visit at a certain time for a visit at a specific pet clinic
run by a doctor.

So naturally pets and their owners, the vets and so on appear to be resource. But so are the HTML pages requested. Sling
does not differentiate between those subjects in terms of resources - only how the data may be accessed, modified and
displayed.

This tutorial aims to provide an example of how the pieces are connected.

## About resources and resource types in the PetClinic

The application is basically a Sling flavored [Spring PetClinic](http://docs.spring.io/docs/petclinic.html) version based
on the [Kotlin](https://github.com/cheptsov/kotlin-nosql-mongodb-petclinic) port. That means the objectives are set and
a port to Sling can be done without looking too much left and right.

The application provides basically a web interfaces to add and modify everything a pet clinic would need to work with.
As already stated, pages are resources just as entities in the domain data and Sling provides a lot of power for creating
them.

## About pets and html pages

The first resource type used will be ```petclinic/components/pages/owner```. The resource type will be used to render pages, which
are made up of components. The resource type will be applied to four resource placed in ```/content/pet-clinic/en```. This
location can be thought of as the directory used for storing web pages accessible to the user. Resources like


    <node>
      <primaryNodeType>nt:unstructured</primaryNodeType>

      <property>
        <name>sling:resourceType</name>
        <type>String</type>
        <value>petclinic/components/pages/owners</value>
      </property>

      <property>
        <name>jcr:title</name>
        <type>String</type>
        <value>Owners</value>
      </property>

    </node>

The property ```sling:resourceType``` determines the resources type and ```jcr:title``` determines the title of the page.
The ```jcr```prefix derives directly from the underlying Java Content Repository implementation and its mechanics. But
that is nothing important for now - important is only the fact that we have these two properties. As done in the existing
pet clinic applications, similar resources are created for veterinarians, specialities and pet types.

In ```${project.basedir}/src/main/resource/templates``` a bunch of ```.html``` file and assets are placed. In order to test the application, the files can be copied in corresponding the ```pet-clinic-ui``` folder. The ```.html``` files have to get the suffix ```.esp``` for the moment. This is necessary to get the files compiled and applied as a renderer for a resource.

Having demo content in ```/content/petclinic/en``` and renderers now as well as assets in place (everything ```/etc```), ```mvn clean install -PautoInstallBundle``` will deploy the application and makes data accessible. Opening the browser
and visiting [http://localhost:8080/content/petclinic/en/owners.html](http://localhost:8080/content/petclinic/en/owners.html)
will give a first idea about what the application looks like. But there is more.

### Resources, resources types and selectors

Before breathing life into the application, lets revisit a bit of the application's current state - in the running instance.
To isolate the main mechanics used focusing on owners reduces the amount of data to look at.

- ```/content/petclinic/en/owners```
- ```/apps/petclinic/components/pages/owners/add.esp```
- ```/apps/petclinic/components/pages/owners/detail.esp```
- ```/apps/petclinic/components/pages/owners/edit.esp```
- ```/apps/petclinic/components/pages/owners/html.esp```

Those are the relevant files to look at. As already demonstrated,
[http://localhost:8080/content/petclinic/en/owners.html](http://localhost:8080/content/petclinic/en/owners.html) gives a
first look at the applications look. This will actually use ```/apps/petclinic/components/pages/owners/html.esp``` to
render the resource. This is a result of the resource resolution process. Sling takes the path and deconstructs certain
information from it:

- The resource: the resource is located after ```host:port``` until the first separator (```.```) or extension
  (```.html``` in the example's case).
- The resource type (```sling:resourceType```): The resource type is a property of the resource determining the script
 to render it. When setting the resource types, the initial folder/path segment should be omitted.
 ```petclinic/components/pages/owners``` is the resource type of the requested resource in this example.
- The extension: The extension is a way to indicate in which format a resource should be rendered. ```.json```, ```.xml``` and
  ```.html``` are possible native extensions to Sling. Which makes
  [http://localhost:8080/content/petclinic/en/owners.json](http://localhost:8080/content/petclinic/en/owners.json) and
  [http://localhost:8080/content/petclinic/en/owners.xml](http://localhost:8080/content/petclinic/en/owners.xml) valid
  calls as well. In the example html is used.
- Selectors: selectors haven't been discussed yet, but are crucial in understanding Sling. Selectors are indicators which
  determine the actual script being invoked when a resource is addressed. Selectors are placed between the resource and its
  extension, separeted by ```.```.

That's quite a bit to grasp. Working examples look as the following:

- [/content/petclinic/en/owners.json](http://localhost:8080/content/petclinic/en/owners.json)
  Invoking the Sling default json renderer.
- [/content/petclinic/en/owners.xml](http://localhost:8080/content/petclinic/en/owners.xml)
  Invoking the Sling default xml renderer.
- [/content/petclinic/en/owners.html](http://localhost:8080/content/petclinic/en/owners.html)
  Invoking ```/apps/petclinic/components/pages/owners/html.esp```, no selector is used only a extension is given. Since The file name matches the extension, this script is chosen for rendering.
- [/content/petclinic/en/owners.add.html](http://localhost:8080/content/petclinic/en/owners.add.html)
  Selector ```add``` is used, invoking ```/apps/petclinic/components/pages/owners/add.esp```. The file is chosen, because it matches the selector's name.
- [/content/petclinic/en/owners.edit.html](http://localhost:8080/content/petclinic/en/owners.edit.html)
  Selector ```edit``` is used, invoking ```/apps/petclinic/components/pages/owners/edit.esp```. The file is chosen, because it matches the selector's name.
- [/content/petclinic/en/owners.detail.html](http://localhost:8080/content/petclinic/en/owners.detail.html)
  Selector ```detail``` is used, invoking ```/apps/petclinic/components/pages/owners/detail.esp```. The file is chosen, because it matches the selector's name.

These simple illustrate only a bit of the flexibility Sling provides, for more information please check out the following resources:

- [Sling Cheat Sheet @ dev.day.com](http://dev.day.com/content/ddc/blog/2008/07/cheatsheet.html)
- [The Basic @ dev.day.com](http://dev.day.com/docs/en/cq/current/developing/the_basics.html)
- [URL to Script resolution](http://sling.apache.org/documentation/the-sling-engine/url-to-script-resolution.html)

## Working with Groovy in Sling

One objectives of this tutorial is working with Groovy. It is a fun and productive language for the JVM and gains more and more popularity.

Working with Groovy is fairly easy, when starting migrating scripts to Groovy scripts. This will be the first moving being made.

### Excourse: The File System Resource Provider

Everything is a resource - so is the file system. When working on scripts, the File System Resource Provider is a handy tool for short tun around cycles.

At [/system/console/configMgr](http://localhost:8080/system/console/configMgr) all configurations are listed. Add a new configuration for the File System Resource Provider. Set 

- ```Provider Root``` to ```/apps/petclinic```
- ```Filesystem Root``` to ```${project.basedir}/pet-clinic-ui/src/main/resources/SLING-INF/apps/petclinic```. The ```${project.basedir}``` needs to be replaced by the actual location on the hard drive.

The File System Resource Provider provides access to the file system as the name suggests and provides to access files as resources. In our use case, it provides access to our Groovy scripts from the working directory. This simplifies the workflow by editing scripts only in the ide removing continuous deployment of scripts.

## The first Groovy script

The Groovy scripts will be generating HTML markup matching our templates, similar to the way the esp files did. Groovy scripts provide better readability and more sophisticated programming model. Starting with a Groovy script in ```/${project.basedir}/pet-clinic-ui/src/main/resources/SLING-INF/apps/petclinic/components/owners``` names ```html.groovy```. Paste in the following contents:


    def builder = new groovy.xml.MarkupBuilder(out)
    builder.html {
      head {
        meta(charset: 'UTF-8')
        meta(content: 'IE=edge,chrome=1', 'http-equiv': 'X-UA-Compatible');
        meta(name: 'viewport', content: 'width=device-width, initial-scale=1.0')
        link(href: '/etc/clientlibs/petclinic/css/default.css', rel: 'stylesheet')
        link(href: '/etc/clientlibs/petclinic/css/semantic.min.css', rel: 'stylesheet')
        script('', type: 'text/javascript', src: '/etc/clientlibs/petclinic/js/jquery-1.11.0.min.js')
        script('', type: 'text/javascript', src: '/etc/clientlibs/petclinic/js/semantic.min.js')
        script('', type: 'text/javascript', src: '/etc/clientlibs/petclinic/js/form.js')
      }
      body {
        div(class: 'ui menu teal inverted') {
          a(class: 'title item', href: '/content/petclinic/en/owners.html') {
            strong('Pet Clinic')
          }
          a(class: 'item', href: '/content/petclinic/en/owners.html', 'Find Owners')
          a(class: 'item', href: '/content/petclinic/en/vets.html', 'Veterinarians')
          a(class: 'item', href: '/content/petclinic/en/specialities.html', 'Specialities')
          a(class: 'item', href: '/content/petclinic/en/petTypes.html', 'Pet Types')
        }
        div(class: 'container') {
          div(class: 'ui grid') {
            div(class: 'seven wide column') {
              h1(class: 'ui header', 'Owners')
            }
            div(class: 'nine wide column') {
              div(class: 'ui icon input', style: 'float: right; margin-left: 1em;') {
                form(method: 'GET') {
                  input(name: 'q', type: 'text', placeholder: 'Find owners...')
                }
                i(class: 'circular search icon', '')
              }
              // todo: link to owners.add.page.
              a(href: '#', class: 'ui button green', style: 'float: right; margin-left: 1em;', 'Add Owner')
            }
          }
          table(class: 'ui table segment') {
            thead {
              tr {
                th('Name')
                th('City')
                th('Address')
                th('Telephone')
                th('Pets')
              }
            }
            tbody {
              tr {
                td {
                  a(href: '#', 'George Franklin')
                }
                td('Madison')
                td('110 W. Liberty St.')
                td('6085551023')
                td {
                  div {
                    span(class: 'ui small label teal', 'Janny')
                    span(class: 'ui small label teal', 'Leo')
                    span(class: 'ui small label teal', 'Shaka')
                  }
                }
              }
            }
          }
          div(class: 'ui divider')
          // todo: create component.
          div(class: 'ui divided horizontal footer link list') {
            div(class: 'item') {
              mkp.yieldUnescaped '&copy; 2014 Florian Salihovic'
            }
            a(class: 'item', href: 'https://github.com/floriansalihovic/pet-clinic', 'at github');
            div(class: 'item') {
              a(href: 'https://sling.apache.org', target: '_blank', 'Powered by Apache Sling')
            }
            div(class: 'item') {
              a(href: 'http://floriansalihovic.github.io', target: '_blank', 'by me')
            }
          }
        }
      }
    }



When having the File System Resource Provider properly configured simply opening [/content/petclinic/en/owners.html](http://localhost:8080/content/petclinic/en/owners.html#) in the browser will output markup similar to the markup provided by the templates. Without any deployment to the running Sling instance - otherwise a deployment is necessay.

That is static markup generation. To access the data in the repository, only a few lines of code have to be changed. Starting by accessing the resource containing the owners provided by the demo content. This can be done by the resources ResourceResolver.


    // getting the user session based resource resolver
    def resourceResolver = resource.getResourceResolver()
    // accessing the node containing the owners
    ownersResource = resourceResolver.getResource('/sling/content/owners')

The reference to ```ownersResource``` allows accessing child resources via ```Resource#listChildren```, which returns an ```java.util.Iterator```. Groovy's each method allows some easy iteration over the resources, which should be adapted to a ```org.apache.sling.api.resource.ValueMap``` to access the properties. The tbody's content can be easily generated with the following code.

    tbody {
      ownersResource.listChildren().each { resource ->
        properties = resource.adaptTo(ValueMap.class)
        tr {
          td {
            a(href: '#', "${properties.get('firstName')} ${properties.get('lastName')}")
          }
          td(properties.get('city'))
          td(properties.get('address'))
          td(properties.get('telephone'))
          td {
            div {
              span(class: 'ui small label teal', 'Janny')
              span(class: 'ui small label teal', 'Leo')
              span(class: 'ui small label teal', 'Shaka')
            }
          }
        }
      }
    }

The ```import``` of ```org.apache.sling.api.resource.ValueMap``` is the last piece of code to be added on top of the script. Refreshing the browser on [/content/petclinic/en/owners.html](http://localhost:8080/content/petclinic/en/owners.html) will display all owners, only the pets will be renderer staticly for now.

### Adding new Owners

The next script implemented will be used to add an owner. Since the main structure of the Groovy script is similar to the one already created to display all users, only necessary content will be posted. Creating a new file ```add.groovy``` based on the code of ```html.groovy```. Replacing the existing ```div(class: 'container') { ... }``` code with the following will create the proper form for adding new owners.

    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Add Owner')
        }
      }
      form(class: 'ui form', role: 'form', action: '/sling/content/owners/*', method: 'POST') {
        div(class: 'field') {
          label(for: 'firstName', 'First Name:')
          input(id: 'firstName', name: 'firstName', type: 'text', placeholder: 'First Name')
        }
        div(class: 'field') {
          label(for: 'lastName', 'Last Name:')
          input(id: 'lastName', name: 'lastName', type: 'text', placeholder: 'Last Name')
        }
        div(class: 'field') {
          label(for: 'address', 'Address:')
          input(id: 'address', name: 'address', type: 'text', placeholder: 'Address')
        }
        div(class: 'field') {
          label(for: 'city', 'City:')
          input(id: 'city', name: 'city', type: 'text', placeholder: 'City')
        }
        div(class: 'field') {
          label(for: 'telephone', 'Telephone:')
          input(id: 'telephone', name: 'telephone', type: 'text', placeholder: 'telephone')
        }

        input(type:'hidden', name:'sling:resourceType', value:'petclinic/components/pages/owners')
        input(type:'hidden', name:':redirect', value:"${resource.getPath()}.html")
        input(type:'hidden', name:'_charset_', value:'UTF-8')
        button(type: 'submit', class: 'ui blue submit button', 'Save')
      }
    }    


The code created is creating a static form ... that's it. Sling will take of the rest. Accessing the page from with the application is fairly easy. In ```groovy.html``` the link with the text ```Add Owner``` has to get a proper ```href```.

    // todo: link to owners.add.page.
    a(href: "${resource.getPath()}.add.html", class: 'ui button green',
        style: 'float: right; margin-left: 1em;', 'Add Owner')

What was just implemented was a new script for resources with a resource type ```'petclinic/components/pages/owners```. That also the reason why linking to ```"${resource.getPath()}.add.html"``` adding new owners is the task. Working on the same resource with a different seelctor is an easy and effective way to add the "add" page.

Implementing a detail view for an owner resource follows the same rules, except that not only a selector ```detail``` is used, but also a suffix. A suffix, in the Sling context is an information, put in the url after the extension set for the resource. For example the path to a resource can be used as a suffix. This construction may lead to weird looking urls but once the concept is clear, it is a very comfortable way to transport data.

[/content/petclinic/en/owners.detail.html/sling/content/owners/georgefranklin](http://localhost:8080/content/petclinic/en/owners.detail.html/sling/content/owners/georgefranklin) is used in ```html.groovy``` to address the resource with the ```detail``` selector and viewing the details of ```/sling/content/owners/georgefranklin```. The ```a``` containing the detail button is now created with the addition information.

    a(href: "${resource.getPath()}.detail.html${ownerResource.getPath()}",
        "${properties.get('firstName')} ${properties.get('lastName')}")


Accessing an owner resource from the path given by the suffix is fairly easy as well.

    import org.apache.sling.api.resource.ValueMap
    import groovy.xml.MarkupBuilder
    
    // getting the user session based resource resolver
    def resourceResolver = resource.getResourceResolver()
    // accessing the node containing the owners
    ownerResource = resourceResolver.getResource(request.getRequestPathInfo().getSuffix())
    // accessing the owner's properties by adapting the resource to a ValueMap.
    ownerProps = ownerResource.adaptTo(ValueMap.class)

```request.getRequestPathInfo().getSuffix()``` is the way to access the suffix (and therefore the path to the owner resource). The resource's ResourceReolver is used to access the owner resource and that's it basically. In the rest of the script both references are used to resolve properties and address other pages (the page resource with different selectors).
















