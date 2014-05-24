import org.apache.sling.api.resource.ValueMap

def resourceResolver = resource.getResourceResolver()
def veterinariansResource = resourceResolver.getResource('/sling/content/veterinarians')
def specialitiesResource = resourceResolver.getResource('/sling/content/specialities')

markupBuilder.form(class: 'ui form', role: 'form', action: "${veterinariansResource.getPath()}/*", method: 'POST') {
  div(class: 'field') {
    label(for: 'name', 'First Name:')
    input(id: 'firstName', name: 'firstName', type: 'text', placeholder: 'First Name')
  }
  div(class: 'field') {
    label(for: 'name', 'Last Name:')
    input(id: 'lastName', name: 'lastName', type: 'text', placeholder: 'Last Name')
  }
  div(class: 'grouped inline fields ui segment') {
    specialitiesResource.listChildren().each { specialityResource ->
      def specialityProps = specialityResource.adaptTo(ValueMap.class)
      div(class: 'field') {
        div(class: 'ui checkbox') {
          input(type: 'checkbox', name: 'specialities', value: "${specialityResource.getPath()}")
          label("${specialityProps.get('name')}")
        }
      }
    }
  }
  input(type: 'hidden', name: 'specialities@TypeHint', value: 'String[]')
  input(type: 'hidden', name: "specialities@Delete", value: 'delete specialities')
  input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/veterinarian')
  input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
  input(type: 'hidden', name: '_charset_', value: 'UTF-8')
  button(type: 'submit', class: 'ui blue submit button', 'Save')
}
