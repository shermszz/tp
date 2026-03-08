package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalOpportunities.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.opportunity.Opportunity;
import seedu.address.testutil.OpportunityBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullOpportunity_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_opportunityAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingOpportunityAdded modelStub = new ModelStubAcceptingOpportunityAdded();
        Opportunity validOpportunity = new OpportunityBuilder().build();

        CommandResult commandResult = new AddCommand(validOpportunity).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validOpportunity)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validOpportunity), modelStub.opportunitiesAdded);
    }

    @Test
    public void execute_duplicateOpportunity_throwsCommandException() {
        Opportunity validOpportunity = new OpportunityBuilder().build();
        AddCommand addCommand = new AddCommand(validOpportunity);
        ModelStub modelStub = new ModelStubWithOpportunity(validOpportunity);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_OPPORTUNITY, ()
                -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Opportunity alice = new OpportunityBuilder().withName("Alice").build();
        Opportunity bob = new OpportunityBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different opportunity -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addOpportunity(Opportunity opportunity) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasOpportunity(Opportunity opportunity) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteOpportunity(Opportunity target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setOpportunity(Opportunity target, Opportunity editedOpportunity) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Opportunity> getFilteredOpportunityList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredOpportunityList(Predicate<Opportunity> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single opportunity.
     */
    private class ModelStubWithOpportunity extends ModelStub {
        private final Opportunity opportunity;

        ModelStubWithOpportunity(Opportunity opportunity) {
            requireNonNull(opportunity);
            this.opportunity = opportunity;
        }

        @Override
        public boolean hasOpportunity(Opportunity opportunity) {
            requireNonNull(opportunity);
            return this.opportunity.isSameOpportunity(opportunity);
        }
    }

    /**
     * A Model stub that always accept the opportunity being added.
     */
    private class ModelStubAcceptingOpportunityAdded extends ModelStub {
        final ArrayList<Opportunity> opportunitiesAdded = new ArrayList<>();

        @Override
        public boolean hasOpportunity(Opportunity opportunity) {
            requireNonNull(opportunity);
            return opportunitiesAdded.stream().anyMatch(opportunity::isSameOpportunity);
        }

        @Override
        public void addOpportunity(Opportunity opportunity) {
            requireNonNull(opportunity);
            opportunitiesAdded.add(opportunity);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
