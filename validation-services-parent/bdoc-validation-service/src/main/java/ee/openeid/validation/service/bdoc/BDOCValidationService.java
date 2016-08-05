package ee.openeid.validation.service.bdoc;

import ee.openeid.siva.validation.document.ValidationDocument;
import ee.openeid.siva.validation.document.report.QualifiedReport;
import ee.openeid.siva.validation.exception.MalformedDocumentException;
import ee.openeid.siva.validation.service.ValidationService;
import ee.openeid.validation.service.bdoc.report.BDOCQualifiedReportBuilder;
import ee.openeid.validation.service.bdoc.signature.policy.BDOCConfigurationService;
import eu.europa.esig.dss.DSSException;
import org.apache.commons.lang.StringUtils;
import org.digidoc4j.Configuration;
import org.digidoc4j.Container;
import org.digidoc4j.ContainerBuilder;
import org.digidoc4j.exceptions.DigiDoc4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

@Service
public class BDOCValidationService implements ValidationService {
    private static final Logger logger = LoggerFactory.getLogger(BDOCValidationService.class);

    private static final String CONTAINER_TYPE_DDOC = "DDOC";

    private BDOCConfigurationService configurationService;

    @Override
    public QualifiedReport validateDocument(ValidationDocument validationDocument) {
        Container container;
        try {
            container = createContainer(validationDocument);
        } catch (DigiDoc4JException | DSSException e) {
            logger.error("Unable to create container from validation document", e);
            throw new MalformedDocumentException(e);
        }
        verifyContainerTypeNotDDOC(container.getType());

        container.validate();
        Date validationTime = new Date();
        return new BDOCQualifiedReportBuilder(container, validationDocument.getName(), validationTime).build();
    }

    private Container createContainer(ValidationDocument validationDocument) {
        InputStream containerInputStream = new ByteArrayInputStream(validationDocument.getBytes());

        Configuration configuration = configurationService.loadConfiguration(validationDocument.getSignaturePolicy());
        return ContainerBuilder.aContainer()
                .fromStream(containerInputStream)
                .withConfiguration(configuration)
                .build();
    }

    private void verifyContainerTypeNotDDOC(String containerType) {
        if (StringUtils.equalsIgnoreCase(containerType, CONTAINER_TYPE_DDOC)) {
            logger.error("DDOC container passed to BDOC validator");
            throw new MalformedDocumentException();
        }
    }

    @Autowired
    public void setConfigurationService(BDOCConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
