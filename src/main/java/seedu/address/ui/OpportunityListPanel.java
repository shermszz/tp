package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.opportunity.Opportunity;

/**
 * Panel containing the list of opportunities.
 */
public class OpportunityListPanel extends UiPart<Region> {
    private static final String FXML = "OpportunityListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(OpportunityListPanel.class);

    @FXML
    private ListView<Opportunity> opportunityListView;

    /**
     * Creates a {@code OpportunityListPanel} with the given {@code ObservableList}.
     */
    public OpportunityListPanel(ObservableList<Opportunity> opportunityList) {
        super(FXML);
        opportunityListView.setItems(opportunityList);
        opportunityListView.setCellFactory(listView -> new OpportunityListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Opportunity} using a {@code OpportunityCard}.
     */
    class OpportunityListViewCell extends ListCell<Opportunity> {
        @Override
        protected void updateItem(Opportunity opportunity, boolean empty) {
            super.updateItem(opportunity, empty);

            if (empty || opportunity == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new OpportunityCard(opportunity, getIndex() + 1).getRoot());
            }
        }
    }

}
