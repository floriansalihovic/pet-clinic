import org.apache.sling.api.resource.ValueMap

def suffix = request.getRequestPathInfo().getSuffix()
def resourceResolver = resource.getResourceResolver()
def ownerResource = resourceResolver.getResource(suffix)
def ownerProps = ownerResource.adaptTo(ValueMap.class)
def petsResource = ownerResource.getChild('pets')

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
          a(href: "${resource.getPath()}.edit.html${ownerResource.getPath()}",
              class: 'small ui right floated button blue',
              style: 'float: right; margin-left: 1em;', 'Edit Owner')
        }
      }
      table(class: 'ui table segment') {
        tbody {
          tr {
            th 'Name'
            td {
              i(class: 'user icon', '')
              span "${ownerProps.get('firstName')} ${ownerProps.get('lastName')}"
            }
          }
          tr {
            th 'Address'
            td {
              i(class: 'map marker icon', '')
              span "${ownerProps.get('city')}, ${ownerProps.get('address')}"
            }
          }
          tr {
            th 'Telephone'
            td {
              i(class: 'phone icon', '')
              span "${ownerProps.get('telephone')}"
            }
          }
        }
      }
      div(class: 'ui grid') {
        div(class: 'twelve wide column') {
          h2(class: 'ui header', 'Pets and Visits')
        }
        div(class: 'four wide column') {
          a(class: 'small ui right floated button green', href: "/content/petclinic/en/pets.add.html${suffix}", 'Add Pet')
        }
      }
      table(class: 'ui table segment') {
        thead {
          tr {
            th(style: 'width: 25%', 'Name')
            th(style: 'width: 25%', 'Type')
            th(style: 'width: 25%', 'Visit Date')
            th(style: 'width: 25%', 'Description')
          }
        }
        tbody {
          if (petsResource) {
            petsResource.listChildren().each { petResource ->
              tr {
                def petProps = petResource.adaptTo(ValueMap.class)
                def petTypeResource = resourceResolver.getResource(petProps.get('typeId'))
                def petTypeProps = petTypeResource.adaptTo(ValueMap.class)
                td(style: 'vertical-align: top') {
                  a(href: "/content/petclinic/en/pets.edit.html${petResource.getPath()}", "${petProps.get('name')}")
                }
                td(style: 'vertical-align: top', "${petTypeProps.get('name')}")
                td(colspan: '2', style: 'padding: 0') {
                  table(class: 'ui table small', style: 'width: 100%') {
                    tbody {
                      def visitsResource = petResource.getChild('visits')
                      if (visitsResource) {
                        visitsResource.listChildren().each { visitResource ->
                          def visitProps = visitResource.adaptTo(ValueMap.class)
                          tr {
                            td(style: 'width:50%') {
                              a(href: "/content/petclinic/en/visits.edit.html${visitResource.getPath()}",
                                  "${visitProps.get('date')}")
                            }
                            td(style: 'width:50%', "${visitProps.get('description')}")
                          }
                        }
                      }
                      tr {
                        td(colspan: '2') {
                          a(class: 'mini ui green button',
                              href: "/content/petclinic/en/visits.add.html${petResource.getPath()}", 'Add Visit')
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
  }
}

sling.include(resource, 'petclinic/components/footer')
