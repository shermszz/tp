package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.opportunity.Address;
import seedu.address.model.opportunity.Email;
import seedu.address.model.opportunity.Name;
import seedu.address.model.opportunity.Opportunity;
import seedu.address.model.opportunity.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Opportunity objects.
 */
public class OpportunityBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;

    /**
     * Creates a {@code OpportunityBuilder} with the default details.
     */
    public OpportunityBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the OpportunityBuilder with the data of {@code opportunityToCopy}.
     */
    public OpportunityBuilder(Opportunity opportunityToCopy) {
        name = opportunityToCopy.getName();
        phone = opportunityToCopy.getPhone();
        email = opportunityToCopy.getEmail();
        address = opportunityToCopy.getAddress();
        tags = new HashSet<>(opportunityToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Opportunity} that we are building.
     */
    public OpportunityBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Opportunity} that we are building.
     */
    public OpportunityBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Opportunity} that we are building.
     */
    public OpportunityBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Opportunity} that we are building.
     */
    public OpportunityBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Opportunity} that we are building.
     */
    public OpportunityBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Opportunity build() {
        return new Opportunity(name, phone, email, address, tags);
    }

}
