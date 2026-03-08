package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.opportunity.Opportunity;
import seedu.address.model.opportunity.UniqueOpportunityList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameOpportunity comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueOpportunityList opportunities;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        opportunities = new UniqueOpportunityList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Opportunities in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the opportunity list with {@code opportunities}.
     * {@code opportunities} must not contain duplicate opportunities.
     */
    public void setOpportunities(List<Opportunity> opportunities) {
        this.opportunities.setOpportunities(opportunities);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setOpportunities(newData.getOpportunityList());
    }

    //// opportunity-level operations

    /**
     * Returns true if a opportunity with the same identity as {@code opportunity} exists in the address book.
     */
    public boolean hasOpportunity(Opportunity opportunity) {
        requireNonNull(opportunity);
        return opportunities.contains(opportunity);
    }

    /**
     * Adds a opportunity to the address book.
     * The opportunity must not already exist in the address book.
     */
    public void addOpportunity(Opportunity p) {
        opportunities.add(p);
    }

    /**
     * Replaces the given opportunity {@code target} in the list with {@code editedOpportunity}.
     * {@code target} must exist in the address book.
     * The opportunity identity of {@code editedOpportunity} must not be the same as another existing opportunity
     * in the address book.
     */
    public void setOpportunity(Opportunity target, Opportunity editedOpportunity) {
        requireNonNull(editedOpportunity);

        opportunities.setOpportunity(target, editedOpportunity);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeOpportunity(Opportunity key) {
        opportunities.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("opportunities", opportunities)
                .toString();
    }

    @Override
    public ObservableList<Opportunity> getOpportunityList() {
        return opportunities.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return opportunities.equals(otherAddressBook.opportunities);
    }

    @Override
    public int hashCode() {
        return opportunities.hashCode();
    }
}
