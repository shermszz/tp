package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_OPPORTUNITIES;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.opportunity.Address;
import seedu.address.model.opportunity.Email;
import seedu.address.model.opportunity.Name;
import seedu.address.model.opportunity.Opportunity;
import seedu.address.model.opportunity.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing opportunity in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the opportunity identified "
            + "by the index number used in the displayed opportunity list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_OPPORTUNITY_SUCCESS = "Edited Opportunity: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_OPPORTUNITY = "This opportunity already exists in the address book.";

    private final Index index;
    private final EditOpportunityDescriptor editOpportunityDescriptor;

    /**
     * @param index of the opportunity in the filtered opportunity list to edit
     * @param editOpportunityDescriptor details to edit the opportunity with
     */
    public EditCommand(Index index, EditOpportunityDescriptor editOpportunityDescriptor) {
        requireNonNull(index);
        requireNonNull(editOpportunityDescriptor);

        this.index = index;
        this.editOpportunityDescriptor = new EditOpportunityDescriptor(editOpportunityDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Opportunity> lastShownList = model.getFilteredOpportunityList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_OPPORTUNITY_DISPLAYED_INDEX);
        }

        Opportunity opportunityToEdit = lastShownList.get(index.getZeroBased());
        Opportunity editedOpportunity = createEditedOpportunity(opportunityToEdit, editOpportunityDescriptor);

        if (!opportunityToEdit.isSameOpportunity(editedOpportunity) && model.hasOpportunity(editedOpportunity)) {
            throw new CommandException(MESSAGE_DUPLICATE_OPPORTUNITY);
        }

        model.setOpportunity(opportunityToEdit, editedOpportunity);
        model.updateFilteredOpportunityList(PREDICATE_SHOW_ALL_OPPORTUNITIES);
        return new CommandResult(String.format(MESSAGE_EDIT_OPPORTUNITY_SUCCESS, Messages.format(editedOpportunity)));
    }

    /**
     * Creates and returns a {@code Opportunity} with the details of {@code opportunityToEdit}
     * edited with {@code editOpportunityDescriptor}.
     */
    private static Opportunity createEditedOpportunity(Opportunity opportunityToEdit,
            EditOpportunityDescriptor editOpportunityDescriptor) {
        assert opportunityToEdit != null;

        Name updatedName = editOpportunityDescriptor.getName().orElse(opportunityToEdit.getName());
        Phone updatedPhone = editOpportunityDescriptor.getPhone().orElse(opportunityToEdit.getPhone());
        Email updatedEmail = editOpportunityDescriptor.getEmail().orElse(opportunityToEdit.getEmail());
        Address updatedAddress = editOpportunityDescriptor.getAddress().orElse(opportunityToEdit.getAddress());
        Set<Tag> updatedTags = editOpportunityDescriptor.getTags().orElse(opportunityToEdit.getTags());

        return new Opportunity(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editOpportunityDescriptor.equals(otherEditCommand.editOpportunityDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editOpportunityDescriptor", editOpportunityDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the opportunity with. Each non-empty field value will replace the
     * corresponding field value of the opportunity.
     */
    public static class EditOpportunityDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditOpportunityDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditOpportunityDescriptor(EditOpportunityDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditOpportunityDescriptor)) {
                return false;
            }

            EditOpportunityDescriptor otherEditOpportunityDescriptor = (EditOpportunityDescriptor) other;
            return Objects.equals(name, otherEditOpportunityDescriptor.name)
                    && Objects.equals(phone, otherEditOpportunityDescriptor.phone)
                    && Objects.equals(email, otherEditOpportunityDescriptor.email)
                    && Objects.equals(address, otherEditOpportunityDescriptor.address)
                    && Objects.equals(tags, otherEditOpportunityDescriptor.tags);
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
}
