package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.opportunity.Opportunity;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_OPPORTUNITY = "Opportunities list contains duplicate opportunity(s).";

    private final List<JsonAdaptedOpportunity> opportunities = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given opportunities.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("opportunities") List<JsonAdaptedOpportunity> opportunities) {
        this.opportunities.addAll(opportunities);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        opportunities.addAll(source.getOpportunityList().stream()
                .map(JsonAdaptedOpportunity::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedOpportunity jsonAdaptedOpportunity : opportunities) {
            Opportunity opportunity = jsonAdaptedOpportunity.toModelType();
            if (addressBook.hasOpportunity(opportunity)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_OPPORTUNITY);
            }
            addressBook.addOpportunity(opportunity);
        }
        return addressBook;
    }

}
