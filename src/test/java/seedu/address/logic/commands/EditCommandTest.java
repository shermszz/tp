package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showOpportunityAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_OPPORTUNITY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_OPPORTUNITY;
import static seedu.address.testutil.TypicalOpportunities.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditOpportunityDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.opportunity.Opportunity;
import seedu.address.testutil.EditOpportunityDescriptorBuilder;
import seedu.address.testutil.OpportunityBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Opportunity editedOpportunity = new OpportunityBuilder().build();
        EditOpportunityDescriptor descriptor = new EditOpportunityDescriptorBuilder(editedOpportunity).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_OPPORTUNITY, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_OPPORTUNITY_SUCCESS,
                Messages.format(editedOpportunity));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setOpportunity(model.getFilteredOpportunityList().get(0), editedOpportunity);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastOpportunity = Index.fromOneBased(model.getFilteredOpportunityList().size());
        Opportunity lastOpportunity = model.getFilteredOpportunityList().get(indexLastOpportunity.getZeroBased());

        OpportunityBuilder opportunityInList = new OpportunityBuilder(lastOpportunity);
        Opportunity editedOpportunity = opportunityInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditOpportunityDescriptor descriptor = new EditOpportunityDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastOpportunity, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_OPPORTUNITY_SUCCESS,
                Messages.format(editedOpportunity));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setOpportunity(lastOpportunity, editedOpportunity);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_OPPORTUNITY, new EditOpportunityDescriptor());
        Opportunity editedOpportunity = model.getFilteredOpportunityList().get(INDEX_FIRST_OPPORTUNITY.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_OPPORTUNITY_SUCCESS,
                Messages.format(editedOpportunity));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showOpportunityAtIndex(model, INDEX_FIRST_OPPORTUNITY);

        Opportunity opportunityInFilteredList =
                model.getFilteredOpportunityList().get(INDEX_FIRST_OPPORTUNITY.getZeroBased());
        Opportunity editedOpportunity = new OpportunityBuilder(opportunityInFilteredList)
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_OPPORTUNITY,
                new EditOpportunityDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_OPPORTUNITY_SUCCESS,
                Messages.format(editedOpportunity));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setOpportunity(model.getFilteredOpportunityList().get(0), editedOpportunity);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateOpportunityUnfilteredList_failure() {
        Opportunity firstOpportunity = model.getFilteredOpportunityList().get(INDEX_FIRST_OPPORTUNITY.getZeroBased());
        EditOpportunityDescriptor descriptor = new EditOpportunityDescriptorBuilder(firstOpportunity).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_OPPORTUNITY, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_OPPORTUNITY);
    }

    @Test
    public void execute_duplicateOpportunityFilteredList_failure() {
        showOpportunityAtIndex(model, INDEX_FIRST_OPPORTUNITY);

        // edit opportunity in filtered list into a duplicate in address book
        Opportunity opportunityInList =
                model.getAddressBook().getOpportunityList().get(INDEX_SECOND_OPPORTUNITY.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_OPPORTUNITY,
                new EditOpportunityDescriptorBuilder(opportunityInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_OPPORTUNITY);
    }

    @Test
    public void execute_invalidOpportunityIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredOpportunityList().size() + 1);
        EditOpportunityDescriptor descriptor = new EditOpportunityDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_OPPORTUNITY_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidOpportunityIndexFilteredList_failure() {
        showOpportunityAtIndex(model, INDEX_FIRST_OPPORTUNITY);
        Index outOfBoundIndex = INDEX_SECOND_OPPORTUNITY;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getOpportunityList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditOpportunityDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_OPPORTUNITY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_OPPORTUNITY, DESC_AMY);

        // same values -> returns true
        EditOpportunityDescriptor copyDescriptor = new EditOpportunityDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_OPPORTUNITY, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_OPPORTUNITY, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_OPPORTUNITY, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditOpportunityDescriptor editOpportunityDescriptor = new EditOpportunityDescriptor();
        EditCommand editCommand = new EditCommand(index, editOpportunityDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editOpportunityDescriptor="
                + editOpportunityDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
