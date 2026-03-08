package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalOpportunities.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.opportunity.Opportunity;
import seedu.address.testutil.OpportunityBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newOpportunity_success() {
        Opportunity validOpportunity = new OpportunityBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addOpportunity(validOpportunity);

        assertCommandSuccess(new AddCommand(validOpportunity), model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validOpportunity)),
                expectedModel);
    }

    @Test
    public void execute_duplicateOpportunity_throwsCommandException() {
        Opportunity opportunityInList = model.getAddressBook().getOpportunityList().get(0);
        assertCommandFailure(new AddCommand(opportunityInList), model,
                AddCommand.MESSAGE_DUPLICATE_OPPORTUNITY);
    }

}
