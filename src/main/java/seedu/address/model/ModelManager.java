package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.opportunity.Opportunity;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Opportunity> filteredOpportunities;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredOpportunities = new FilteredList<>(this.addressBook.getOpportunityList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasOpportunity(Opportunity opportunity) {
        requireNonNull(opportunity);
        return addressBook.hasOpportunity(opportunity);
    }

    @Override
    public void deleteOpportunity(Opportunity target) {
        addressBook.removeOpportunity(target);
    }

    @Override
    public void addOpportunity(Opportunity opportunity) {
        addressBook.addOpportunity(opportunity);
        updateFilteredOpportunityList(PREDICATE_SHOW_ALL_OPPORTUNITIES);
    }

    @Override
    public void setOpportunity(Opportunity target, Opportunity editedOpportunity) {
        requireAllNonNull(target, editedOpportunity);

        addressBook.setOpportunity(target, editedOpportunity);
    }

    //=========== Filtered Opportunity List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Opportunity} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Opportunity> getFilteredOpportunityList() {
        return filteredOpportunities;
    }

    @Override
    public void updateFilteredOpportunityList(Predicate<Opportunity> predicate) {
        requireNonNull(predicate);
        filteredOpportunities.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredOpportunities.equals(otherModelManager.filteredOpportunities);
    }

}
