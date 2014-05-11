import org.apache.sling.api.resource.ValueMap

def suffix = request.getRequestPathInfo().getSuffix()
def resourceResolver = resource.getResourceResolver()
def veterinarianResource = resourceResolver.getResource(suffix);
def veterinarianProps = veterinarianResource.adaptTo(ValueMap.class);
def specialitiesResource = resourceResolver.getResource('/sling/content/specialities')

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Edit Veterinarian')
        }
      }
      form(class: 'ui form', role: 'form', action: "${veterinarianResource.getPath()}", method: 'POST') {
        div(class: 'field') {
          label(for: 'name', 'First Name:')
          input(id: 'firstName', name: 'firstName', type: 'text', placeholder: 'First Name',
              value: "${veterinarianProps.get('firstName')}")
        }
        div(class: 'field') {
          label(for: 'name', 'Last Name:')
          input(id: 'lastName', name: 'lastName', type: 'text', placeholder: 'Last Name',
              value: "${veterinarianProps.get('lastName')}")
        }
        div(class: 'grouped inline fields ui segment') {
          specialitiesResource.listChildren().each { specialityResource ->
            def specialityProps = specialityResource.adaptTo(ValueMap.class)
            div(class: 'field') {
              div(class: 'ui checkbox') {
                if (specialityResource.getPath() in veterinarianProps.get('specialities'))
                  input(type: 'checkbox', name: 'specialities', value: "${specialityResource.getPath()}"
                      , checked: 'checked')
                else
                  input(type: 'checkbox', name: 'specialities', value: "${specialityResource.getPath()}")
                label("${specialityProps.get('name')}")
              }
            }
          }
        }
        input(type: 'hidden', name: 'specialities@TypeHint', value: 'String[]')
        input(type: 'hidden', name: "specialities@Delete", value: 'delete specialities')
        input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/components/pages/veterinarians/veterinarian')
        input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
        input(type: 'hidden', name: '_charset_', value: 'UTF-8')
        button(type: 'submit', class: 'ui blue submit button', 'Save')
      }
    }
  }
}

sling.include(resource, 'petclinic/components/footer')