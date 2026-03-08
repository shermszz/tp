package seedu.address.testutil;

import seedu.address.model.AddressBook;
import seedu.address.model.opportunity.Opportunity;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withOpportunity("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook();
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Opportunity} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withOpportunity(Opportunity opportunity) {
        addressBook.addOpportunity(opportunity);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
