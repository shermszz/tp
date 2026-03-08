package seedu.address.model.opportunity;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.opportunity.exceptions.DuplicateOpportunityException;
import seedu.address.model.opportunity.exceptions.OpportunityNotFoundException;

/**
 * A list of opportunities that enforces uniqueness between its elements and does not allow nulls.
 * An opportunity is considered unique by comparing using {@code Opportunity#isSameOpportunity(Opportunity)}.
 * As such, adding and updating of opportunities uses Opportunity#isSameOpportunity(Opportunity) for equality
 * so as to ensure that the opportunity being added or updated is unique in terms of identity in the
 * UniqueOpportunityList. However, the removal of an opportunity uses Opportunity#equals(Object) so
 * as to ensure that the opportunity with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Opportunity#isSameOpportunity(Opportunity)
 */
public class UniqueOpportunityList implements Iterable<Opportunity> {

    private final ObservableList<Opportunity> internalList = FXCollections.observableArrayList();
    private final ObservableList<Opportunity> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent opportunity as the given argument.
     */
    public boolean contains(Opportunity toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameOpportunity);
    }

    /**
     * Adds a opportunity to the list.
     * The opportunity must not already exist in the list.
     */
    public void add(Opportunity toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateOpportunityException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the opportunity {@code target} in the list with {@code editedOpportunity}.
     * {@code target} must exist in the list.
     * The opportunity identity of {@code editedOpportunity} must not be the same as another existing opportunity
     * in the list.
     */
    public void setOpportunity(Opportunity target, Opportunity editedOpportunity) {
        requireAllNonNull(target, editedOpportunity);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new OpportunityNotFoundException();
        }

        if (!target.isSameOpportunity(editedOpportunity) && contains(editedOpportunity)) {
            throw new DuplicateOpportunityException();
        }

        internalList.set(index, editedOpportunity);
    }

    /**
     * Removes the equivalent opportunity from the list.
     * The opportunity must exist in the list.
     */
    public void remove(Opportunity toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new OpportunityNotFoundException();
        }
    }

    public void setOpportunities(UniqueOpportunityList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code opportunities}.
     * {@code opportunities} must not contain duplicate opportunities.
     */
    public void setOpportunities(List<Opportunity> opportunities) {
        requireAllNonNull(opportunities);
        if (!opportunitiesAreUnique(opportunities)) {
            throw new DuplicateOpportunityException();
        }

        internalList.setAll(opportunities);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Opportunity> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Opportunity> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueOpportunityList)) {
            return false;
        }

        UniqueOpportunityList otherUniqueOpportunityList = (UniqueOpportunityList) other;
        return internalList.equals(otherUniqueOpportunityList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code opportunities} contains only unique opportunities.
     */
    private boolean opportunitiesAreUnique(List<Opportunity> opportunities) {
        for (int i = 0; i < opportunities.size() - 1; i++) {
            for (int j = i + 1; j < opportunities.size(); j++) {
                if (opportunities.get(i).isSameOpportunity(opportunities.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
