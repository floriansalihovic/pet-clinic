## Data access and content modification.

The previous chapter was introductionary and provides some important basics towards content bundles. Actual content creation or modification in a Application is mostly done because of user interactions. Adding new owners, editing existing ones or providing new specialities for veterinarians is often done via forms. Which data the form holds is quite often done via forms, which are prefilled if data should be edited.

    <form class="ui form" role="form" action="/sling/content/owners/*" method="POST">
      <div class="field">
        <label for="firstName">First Name:</label>
        <input id="firstName" name="firstName" type="text" placeholder="First Name">
      </div>
      <div class="field">
        <label for="lastName">Last Name:</label>
        <input id="lastName" name="lastName" type="text" placeholder="Last Name">
      </div>
      <div class="field">
        <label for="address">Address:</label>
        <input id="address" name="address" type="text" placeholder="Address">
      </div>
      <div class="field">
        <label for="city">City:</label>
        <input id="city" name="city" type="text" placeholder="City">
      </div>
      <div class="field">
        <label for="telephone">Telephone:</label>
        <input id="telephone" name="telephone" type="text" placeholder="Telephone">
      </div>
      <input type="hidden" name="sling:resourceType" value="petclinic/owner">
      <input type="hidden" name=":redirect" value="/content/petclinic/en/owners.html">
      <input type="hidden" name="_charset_" value="UTF-8">
      <button type="submit" class="ui blue submit button">Save</button>
    </form>

This for example is the form for adding an owner similar to the demo content owners already available. A form for editing an owner would looks slightly different.

    <form class="ui form" role="form" action="/sling/content/owners/georgefranklin" method="POST">
      <div class="field">
        <label for="firstName">First Name:</label>
        <input id="firstName" name="firstName" type="text" placeholder="First Name" value="George">
      </div>
      <div class="field">
        <label for="lastName">Last Name:</label>
        <input id="lastName" name="lastName" type="text" placeholder="Last Name" value="Franklin">
      </div>
      <div class="field">
        <label for="address">Address:</label>
        <input id="address" name="address" type="text" placeholder="Address" value="110 W. Liberty St.">
      </div>
      <div class="field">
        <label for="city">City:</label>
        <input id="city" name="city" type="text" placeholder="City" value="Madison">
      </div>
      <div class="field">
        <label for="telephone">Telephone:</label>
        <input id="telephone" name="telephone" type="text" placeholder="Telephone" value="6085551023">
      </div>
      <input type="hidden" name="sling:resourceType" value="petclinic/owner">
      <input type="hidden" name=":redirect" value="/content/petclinic/en/owners.html">
      <input type="hidden" name="_charset_" value="UTF-8">
      <button type="submit" class="ui blue submit button">Save</button>
    </form>

The differnces are easy to spot. The actions point to different paths and therefor resources and the values of the input elements are set as well. And that's basically all there is to know about creating content creation or modification.

1. Creating a form or AJAX request to send data via POST.
2. Reference a resource via path.
3. Set properties for the resource.
4. Provide additional information needed (typehints, redirect information and so on).

That sound easy and it is. Of course there is always more to know and for a deep dive check out the part [Manipulating Content](http://sling.apache.org/documentation/bundles/manipulating-content-the-slingpostservlet-servlets-post.html) on the project's documentation. The key component is the SlingPostServlet. The servlet won't be dicussed in this guide. It is simply too powerful and would break the guide's scope.

Accessing the data is somewhat tricky though. This relies on the inner workings of Sling. Resolving resources in Sling is done with the ```org.apache.sling.api.resource.ResourceResolver```. Using ```ResourceResolver#getResource``` and passing a path will return the desired resource or ```null```, if the resource does not exist.

When working with Groovy, which is one of the guide's objectives, the Sling script engine is involved. The arguments for the script engines contain the relevant objects to work with. For our script, knowing about the current ```resource``` and the ```SlingScriptHelper``` is basically enough. A ```ResourceResolver``` will accessed simply by using ```Resource#getResourceResolver()```.

A basic groovy script for views will therefor look like

    def resourceResolver = resource.getResourceResolver()
    der markupBuilder = new groovy.xml.MarkupBuilder(out)
    markupBuilder.html {
      head {
        title('The srcipt')
      }
      body {
        h1('Hello world.')
      }
    }





