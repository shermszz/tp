package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditOpportunityDescriptor;
import seedu.address.model.opportunity.Address;
import seedu.address.model.opportunity.Email;
import seedu.address.model.opportunity.Name;
import seedu.address.model.opportunity.Opportunity;
import seedu.address.model.opportunity.Phone;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditOpportunityDescriptor objects.
 */
public class EditOpportunityDescriptorBuilder {

    private EditOpportunityDescriptor descriptor;

    public EditOpportunityDescriptorBuilder() {
        descriptor = new EditOpportunityDescriptor();
    }

    public EditOpportunityDescriptorBuilder(EditOpportunityDescriptor descriptor) {
        this.descriptor = new EditOpportunityDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditOpportunityDescriptor} with fields containing {@code opportunity}'s details
     */
    public EditOpportunityDescriptorBuilder(Opportunity opportunity) {
        descriptor = new EditOpportunityDescriptor();
        descriptor.setName(opportunity.getName());
        descriptor.setPhone(opportunity.getPhone());
        descriptor.setEmail(opportunity.getEmail());
        descriptor.setAddress(opportunity.getAddress());
        descriptor.setTags(opportunity.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditOpportunityDescriptor} that we are building.
     */
    public EditOpportunityDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditOpportunityDescriptor} that we are building.
     */
    public EditOpportunityDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditOpportunityDescriptor} that we are building.
     */
    public EditOpportunityDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditOpportunityDescriptor} that we are building.
     */
    public EditOpportunityDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditOpportunityDescriptor}
     * that we are building.
     */
    public EditOpportunityDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditOpportunityDescriptor build() {
        return descriptor;
    }
}
