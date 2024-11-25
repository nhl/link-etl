package com.nhl.link.move.runtime.cayenne;

import com.nhl.link.move.extractor.Extractor;
import com.nhl.link.move.extractor.model.ExtractorModel;
import com.nhl.link.move.runtime.extractor.IExtractorFactory;

/**
 * Factory for {@link CayenneExtractor}
 *
 * @since 3.0
 */
public class CayenneExtractorFactory implements IExtractorFactory<CayenneConnector> {

    private static final String CAYENNE_EXTRACTOR_TYPE = "cayenne";
    private static final String SOURCE_ENTITY_PROPERTY = "extractor.cayenne.entity";

    @Override
    public String getExtractorType() {
        return CAYENNE_EXTRACTOR_TYPE;
    }

    @Override
    public Class<CayenneConnector> getConnectorType() {
        return CayenneConnector.class;
    }

    @Override
    public Extractor createExtractor(CayenneConnector connector, ExtractorModel model) {
        String sourceEntity = model.getPropertyValue(SOURCE_ENTITY_PROPERTY);
        return new CayenneExtractor(connector.sharedContext(), model.getAttributes(), sourceEntity);
    }
}
