import org.apache.sling.api.resource.ValueMap

def suffix = request.getRequestPathInfo().getSuffix()
def resourceResolver = resource.getResourceResolver()
def petTypeResource = resourceResolver.getResource(suffix);
def petTypeProps = petTypeResource.adaptTo(ValueMap.class);

markupBuilder.form(class: 'ui form', role: 'form', action: "${petTypeResource.getPath()}", method: 'POST') {
  div(class: 'field') {
    label(for: 'name', 'Name:')
    input(id: 'name', name: 'name', type: 'text', placeholder: 'Name', value: "${petTypeProps.get('name')}")
  }
  input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/components/pages/petTypes/petType')
  input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
  input(type: 'hidden', name: '_charset_', value: 'UTF-8')
  button(type: 'submit', class: 'ui blue submit button', 'Save')
}
