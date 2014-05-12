import org.apache.sling.api.resource.ValueMap

def suffix = request.getRequestPathInfo().getSuffix()
def resourceResolver = resource.getResourceResolver()
def ownerResource = resourceResolver.getResource(suffix)
def ownerProps = ownerResource.adaptTo(ValueMap.class)

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Add Owner')
        }
      }
      sling.include(resource, 'petclinic/components/owners')
    }
  }
}

sling.include(resource, 'petclinic/components/footer')