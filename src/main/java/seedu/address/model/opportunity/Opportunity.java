package seedu.address.model.opportunity;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents an Opportunity in the address book.
 * Guarantees: required fields are present and not null, optional fields may be null,
 * field values are validated by their respective classes, immutable.
 */
public class Opportunity {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();

    // Required fields
    private final Company company;
    private final Role role;

    /**
     * Every field must be present and not null.
     */
    public Opportunity(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.company = new Company("Test Company");
        this.role = new Role("Test Role");
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Company getCompany() {
        return company;
    }

    public Role getRole() {
        return role;
    }

    /**
     * Returns true if both opportunities have the same name.
     * This defines a weaker notion of equality between two opportunities.
     */
    public boolean isSameOpportunity(Opportunity otherOpportunity) {
        if (otherOpportunity == this) {
            return true;
        }

        return otherOpportunity != null
                && otherOpportunity.getName().equals(getName());
    }

    /**
     * Returns true if both opportunities have the same identity and data fields.
     * This defines a stronger notion of equality between two opportunities.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Opportunity)) {
            return false;
        }

        Opportunity otherOpportunity = (Opportunity) other;
        return name.equals(otherOpportunity.name)
                && phone.equals(otherOpportunity.phone)
                && email.equals(otherOpportunity.email)
                && address.equals(otherOpportunity.address)
                && tags.equals(otherOpportunity.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }

}
