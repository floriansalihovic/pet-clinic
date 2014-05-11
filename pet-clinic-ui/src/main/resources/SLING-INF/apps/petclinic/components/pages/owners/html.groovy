import org.apache.sling.api.resource.ValueMap

def resourceResolver = resource.getResourceResolver()
def ownersResource = resourceResolver.getResource('/sling/content/owners')

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Owners')
        }
        div(class: 'nine wide column') {
          div(class: 'ui icon input', style: 'float: right; margin-left: 1em;') {
            form(method: 'GET') {
              input(name: 'q', type: 'text', placeholder: 'Find owners...')
            }
            i(class: 'circular search icon', '')
          }
          a(href: "${resource.getPath()}.add.html", class: 'ui button green',
              style: 'float: right; margin-left: 1em;', 'Add Owner')
        }
      }
      table(class: 'ui table segment') {
        thead {
          tr {
            th('Name'); th('City'); th('Address'); th('Telephone'); th('Pets')
          }
        }
        tbody {
          ownersResource.listChildren().each { ownerResource ->
            def ownerProps = ownerResource.adaptTo(ValueMap.class)
            tr {
              td {
                a(href: "${resource.getPath()}.detail.html${ownerResource.getPath()}",
                    "${ownerProps.get('firstName')} ${ownerProps.get('lastName')}")
              }
              td(ownerProps.get('city'))
              td(ownerProps.get('address'))
              td(ownerProps.get('telephone'))
              td {
                def petsResource = ownerResource.getChild('pets')
                if (petsResource) {
                  div {
                    petsResource.listChildren().each { petResource ->
                      def petProps = petResource.adaptTo(ValueMap.class)
                      span(class: 'ui small label teal', "${petProps.get('name')}")
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
}

sling.include(resource, 'petclinic/components/footer')
