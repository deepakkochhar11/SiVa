package ee.openeid.siva.validation.document.report;

import lombok.Data;

import java.util.List;

@Data
public class ValidationConclusion {

    private Policy policy;

    private String validationTime;

    private String signatureForm;

    private List<ValidationWarning> validationWarnings;

    private ValidatedDocument validatedDocument;

    private String validationLevel;

    private List<SignatureValidationData> signatures;

    private Integer validSignaturesCount = 0;

    private Integer signaturesCount = 0;

    private List<TimeStampTokenValidationData> timeStampTokens;
}