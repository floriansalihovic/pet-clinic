import org.apache.sling.api.resource.ValueMap

// the suffix pointing to an pet resource
def suffix = request.getRequestPathInfo().getSuffix()
// the user session based resource resolver
def resourceResolver = resource.getResourceResolver()
// the pet resource
def specialityResource = resourceResolver.getResource(suffix);
// the pet resource
def specialityProps = specialityResource.adaptTo(ValueMap.class);

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Edit Speciality')
        }
      }
      form(class: 'ui form', role: 'form', action: "${specialityResource.getPath()}", method: 'POST') {
        div(class: 'field') {
          label(for: 'name', 'Name:')
          input(id: 'name', name: 'name', type: 'text', placeholder: 'Name', value: "${specialityProps.get('name')}")
        }
        input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/components/pages/specialities/speciality')
        input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
        input(type: 'hidden', name: '_charset_', value: 'UTF-8')
        button(type: 'submit', class: 'ui blue submit button', 'Save')
      }
    }
  }
}

sling.include(resource, 'petclinic/components/footer')