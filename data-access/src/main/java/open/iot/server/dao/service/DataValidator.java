package open.iot.server.dao.service;

import com.fasterxml.jackson.databind.JsonNode;
import open.iot.server.common.data.BaseData;
import open.iot.server.dao.exception.DataValidationException;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public abstract class DataValidator<D extends BaseData<?>> {

    private static final Logger log = LoggerFactory.getLogger("DataValidator");

    private static EmailValidator emailValidator = EmailValidator.getInstance();

    public void validate(D data) {
        try {
            if (data == null) {
                throw new DataValidationException("Data object can't be null!");
            }
            validateDataImpl(data);
            if (data.getId() == null) {
                validateCreate(data);
            } else {
                validateUpdate(data);
            }
        } catch (DataValidationException e) {
            log.error("Data object is invalid: [{}]", e.getMessage());
            throw e;
        }


    }

    protected void validateDataImpl(D data) {
    }

    protected void validateCreate(D data) {
    }

    protected void validateUpdate(D data) {
    }

    protected boolean isSameData(D existentData, D actualData) {
        return actualData.getId() != null && existentData.getId().equals(actualData.getId());
    }

    protected static void validateEmail(String email) {
        if (!emailValidator.isValid(email)) {
            throw new DataValidationException("Invalid email address format '" + email + "'!");
        }
    }

    protected static void validateJsonStructure(JsonNode expectedNode, JsonNode actualNode) {
        Set<String> expectedFields = new HashSet<>();
        Iterator<String> fieldsIterator = expectedNode.fieldNames();
        while (fieldsIterator.hasNext()) {
            expectedFields.add(fieldsIterator.next());
        }

        Set<String> actualFields = new HashSet<>();
        fieldsIterator = actualNode.fieldNames();
        while (fieldsIterator.hasNext()) {
            actualFields.add(fieldsIterator.next());
        }

        if (!expectedFields.containsAll(actualFields) || !actualFields.containsAll(expectedFields)) {
            throw new DataValidationException("Provided json structure is different from stored one '" + actualNode + "'!");
        }

        for (String field : actualFields) {
            if (!actualNode.get(field).isTextual()) {
                throw new DataValidationException("Provided json structure can't contain non-text values '" + actualNode + "'!");
            }
        }
    }
}
