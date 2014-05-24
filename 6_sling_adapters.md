## Sling Adapters

Sling adapters can be used to transform an instance of one class to an instance of another class.

    package io.github.floriansalihovic.petclinic.adapters;
    
    import io.github.floriansalihovic.petclinic.search.*;
    import io.github.floriansalihovic.petclinic.search.impl.*;
    import org.apache.felix.scr.annotations.*;
    import org.apache.sling.api.*;
    import org.apache.sling.api.adapter.*;
    import org.apache.sling.api.request.*;
    import org.slf4j.*;
    
    /**
     * Adapter factory providing an adapter for SlingHttpServletRequests to be adapted to OwnerSearchServices
     */
    @Component
    @Service
    @Properties({
            @Property(name = AdapterFactory.ADAPTER_CLASSES,
                    value = {"io.github.floriansalihovic.petclinic.search.OwnerSearchService"}),
            @Property(name = AdapterFactory.ADAPTABLE_CLASSES,
                    value = {"org.apache.sling.api.SlingHttpServletRequest"})})
    public class OwnerSearchServiceAdapterFactory implements AdapterFactory {
    
        /**
         * The default logger.
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(OwnerSearchServiceAdapterFactory.class);
    
        @SuppressWarnings("unchecked")
        @Override
        public <AdapterType> AdapterType getAdapter(Object adaptable,
                                                    Class<AdapterType> type) {
            LOGGER.info("getAdapter {} for {}", adaptable, type);
            if (type != OwnerSearchService.class) {
                return null;
            } else if (adaptable instanceof SlingHttpServletRequest) {
                return (AdapterType) this.adaptTo((SlingHttpServletRequest) adaptable);
            } else {
                LOGGER.warn("getAdapter: Unexpected call with unsupported type {}; cannot adapt {}", type, adaptable);
            }
    
            return null;
        }
    
        /**
         * The adapting method creating an OwnerSearchService instance from a SlingHttpServletRequest.
         *
         * @param request
         *         The request to be adapted to an OwnerSearchService instance.
         *
         * @return The OwnerSearchService instance created.
         */
        private OwnerSearchService adaptTo(SlingHttpServletRequest request) {
            final RequestParameter p = request.getRequestParameter("q");
            final OwnerSearchService service = new OwnerSearchServiceImpl();
    
            if (null != p) {
                service.setQuery(p.getString());
            }
    
            service.setResourceResolver(request.getResourceResolver());
    
            return service;
        }
    }
