package seedu.address.model.opportunity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.OpportunityBuilder;

public class OpportunityContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        OpportunityContainsKeywordsPredicate firstPredicate =
                new OpportunityContainsKeywordsPredicate(firstPredicateKeywordList);
        OpportunityContainsKeywordsPredicate secondPredicate =
                new OpportunityContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        OpportunityContainsKeywordsPredicate firstPredicateCopy =
                new OpportunityContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different opportunity -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        OpportunityContainsKeywordsPredicate predicate =
                new OpportunityContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new OpportunityBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = new OpportunityContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new OpportunityBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = new OpportunityContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new OpportunityBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new OpportunityContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new OpportunityBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        OpportunityContainsKeywordsPredicate predicate =
                new OpportunityContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new OpportunityBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new OpportunityContainsKeywordsPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new OpportunityBuilder().withName("Alice Bob").build()));

        // Keywords match phone, email and address, but does not match name
        predicate = new OpportunityContainsKeywordsPredicate(
                Arrays.asList("12345", "alice@email.com", "Main", "Street"));
        assertFalse(predicate.test(new OpportunityBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withAddress("Main Street").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        OpportunityContainsKeywordsPredicate predicate = new OpportunityContainsKeywordsPredicate(keywords);

        String expected = OpportunityContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
