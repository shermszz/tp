package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalOpportunities.ALICE;
import static seedu.address.testutil.TypicalOpportunities.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.opportunity.Opportunity;
import seedu.address.model.opportunity.exceptions.DuplicateOpportunityException;
import seedu.address.testutil.OpportunityBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getOpportunityList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateOpportunities_throwsDuplicateOpportunityException() {
        // Two opportunities with the same identity fields
        Opportunity editedAlice = new OpportunityBuilder(ALICE)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        List<Opportunity> newOpportunities = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newOpportunities);

        assertThrows(DuplicateOpportunityException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasOpportunity_nullOpportunity_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasOpportunity(null));
    }

    @Test
    public void hasOpportunity_opportunityNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasOpportunity(ALICE));
    }

    @Test
    public void hasOpportunity_opportunityInAddressBook_returnsTrue() {
        addressBook.addOpportunity(ALICE);
        assertTrue(addressBook.hasOpportunity(ALICE));
    }

    @Test
    public void hasOpportunity_opportunityWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addOpportunity(ALICE);
        Opportunity editedAlice = new OpportunityBuilder(ALICE)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(addressBook.hasOpportunity(editedAlice));
    }

    @Test
    public void getOpportunityList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getOpportunityList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName()
                + "{opportunities=" + addressBook.getOpportunityList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose opportunities list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Opportunity> opportunities = FXCollections.observableArrayList();

        AddressBookStub(Collection<Opportunity> opportunities) {
            this.opportunities.setAll(opportunities);
        }

        @Override
        public ObservableList<Opportunity> getOpportunityList() {
            return opportunities;
        }
    }

}
