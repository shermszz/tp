package seedu.address.model.opportunity;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Opportunity}'s {@code Name} matches any of the keywords given.
 */
public class OpportunityContainsKeywordsPredicate implements Predicate<Opportunity> {
    private final List<String> keywords;

    public OpportunityContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Opportunity opportunity) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(opportunity.getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof OpportunityContainsKeywordsPredicate)) {
            return false;
        }

        OpportunityContainsKeywordsPredicate otherPredicate = (OpportunityContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
