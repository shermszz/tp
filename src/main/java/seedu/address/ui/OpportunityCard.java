package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.opportunity.Opportunity;

/**
 * An UI component that displays information of a {@code Opportunity}.
 */
public class OpportunityCard extends UiPart<Region> {

    private static final String FXML = "OpportunityListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Opportunity opportunity;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code OpportunityCode} with the given {@code Opportunity} and index to display.
     */
    public OpportunityCard(Opportunity opportunity, int displayedIndex) {
        super(FXML);
        this.opportunity = opportunity;
        id.setText(displayedIndex + ". ");
        name.setText(opportunity.getName().fullName);
        phone.setText(opportunity.getPhone().value);
        address.setText(opportunity.getAddress().value);
        email.setText(opportunity.getEmail().value);
        opportunity.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
