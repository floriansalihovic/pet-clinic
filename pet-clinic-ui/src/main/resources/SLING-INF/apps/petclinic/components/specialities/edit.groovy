import org.apache.sling.api.resource.ValueMap

def suffix = request.getRequestPathInfo().getSuffix()
def resourceResolver = resource.getResourceResolver()
def specialityResource = resourceResolver.getResource(suffix);
def specialityProps = specialityResource.adaptTo(ValueMap.class);

markupBuilder.form(class: 'ui form', role: 'form', action: "${specialityResource.getPath()}", method: 'POST') {
  div(class: 'field') {
    label(for: 'name', 'Name:')
    input(id: 'name', name: 'name', type: 'text', placeholder: 'Name', value: "${specialityProps.get('name')}")
  }
  input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/speciality')
  input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
  input(type: 'hidden', name: '_charset_', value: 'UTF-8')
  button(type: 'submit', class: 'ui blue submit button', 'Save')
}
