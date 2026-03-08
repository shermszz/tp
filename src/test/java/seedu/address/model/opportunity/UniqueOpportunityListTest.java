package seedu.address.model.opportunity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalOpportunities.ALICE;
import static seedu.address.testutil.TypicalOpportunities.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.opportunity.exceptions.DuplicateOpportunityException;
import seedu.address.model.opportunity.exceptions.OpportunityNotFoundException;
import seedu.address.testutil.OpportunityBuilder;

public class UniqueOpportunityListTest {

    private final UniqueOpportunityList uniqueOpportunityList = new UniqueOpportunityList();

    @Test
    public void contains_nullOpportunity_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueOpportunityList.contains(null));
    }

    @Test
    public void contains_opportunityNotInList_returnsFalse() {
        assertFalse(uniqueOpportunityList.contains(ALICE));
    }

    @Test
    public void contains_opportunityInList_returnsTrue() {
        uniqueOpportunityList.add(ALICE);
        assertTrue(uniqueOpportunityList.contains(ALICE));
    }

    @Test
    public void contains_opportunityWithSameIdentityFieldsInList_returnsTrue() {
        uniqueOpportunityList.add(ALICE);
        Opportunity editedAlice = new OpportunityBuilder(ALICE)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(uniqueOpportunityList.contains(editedAlice));
    }

    @Test
    public void add_nullOpportunity_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueOpportunityList.add(null));
    }

    @Test
    public void add_duplicateOpportunity_throwsDuplicateOpportunityException() {
        uniqueOpportunityList.add(ALICE);
        assertThrows(DuplicateOpportunityException.class, () -> uniqueOpportunityList.add(ALICE));
    }

    @Test
    public void setOpportunity_nullTargetOpportunity_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueOpportunityList.setOpportunity(null, ALICE));
    }

    @Test
    public void setOpportunity_nullEditedOpportunity_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueOpportunityList.setOpportunity(ALICE, null));
    }

    @Test
    public void setOpportunity_targetOpportunityNotInList_throwsOpportunityNotFoundException() {
        assertThrows(OpportunityNotFoundException.class, () -> uniqueOpportunityList.setOpportunity(ALICE, ALICE));
    }

    @Test
    public void setOpportunity_editedOpportunityIsSameOpportunity_success() {
        uniqueOpportunityList.add(ALICE);
        uniqueOpportunityList.setOpportunity(ALICE, ALICE);
        UniqueOpportunityList expectedUniqueOpportunityList = new UniqueOpportunityList();
        expectedUniqueOpportunityList.add(ALICE);
        assertEquals(expectedUniqueOpportunityList, uniqueOpportunityList);
    }

    @Test
    public void setOpportunity_editedOpportunityHasSameIdentity_success() {
        uniqueOpportunityList.add(ALICE);
        Opportunity editedAlice = new OpportunityBuilder(ALICE)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        uniqueOpportunityList.setOpportunity(ALICE, editedAlice);
        UniqueOpportunityList expectedUniqueOpportunityList = new UniqueOpportunityList();
        expectedUniqueOpportunityList.add(editedAlice);
        assertEquals(expectedUniqueOpportunityList, uniqueOpportunityList);
    }

    @Test
    public void setOpportunity_editedOpportunityHasDifferentIdentity_success() {
        uniqueOpportunityList.add(ALICE);
        uniqueOpportunityList.setOpportunity(ALICE, BOB);
        UniqueOpportunityList expectedUniqueOpportunityList = new UniqueOpportunityList();
        expectedUniqueOpportunityList.add(BOB);
        assertEquals(expectedUniqueOpportunityList, uniqueOpportunityList);
    }

    @Test
    public void setOpportunity_editedOpportunityHasNonUniqueIdentity_throwsDuplicateOpportunityException() {
        uniqueOpportunityList.add(ALICE);
        uniqueOpportunityList.add(BOB);
        assertThrows(DuplicateOpportunityException.class, () -> uniqueOpportunityList.setOpportunity(ALICE, BOB));
    }

    @Test
    public void remove_nullOpportunity_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueOpportunityList.remove(null));
    }

    @Test
    public void remove_opportunityDoesNotExist_throwsOpportunityNotFoundException() {
        assertThrows(OpportunityNotFoundException.class, () -> uniqueOpportunityList.remove(ALICE));
    }

    @Test
    public void remove_existingOpportunity_removesOpportunity() {
        uniqueOpportunityList.add(ALICE);
        uniqueOpportunityList.remove(ALICE);
        UniqueOpportunityList expectedUniqueOpportunityList = new UniqueOpportunityList();
        assertEquals(expectedUniqueOpportunityList, uniqueOpportunityList);
    }

    @Test
    public void setOpportunities_nullUniqueOpportunityList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> uniqueOpportunityList.setOpportunities((UniqueOpportunityList) null));
    }

    @Test
    public void setOpportunities_uniqueOpportunityList_replacesOwnListWithProvidedUniqueOpportunityList() {
        uniqueOpportunityList.add(ALICE);
        UniqueOpportunityList expectedUniqueOpportunityList = new UniqueOpportunityList();
        expectedUniqueOpportunityList.add(BOB);
        uniqueOpportunityList.setOpportunities(expectedUniqueOpportunityList);
        assertEquals(expectedUniqueOpportunityList, uniqueOpportunityList);
    }

    @Test
    public void setOpportunities_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> uniqueOpportunityList.setOpportunities((List<Opportunity>) null));
    }

    @Test
    public void setOpportunities_list_replacesOwnListWithProvidedList() {
        uniqueOpportunityList.add(ALICE);
        List<Opportunity> opportunityList = Collections.singletonList(BOB);
        uniqueOpportunityList.setOpportunities(opportunityList);
        UniqueOpportunityList expectedUniqueOpportunityList = new UniqueOpportunityList();
        expectedUniqueOpportunityList.add(BOB);
        assertEquals(expectedUniqueOpportunityList, uniqueOpportunityList);
    }

    @Test
    public void setOpportunities_listWithDuplicateOpportunities_throwsDuplicateOpportunityException() {
        List<Opportunity> listWithDuplicateOpportunities = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicateOpportunityException.class, ()
                -> uniqueOpportunityList.setOpportunities(listWithDuplicateOpportunities));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueOpportunityList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueOpportunityList.asUnmodifiableObservableList().toString(), uniqueOpportunityList.toString());
    }
}
