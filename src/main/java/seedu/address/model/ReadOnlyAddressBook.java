package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.opportunity.Opportunity;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the opportunities list.
     * This list will not contain any duplicate opportunities.
     */
    ObservableList<Opportunity> getOpportunityList();

}
