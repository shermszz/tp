package seedu.address.model.opportunity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalOpportunities.ALICE;
import static seedu.address.testutil.TypicalOpportunities.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.OpportunityBuilder;

public class OpportunityTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Opportunity opportunity = new OpportunityBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> opportunity.getTags().remove(0));
    }

    @Test
    public void isSameOpportunity() {
        // same object -> returns true
        assertTrue(ALICE.isSameOpportunity(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameOpportunity(null));

        // same name, all other attributes different -> returns true
        Opportunity editedAlice = new OpportunityBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameOpportunity(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new OpportunityBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameOpportunity(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Opportunity editedBob = new OpportunityBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSameOpportunity(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new OpportunityBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameOpportunity(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Opportunity aliceCopy = new OpportunityBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different opportunity -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Opportunity editedAlice = new OpportunityBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new OpportunityBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new OpportunityBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new OpportunityBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new OpportunityBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Opportunity.class.getCanonicalName() + "{name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhone() + ", email=" + ALICE.getEmail()
                + ", address=" + ALICE.getAddress() + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
