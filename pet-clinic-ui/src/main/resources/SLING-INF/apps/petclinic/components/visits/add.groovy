import org.apache.sling.api.resource.ValueMap

def suffix = request.getRequestPathInfo().getSuffix()
def resourceResolver = resource.getResourceResolver()
def petResource = resourceResolver.getResource(suffix)
def petProps = petResource.adaptTo(ValueMap.class)
def ownerResource = petResource.getParent().getParent()
def ownerProps = ownerResource.adaptTo(ValueMap.class)

markupBuilder.form(class: 'ui form', role: 'form', action: "${suffix}/visits/*", method: 'POST') {
  div(class: 'field') {
    label(for: 'firstName', 'First Name:')
    input(id: 'firstName', name: 'firstName', type: 'text', readonly: 'true',
        value: "${ownerProps.get('firstName')} ${ownerProps.get('lastName')}")
  }
  div(class: 'field') {
    label(for: 'name', 'Name:')
    input(id: 'petName', name: 'petName', type: 'text', placeholder: 'Name', readonly: 'true',
        value: "${petProps.get('name')}")
  }
  div(class: 'field') {
    label(for: 'birthDate', 'Birth Date:')
    input(id: 'birthDate', name: 'birthDate', type: 'text', placeholder: 'dd/MM/yy', readonly: 'true',
        value: "${petProps.get('birthDate')}")
  }
  div(class: 'field') {
    label(for: 'date', 'Date:')
    input(id: 'date', name: 'date', type: 'text', placeholder: 'dd/MM/yy')
  }
  div(class: 'field') {
    label(for: 'description', 'Description:')
    input(id: 'description', name: 'description', type: 'text', placeholder: 'Description')
  }
  input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/visit')
  input(type: 'hidden', name: ':nameHint', value: 'visit')
  input(type: 'hidden', name: ':redirect',
      value: "/content/petclinic/en/owners.detail.html${ownerResource.getPath()}")
  input(type: 'hidden', name: '_charset_', value: 'UTF-8')
  button(type: 'submit', class: 'ui blue submit button', 'Save')
}
