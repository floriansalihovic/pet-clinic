import org.apache.sling.api.resource.ValueMap

def resourceResolver = resource.getResourceResolver()
def petTypesResource = resourceResolver.getResource('/sling/content/petTypes')

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Pet Types')
        }
        div(class: 'nine wide column') {
          a(href: "${resource.getPath()}.add.html", class: 'ui button green',
              style: 'float: right; margin-left: 1em;', 'Add Pet Type')
        }
      }
      table(class: 'ui table segment') {
        thead {
          tr {
            th('Name')
          }
        }
        tbody {
          petTypesResource.listChildren().each { petTypeResource ->
            def petTypeProps = petTypeResource.adaptTo(ValueMap.class)
            tr {
              td {
                a(href: "${resource.getPath()}.edit.html${petTypeResource.getPath()}", "${petTypeProps.get('name')}")
              }
            }
          }
        }
      }
    }
  }
}

sling.include(resource, 'petclinic/components/footer')