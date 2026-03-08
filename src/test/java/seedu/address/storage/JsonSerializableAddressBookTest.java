package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.testutil.TypicalOpportunities;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_OPPORTUNITIES_FILE =
            TEST_DATA_FOLDER.resolve("typicalOpportunitiesAddressBook.json");
    private static final Path INVALID_OPPORTUNITY_FILE = TEST_DATA_FOLDER.resolve("invalidOpportunityAddressBook.json");
    private static final Path DUPLICATE_OPPORTUNITY_FILE =
            TEST_DATA_FOLDER.resolve("duplicateOpportunityAddressBook.json");

    @Test
    public void toModelType_typicalOpportunitiesFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_OPPORTUNITIES_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalOpportunitiesAddressBook = TypicalOpportunities.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalOpportunitiesAddressBook);
    }

    @Test
    public void toModelType_invalidOpportunityFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_OPPORTUNITY_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateOpportunities_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_OPPORTUNITY_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_OPPORTUNITY,
                dataFromFile::toModelType);
    }

}
