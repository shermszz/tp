package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.opportunity.Opportunity;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Opportunity> PREDICATE_SHOW_ALL_OPPORTUNITIES = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a opportunity with the same identity as {@code opportunity} exists in the address book.
     */
    boolean hasOpportunity(Opportunity opportunity);

    /**
     * Deletes the given opportunity.
     * The opportunity must exist in the address book.
     */
    void deleteOpportunity(Opportunity target);

    /**
     * Adds the given opportunity.
     * {@code opportunity} must not already exist in the address book.
     */
    void addOpportunity(Opportunity opportunity);

    /**
     * Replaces the given opportunity {@code target} with {@code editedOpportunity}.
     * {@code target} must exist in the address book.
     * The opportunity identity of {@code editedOpportunity} must not be the same as another existing opportunity
     * in the address book.
     */
    void setOpportunity(Opportunity target, Opportunity editedOpportunity);

    /** Returns an unmodifiable view of the filtered opportunity list */
    ObservableList<Opportunity> getFilteredOpportunityList();

    /**
     * Updates the filter of the filtered opportunity list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredOpportunityList(Predicate<Opportunity> predicate);
}
