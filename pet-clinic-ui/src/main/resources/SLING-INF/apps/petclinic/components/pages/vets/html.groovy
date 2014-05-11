import org.apache.sling.api.resource.ValueMap

def resourceResolver = resource.getResourceResolver()
def veterinariansResource = resourceResolver.getResource('/sling/content/veterinarians')

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Veterinarians')
        }
        div(class: 'nine wide column') {
          a(href: "${resource.getPath()}.add.html", class: 'ui button green',
              style: 'float: right; margin-left: 1em;', 'Add Veterinarian')
        }
      }
      table(class: 'ui table segment') {
        thead {
          tr {
            th('Name'); th('Specialities')
          }
        }
        tbody {
          veterinariansResource.listChildren().each { veterinarianResource ->
            def veterinarianProps = veterinarianResource.adaptTo(ValueMap.class)
            tr {
              td {
                a(href: "${resource.getPath()}.edit.html${veterinarianResource.getPath()}",
                    "${veterinarianProps.get('firstName')} ${veterinarianProps.get('lastName')}")
              }
              td {
                div {
                  def specialities = veterinarianProps.get('specialities') ?: []
                  specialities.each { path ->
                    def specialityProps = resourceResolver.getResource(path).adaptTo(ValueMap.class)
                    span(class: 'ui small teal label', specialityProps.get('name'))
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

sling.include(resource, 'petclinic/components/footer')