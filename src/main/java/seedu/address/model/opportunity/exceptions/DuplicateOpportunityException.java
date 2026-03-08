package seedu.address.model.opportunity.exceptions;

/**
 * Signals that the operation will result in duplicate Opportunities (Opportunities are considered duplicates
 * if they have the same identity).
 */
public class DuplicateOpportunityException extends RuntimeException {
    public DuplicateOpportunityException() {
        super("Operation would result in duplicate opportunities");
    }
}
