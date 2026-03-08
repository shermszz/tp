package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_OPPORTUNITIES_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalOpportunities.CARL;
import static seedu.address.testutil.TypicalOpportunities.ELLE;
import static seedu.address.testutil.TypicalOpportunities.FIONA;
import static seedu.address.testutil.TypicalOpportunities.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.opportunity.OpportunityContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        OpportunityContainsKeywordsPredicate firstPredicate =
                new OpportunityContainsKeywordsPredicate(Collections.singletonList("first"));
        OpportunityContainsKeywordsPredicate secondPredicate =
                new OpportunityContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different opportunity -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noOpportunityFound() {
        String expectedMessage = String.format(MESSAGE_OPPORTUNITIES_LISTED_OVERVIEW, 0);
        OpportunityContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredOpportunityList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredOpportunityList());
    }

    @Test
    public void execute_multipleKeywords_multipleOpportunitiesFound() {
        String expectedMessage = String.format(MESSAGE_OPPORTUNITIES_LISTED_OVERVIEW, 3);
        OpportunityContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredOpportunityList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredOpportunityList());
    }

    @Test
    public void toStringMethod() {
        OpportunityContainsKeywordsPredicate predicate =
                new OpportunityContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code OpportunityContainsKeywordsPredicate}.
     */
    private OpportunityContainsKeywordsPredicate preparePredicate(String userInput) {
        return new OpportunityContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
