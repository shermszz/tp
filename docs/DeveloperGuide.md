---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# InternTrack Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](http://se-education.org)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="300" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

<box type="info" seamless>

**Note:** InternTrack is built on AB3 and still retains some legacy internal class names such as
`AddressBook`, `AddressBookParser`, `ReadOnlyAddressBook`, and `VersionedAddressBook`.
These are implementation names. Explanations in this guide use InternTrack terms such as opportunity contacts/records,
contacts, companies, roles, and statuses.

</box>

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="950" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="350" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `OpportunityListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-W13-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Opportunity` objects residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="650"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" width="1200"/>

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete an opportunity record).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="700"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="800" />


The `Model` component,

* stores InternTrack data internally as `Opportunity` objects (which are contained in a `UniqueOpportunityList` object).
* stores the currently 'selected' `Opportunity` objects (e.g., results of a search query or an archive filter) as a separate *filtered* list which is exposed to outsiders as an unmodifiable `ObservableList<Opportunity>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPrefs` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components).

<box type="info" seamless>

**Note:** The `Phone` attribute is optional (represented by a `0..1` multiplicity in the class diagram), while all other attributes such as `Email`, `Cycle`, `Company`, and `Role` are strictly mandatory (`1` multiplicity) to ensure robust duplicate detection across internship applications.

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-W13-2/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="650" />

The `Storage` component,
* can save InternTrack data to JSON storage files such as `addressbook.json`, and read them back into corresponding objects together with user preference data.
* inherits from both `AddressBookStorage` and `UserPrefsStorage`, which means it can be treated as either one (if the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Undo feature

#### Implementation

The undo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()` and `Model#undoAddressBook()` respectively.

Given below is an example usage scenario and how the undo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial tracker state, and the `currentStatePointer` pointing to that single tracker state.

<puml src="diagrams/UndoState0.puml" alt="UndoState0" />

Step 2. The user executes `delete 5` command to delete the 5th opportunity contact in the tracker. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the tracker after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted tracker state.

<puml src="diagrams/UndoState1.puml" alt="UndoState1" />

Step 3. The user executes `add n/David …​` to add a new opportunity contact. The `add` command also calls `Model#commitAddressBook()`, causing another modified tracker state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoState2.puml" alt="UndoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the tracker state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the opportunity contact was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous tracker state, and restore the tracker data to that state.

<puml src="diagrams/UndoState3.puml" alt="UndoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial tracker state, then there are no previous tracker states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

Step 5. The user then decides to execute the command `list`. Commands that do not modify the tracker, such as `list`, will usually not call `Model#commitAddressBook()` or `Model#undoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoState4.puml" alt="UndoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all tracker states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to restore the previously undone `add n/David …​` command state after a new mutation occurs. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoState5.puml" alt="UndoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo by itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the opportunity record being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

### Data archiving

The data archiving feature allows users to hide selected opportunity records from the active list without deleting them.

This is implemented by storing an archived flag in each Opportunity. Archive and unarchive commands update this flag accordingly. The Model uses separate predicates to display active records and archived records. The archived state is also saved in the storage file so that it persists across sessions.

This approach keeps the implementation simple because it extends the existing record structure instead of introducing a separate archive data structure.

Note that archived records remain subject to uniqueness enforcement. `isSameOpportunity()` does not compare the `isArchived` flag, so an archived and an active record with the same Email, Company, Role, and Cycle cannot coexist in the tracker. Attempting to add a record whose identity matches an archived entry will be rejected. The user should use `unarchive` to restore the archived entry instead.

### Input validation philosophy

InternTrack follows a **minimalist validation approach**: only block characters that break the CLI parser, allow everything else.

Command words and certain command sub-keywords are case-insensitive, including `archive` in `list archive` and `cycle` in `archive cycle`.

Prefixes are case-sensitive and must be lowercase, such as `n/`, `e/`, `cr/`, `c/`, `r/`, `s/`, `cy/`, `p/`, and `a/`.

#### Simplified validation for Name, ContactRole, Company, and Role fields

**Rationale for simplified validation:**

The `Name`, `ContactRole`, `Company`, and `Role` fields use a simplified validation strategy that only blocks the forward slash (`/`) character:

1. **Avoid overzealous validation**: Pre-emptively blocking characters leads to false positives and frustrates users
2. **Support international users**: Allows Unicode names, symbols, emoji, and special characters used globally
3. **Future-proof**: No need to maintain complex lists of "allowed" characters or update validation for new use cases
4. **Clear technical justification**: Forward slash (`/`) is blocked because it conflicts with CLI prefix syntax, not arbitrary preference

**Validation rules:**
* **Name field**: Any characters except `/`. Length: 1-60 characters.
  * Examples: `John Smith`, `@John`, `李明`, `Dr. O'Connor, Jr.`, `???` (placeholder), `C++`
* **ContactRole field**: Any characters except `/`. Length: 1-50 characters.
  * Examples: `recruiter`, `C++ Developer`, `#TechLead`, `...` (placeholder), `HR & Recruiting`
* **Company field**: Any characters except `/`. Length: 1-60 characters.
  * Examples: `Google`, `3M Company`, `AT&T`, `@Startup`, `北京公司`
* **Role field**: Any characters except `/`. Length: 1-80 characters.
  * Examples: `Software Engineer`, `C# Developer`, `SWE-ML Engineer`, `Full-Stack (React+Node)`

**What's allowed:**
- All Unicode characters (letters, digits, symbols)
- Special punctuation (@, #, $, %, &, *, !, ?, etc.)
- Emoji and extended Unicode
- Placeholder values: `...`, `???`, `(TBD)`, `---`, `!!!`
- Programming-related names: `C++`, `C#`, `.NET`

**What's blocked:**
- Forward slash (`/`) - conflicts with CLI prefix syntax
- Leading/trailing whitespace - automatically trimmed
- Empty strings - prevented by MIN_LENGTH validation

**Rationale for blocking forward slash (`/`):**

The forward slash character is specifically blocked in Name, ContactRole, Company, and Role fields because:
1. **CLI prefix delimiter**: InternTrack uses `/` as the prefix delimiter (e.g., `n/`, `cr/`, `c/`, `r/`)
2. **Parser safety**: The `ArgumentTokenizer` recognizes prefixes when preceded by a space. A field value containing a known prefix pattern after a space (e.g., `Frontend c/Backend`) would be misinterpreted as a new prefix
3. **Legitimate technical constraint**: This restriction is based on a real parsing concern, not arbitrary preference
4. **Available alternatives**: Users can express compound values using:
   - Hyphens: `SWE-ML Engineer` instead of `SWE/ML Engineer`
   - Ampersands: `Frontend & Backend` instead of `Frontend/Backend`
   - Parentheses: `SWE (ML)` instead of `SWE/ML`

**Implementation:**

All four fields use the same regex pattern:
```java
public static final String VALIDATION_REGEX = "[^/\\s][^/]*";
```

This pattern:
- `[^/\\s]` - First character: NOT slash, NOT whitespace
- `[^/]*` - Remaining characters: NOT slash (zero or more)
- Combined with `trim()` to remove leading/trailing whitespace
- Length validation ensures non-empty input (MIN_LENGTH = 1)

**Design decision:** We prefer to be maximally permissive (allow all characters) while maintaining strict boundaries on the one character (`/`) that directly conflicts with our CLI prefix syntax. This strikes the optimal balance between user flexibility and technical correctness.

**Note on Role field:** Previously, Role.java allowed `/` in its regex (`[\\p{Alnum}][\\p{Alnum} &.,()'\\-/]*`), which was inconsistent with the other fields. This has been corrected to use the same simplified validation pattern for consistency.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* requires a lightweight way to manage a significant number of concurrent internship applications
* tech-savvy undergraduate
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

* manage internship applications faster than a typical mouse/GUI driven app
* helps user capture, update, and retrieve key application details in seconds
* reduce the mental overhead of keeping track of recruiters, interviewers, referrers, and other opportunity-related contacts
* serve as a lightweight offline desktop tool for organizing opportunity contacts


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​ | So that I …​ |
|----------|---------|--------------|----------------|
| `* * *` | undergraduate applicant | add a new application-related contact record | can track important contacts instead of relying on memory |
| `* * *` | busy applicant juggling many applications | list all tracked contact records | can see who I am currently managing at a glance |
| `* * *` | busy applicant juggling many applications | remove a contact record I no longer need | my tracker stays uncluttered |
| `* * *` | undergraduate applicant | have my tracked records persist between sessions | do not lose critical contact information |
| `* * *` | undergraduate applicant | have each contact record include opportunity context such as company and role | can remember why the contact matters |
| `* * *` | busy applicant juggling many applications | edit an existing contact record | can keep details up to date when a contact’s information or application context changes |
| `* * *` | busy applicant juggling many applications | quickly find a contact record by keyword | can retrieve details under time pressure |
| `* *` | new user exploring InternTrack | see example records | can understand what information the product is meant to track |
| `* *` | new user exploring InternTrack | remove the example records in one go | can start clean without tedious manual cleanup |
| `* *` | busy applicant juggling many applications | record a stage or status for a contact record | know my current progress with that contact and opportunity |
| `* *` | undergraduate applicant | store contact details such as name and email | can follow up without searching through chats or email threads |
| `* *` | undergraduate applicant | store a contact role for each record | can tell whether someone is a recruiter, interviewer, referrer, or hiring manager |
| `* *` | busy applicant juggling many applications | remove multiple contact records at once | can clean up outdated records more efficiently |
| `* *` | end-of-cycle user | archive old records | my current list stays focused without losing history |
| `* *` | end-of-cycle user | unarchive previously archived records | can restore older records when I need to revisit them |
| `* *` | user resetting my tracker | clear all contact records in one shot | can start over without deleting records one by one |
| `* *` | new user exploring InternTrack | have quick access to the user guide from within the app | can learn how to use the commands when I get stuck |
| `* *` | keyboard-oriented user | exit the app using a command | can close it quickly without leaving the keyboard |
| `* *` | busy applicant juggling many applications | filter records by company | can focus on all contacts related to one organization at a time |
| `* *` | busy applicant juggling many applications | recover from accidental destructive actions | one mistake does not wipe critical records |
| `*` | busy applicant juggling many applications | be warned about potential duplicate contact records | do not track the same contact twice unnecessarily |
| `*` | frequent user | maintain consistent stage labels | filtering and review remain reliable over time |

### Use cases

(For all use cases below, the **System** is the `InternTrack` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 — Add an opportunity contact**

**MSS**

1. User requests to add a new opportunity contact.
2. System validates the provided details.
3. System creates the new opportunity contact.
4. System reflects the newly added opportunity contact.

   Use case ends.

**Extensions**
* 2a. Required details are missing (Name, Email, Contact Role, Company, Role, Status, or Cycle).
    * 2a1. System shows an error message indicating missing required details.

        Use case resumes from step 1.

* 2b. Duplicate detected (same Email, Company, Role and Cycle already exists).
    * 2b1. System informs the user that a duplicate opportunity contact exists.

        Use case ends.

* 2c. Provided details are invalid (e.g. Email is not a valid format, Status is not a recognised value).
    * 2c1. System shows an error message indicating the invalid field(s).

        Use case resumes from step 1.


**Use case: UC02 — Remove an opportunity contact**

**MSS**

1.  User requests to <u>list opportunity contacts (UC03)</u>.
2.  System shows the list of stored opportunity contacts.
3.  User requests to remove one or more specific contacts in the list.
4.  System removes the requested contact(s).
5.  System reflects the removal.

    Use case ends.

**Extensions**

* 1a. No contacts exist.
    * 1a1. System informs the user there are no contacts to remove.

      Use case ends.

* 3a. One or more of the provided indexes does not refer to an existing contact.
    * 3a1. System shows an error message.

      Use case resumes from step 3.


**Use case: UC03 — List opportunity contacts**

**MSS**

1.  User requests to list opportunity contacts.
2.  System retrieves and shows all active (unarchived) opportunity contacts.

    Use case ends.

**Extensions**

* 1a. No active opportunity contacts exist.
    * 1a1. System informs the user there are no active opportunity contacts.

      Use case ends.

* 1b. User requests to list archived contacts.
    * 1b1. System retrieves and shows all archived opportunity contacts.

      Use case ends.

    * 1b2. No archived contacts exist.
        * 1b2a. System informs the user that the archive is empty.

          Use case ends.


**Use case: UC04 — Edit an opportunity contact**

Preconditions: At least one opportunity contact exists.

**MSS**

1.  User requests to <u>list opportunity contacts (UC03)</u>.
2.  System shows the list of opportunity contacts.
3.  User requests to edit the details of a specified opportunity contact.
4.  System validates the new details.
5.  System updates the opportunity contact.
6.  System reflects the update.

    Use case ends.

**Extensions**

* 4a. Index is invalid.
  * 4a1. System shows an error message.

    Use case resumes from step 3.

* 4b. Provided details are invalid (e.g. Email is not a valid format, Status is not a recognised value).
  * 4b1. System shows an error message indicating the invalid field(s).

    Use case resumes from step 3.

* 4c. Edited details result in a duplicate (same Email, Company, Role and Cycle as an existing record).
  * 4c1. System informs the user that a duplicate opportunity contact already exists.

    Use case ends.

**Use case: UC05 — Search for opportunity contacts by keyword**

**MSS**

1.  User requests to search opportunity contacts matching a keyword.
2.  System shows all opportunity contacts that match the keyword.

    Use case ends.

**Extensions**

* 2a. No matches found.
    * 2a1. System informs the user there are no matching opportunity contacts.

      Use case ends.

**Use case: UC06 — Archive an opportunity contact**

Preconditions: At least one opportunity contact exists in the unarchived/active list.

**MSS**

1.  User requests to <u>list unarchived/active opportunity contacts (UC03)</u>.
2.  System shows the list of unarchived opportunity contacts.
3.  User requests to archive a specific opportunity contact from the unarchived list.
4.  System archives the specified opportunity contact.
5.  System confirms that the opportunity contact has been archived.

    Use case ends.

**Extensions**

* 3a. The specified index is invalid.
    * 3a1. System informs the user that the index is invalid.

      Use case ends.

**Use case: UC07 — Archive opportunity contacts by cycle**

Preconditions: At least one opportunity contact exists in the unarchived/active list.

**MSS**

1.  User requests to <u>list unarchived/active opportunity contacts (UC03)</u>.
2.  System shows the list of unarchived opportunity contacts.
3.  User requests to archive all opportunity contacts belonging to a specified cycle from the unarchived list.
4.  System archives all unarchived opportunity contacts that belong to the specified cycle.
5.  System confirms that the matching opportunity contacts have been archived.

    Use case ends.

**Extensions**

* 3a. The specified cycle is invalid.
    * 3a1. System informs the user that the cycle is invalid.

      Use case ends.

* 3b. There are no unarchived opportunity contacts belonging to the specified cycle.
    * 3b1. System informs the user that no matching opportunity contacts were found.

      Use case ends.

**Use case: UC08 — Unarchive an opportunity contact**

Preconditions: At least one archived opportunity contact exists.

**MSS**

1. User requests to show archived opportunity contacts.
2. System shows the list of archived opportunity contacts.
3. User requests to unarchive a specific opportunity contact from the displayed archived list.
4. System unarchives the specified opportunity contact.
5. System confirms that the opportunity contact has been unarchived.

   Use case ends.

**Extensions**

* 3a. The specified index is invalid.

  * 3a1. System informs the user that the index is invalid.

    Use case ends.

**Use case: UC09 — Undo a command**

**MSS**

1.  User requests to undo the previous mutating command.
2.  System restores the tracker data to its previous state.
3.  System displays a success message.

    Use case ends.

**Extensions**

* 1a. There is no previous state to restore.
    * 1a1. System shows an error message indicating there are no more commands to undo.

      Use case ends.

**Note:** `undo` restores the underlying tracker data only. It does not modify the current UI state, including the currently displayed filtered list and the selected Main/Archive view.

**Use case: UC10 — Clear opportunity contact**

**MSS**

1. User requests to clear all opportunity contacts.
2. System deletes all tracked opportunity contacts, replacing it with an empty state.
3. System reflects an empty tracker.

   Use case ends.

**Use case: UC11 — Request for help**

**MSS**

1. User requests for help.
2. System opens the help window containing a link to the User Guide.
3. User copies link to User Guide.

   Use case ends.

**Use case: UC12 — Exit the application**

**MSS**

1. User requests to exit the application.
2. System closes the application.

   Use case ends.

### Non-Functional Requirements

**For this section:**
**commands** refer to the following commands: **add, delete, list, find, edit, archive, and unarchive**.
**core functions** refer to the execution of the app’s main features, including command execution, data storage, and GUI support, but excluding application startup.

1. **Platform compatibility:** The app must run on Windows 10/11, macOS, and Ubuntu Linux with Java 17 installed.
2. **Distribution:** The app must be distributable as a single executable JAR without requiring an installer.
3. **Offline operation:** All core functions must work fully offline and must not require any remote server or external API.
4. **Local storage format:** Data must be stored locally in a human-editable text file (e.g., JSON) and must not require a DBMS.
5. **Response time:** With up to **500** opportunity contacts, all commands must complete within **500 ms**, excluding application startup.
6. **Startup time:** With up to **500** opportunity contacts, the application must be ready to accept user input within **2 seconds** after launch on a typical modern laptop.
7. **Autosave reliability:** The app must automatically save after every state-changing operation (e.g., add, delete, edit, archive, unarchive).
8. **Graceful storage failure:** If writing the storage file fails, the app must not crash and must show a clear error message. If the storage file is missing on startup, the app must not crash and must start with sample data. If the storage file exists but cannot be read or parsed, the app must not crash and must start with an empty data set without showing a user-facing error message.
9. **CLI-first usability:** All commands must be executable using keyboard-only input; the GUI is for visualization and must not be required to complete core tasks.

### Glossary

* **CLI**: Command Line Interface; users interact primarily by typing commands.
* **Opportunity contact / record**: A single record representing a tracked contact and the associated internship opportunity.
* **Contact**: A recruiter, interviewer, referrer, hiring manager, or other person involved in the user’s internship application process.
* **Contact Role**: The role of the contact person associated with the opportunity (e.g. recruiter, hiring manager, interviewer, referrer).
* **Company**: The organisation associated with the tracked contact or opportunity.
* **Role**: The internship position associated with the opportunity.
* **Status / Stage**: The current progress state of the user’s interaction with a contact or the associated opportunity (e.g. SAVED, APPLIED, OA, INTERVIEW, OFFER, REJECTED, WITHDRAWN).
* **Keyword**: A text fragment used to search or filter records.
* **Duplicate records**: Multiple records sharing the same Email, Company, Role, and Cycle.
* **Index**: A temporary 1-based position number shown in a displayed list.
* **MSS**: Main Success Scenario; the most straightforward interaction for a given use case, assuming nothing goes wrong.
* **Persistence**: The ability to save and load data across sessions from local storage.
* **Prefix**: A short label used to indicate a field in typed input (e.g. n/, e/, c/, r/).
* **Cycle**: A defined internship application period, such as Summer, Semester 1, or Semester 2.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:**
These instructions complement the User Guide.
Refer to the UG for full command formats, accepted values, and standard examples.
The test cases below focus on:
* one fast end-to-end path through the main features
* view-dependent behavior
* invalid inputs and edge cases
* persistence and file-recovery behavior

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy it into an empty folder.

   1. Open a command terminal, `cd` into the folder you put the jar file in, and run `java -jar interntrack.jar`.<br>
      Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window and layout preferences

   1. Resize the window to an optimum size. Move the window to a different location. Adjust the divider between the result display box and the opportunity list. Close the window.

   1. Re-launch the app by running `java -jar interntrack.jar` in the terminal.<br>
      Expected: The most recent window size, window location, and divider position are retained.

1. Exiting the application

   1. Prerequisites: The application is running.

   1. Test case: `exit`<br>
      Expected: The application closes.

   1. Test case: `exit 123`<br>
      Expected: The application does not close. An invalid command format error is shown.

### Fast end-to-end smoke test

1. Start from a fresh launch.

1. Run the following commands in order:

   1. `list`

   1. `add n/Alex Tan e/alex.tan@test.com cr/recruiter c/Grab r/SWE Intern s/SAVED cy/SUMMER 2026 p/+65 9123 4567`

   1. `find Alex`

   1. `edit 1 s/INTERVIEW p/+65-9000-1111`

   1. `archive 1`

   1. `list archive`

   1. `unarchive 1`

   1. `list`

   1. `delete 1`

   1. `undo`

1. Expected:
   * Each command succeeds.
   * The view indicator updates correctly between `Main` and `Archive`.
   * The deleted contact is restored after `undo`.

### Help and list commands

1. Help window

   1. Prerequisites: The application is running.

   1. Test case: `help`<br>
      Expected: The help window opens or a help message containing the help link is shown.

   1. Test case: `help 123`<br>
      Expected: No help window is opened. An invalid command format error is shown.

1. List commands

   1. Test case: `list`<br>
      Expected: All unarchived opportunity contacts are shown. The `Main` tab is highlighted.

   1. Test case: `list archive`<br>
      Expected: All archived opportunity contacts are shown. The `Archive` tab is highlighted.

   1. Test case: `list 123`<br>
      Expected: The displayed list is unchanged. An invalid command format error is shown.

   1. Test case: `list archive 123`<br>
      Expected: The displayed list is unchanged. An invalid command format error is shown.

### Add and edit edge cases

1. Adding a record with Unicode and placeholders

   1. Test case: `add n/李明 e/liming@test.com cr/(TBD) c/北京公司 r/C++ Developer s/APPLIED cy/WINTER 2026 p/+65 8888 7777`<br>
      Expected: A new opportunity contact is added successfully.<br><br>

1. Rejecting an add command with an invalid email

   1. Test case: `add n/Chris Tan e/not-an-email cr/recruiter c/Meta r/SWE Intern s/SAVED cy/SUMMER 2026`<br>
      Expected: No opportunity contact is added. Error details are shown in the status message.<br><br>

1. Rejecting an add command with an invalid status

   1. Test case: `add n/Dan Lim e/dan.lim@test.com cr/recruiter c/TikTok r/Data Analyst s/PENDING cy/SUMMER 2026`<br>
      Expected: No opportunity contact is added. Error details are shown in the status message.<br><br>

1. Rejecting a duplicate against an active record

   1. Prerequisites: Ensure this active record exists:
      `add n/Active Dup e/active.dup@test.com cr/recruiter c/Grab r/SWE Intern s/SAVED cy/SUMMER 2026`

   1. Test case:
      `add n/Another Active Dup e/active.dup@test.com cr/hiring manager c/Grab r/SWE Intern s/APPLIED cy/SUMMER 2026`<br>
      Expected: No opportunity contact is added. A duplicate-entry error is shown.

1. Rejecting a duplicate against an archived record

   1. Prerequisites:
      1. Add this record:
         `add n/Archived Dup e/archived.dup@test.com cr/recruiter c/Stripe r/SWE Intern s/SAVED cy/SUMMER 2026`
      1. Find it:
         `find Archived Dup`
      1. Archive it:
         `archive 1`<br><br>

   1. Test case:
      `add n/Another Archived Dup e/archived.dup@test.com cr/hiring manager c/Stripe r/SWE Intern s/OFFER cy/SUMMER 2026`<br>
      Expected: No opportunity contact is added. A duplicate-entry error is shown.

1. Editing an existing opportunity contact

   1. Prerequisites: List all active opportunity contacts using the `list` command. Ensure there is at least one entry.

   1. Test case: `edit 1 p/90001111 s/INTERVIEW`<br>
      Expected: The first opportunity contact is updated with the new phone number and status. A success message is shown.

1. Clearing the phone number of an opportunity contact

   1. Prerequisites: Ensure the first displayed opportunity contact has a phone number.

   1. Test case: `edit 1 p/`<br>
      Expected: The phone number of the first displayed opportunity contact is cleared. A success message is shown.

1. Rejecting invalid edit commands

   1. Test case: `edit 0 s/OFFER`<br>
      Expected: No opportunity contact is edited. Error details are shown in the status message.

   1. Test case: `edit 1`<br>
   Expected: No opportunity contact is edited. An error message is shown: `At least one field to edit must be provided.`

1. Editing into a duplicate opportunity contact

   1. Prerequisites:
      1. Add the first record:
         `add n/Alex Tan e/alex.dup@test.com cr/recruiter c/Grab r/SWE Intern s/SAVED cy/SUMMER 2026`
      1. Add the second record:
         `add n/Ben Lim e/ben.unique@test.com cr/hiring manager c/Stripe r/Data Analyst s/APPLIED cy/WINTER 2026`
      1. Run:
         `list`<br><br>

   1. Test case:
      `edit 2 e/alex.dup@test.com c/Grab r/SWE Intern cy/SUMMER 2026`<br>
      Expected: No opportunity contact is edited. A duplicate-entry error is shown because the edited record now matches the first record in email, company, role, and cycle.

### Find command edge cases

1. Finding active opportunity contacts by name

   1. Prerequisites: Ensure the active list contains at least one contact whose name contains `Jane`.

   1. Test case: `find Jane`<br>
      Expected: Only active opportunity contacts whose names match `Jane` are shown. The `Main` tab is highlighted.

1. OR matching for multiple name keywords

   1. Prerequisites: Ensure the active list contains at least one contact named `Jane` and at least one named `David`.

   1. Test case: `find Jane David`<br>
      Expected: All active opportunity contacts whose names contain `Jane` or `David` are shown. The `Main` tab is highlighted.

1. Company-only search

   1. Prerequisites: Ensure the active list contains at least one contact whose company contains `Stripe`.

   1. Test case: `find c/Stripe`<br>
      Expected: Only active opportunity contacts whose company matches `Stripe` are shown. The `Main` tab is highlighted.

1. Archived company-only search

   1. Prerequisites: Ensure there is at least one archived opportunity contact whose company contains `Stripe`.

   1. Test case: `find a/ c/Stripe`<br>
      Expected: Only archived opportunity contacts whose company matches `Stripe` are shown. The `Archive` tab is highlighted.

1. Archived search by name and company

   1. Prerequisites: Ensure there is at least one archived opportunity contact whose name contains `Jane` and whose company contains `Stripe`.

   1. Test case: `find a/Jane c/Stripe`<br>
      Expected: Only archived opportunity contacts whose name contains `Jane` and whose company contains `Stripe` are shown. The `Archive` tab is highlighted.

1. Rejecting invalid find commands

   1. Test case: `find a/`<br>
      Expected: No search is performed. An invalid command format error is shown.

   1. Test case: `find google a/`<br>
   Expected: No search is performed. An error message is shown: `When using a/, keywords must appear after it (optionally separated by whitespace). Use 'find a/KEYWORD' or 'find a/ KEYWORD' to search archived by name, or 'find a/ c/COMPANY' to search archived by company.`

   1. Test case: `find alice a/bob`<br>
      Expected: No search is performed. An error message is shown: `Keywords cannot appear both before and after a/. Use 'find a/KEYWORD' or 'find a/ KEYWORD' to search archived opportunities.`

   1. Other invalid commands to try: `find`, `find c/`<br>
      Expected: No search is performed. Error details are shown in the status message.

1. Unsupported `find` prefixes are treated as plain name keywords

   1. Prerequisites: Ensure at least one active contact name contains `Jane`.

   1. Test case: `find Jane r/SWE`<br>
      Expected: The command is accepted as a name-keyword search (not as a prefixed role filter). Results match names containing
      `Jane` or `r/SWE` (typically only `Jane` matches). No unsupported-prefix-specific error is shown.

### Delete, archive, and unarchive edge cases

1. Deleting an opportunity contact

   1. Prerequisites: List all active opportunity contacts using the `list` command. Ensure there is at least one opportunity contact.

   1. Test case: `delete 1`<br>
      Expected: The first displayed opportunity contact is deleted. Details of the deleted opportunity contact are shown.

1. Duplicate indices

   1. Test case: `delete 1 1`<br>
      Expected: No opportunity contact is deleted. Error details are shown in the status message.

   1. Test case: `archive 1 1`<br>
      Expected: No opportunity contact is archived. Error details are shown in the status message.

   1. Test case: `unarchive 1 1`<br>
      Expected: No opportunity contact is unarchived. Error details are shown in the status message.

1. Other invalid delete commands

   1. Test case: `delete 0`<br>
      Expected: No opportunity contact is deleted. Error details are shown in the status message.

   1. Other incorrect commands to try: `delete`, `delete x`, `delete 999`<br>
      Expected: No opportunity contact is deleted. Error details are shown in the status message.

1. Archiving by index

   1. Prerequisites: List all active opportunity contacts using the `list` command. Ensure there is at least one entry.

   1. Test case: `archive 1`<br>
      Expected: The first active opportunity contact is archived and no longer shown in the active list. A success message is shown.

1. Cycle-based archive

   1. Prerequisites: Ensure there is at least one active opportunity contact with cycle `SUMMER 2026`.

   1. Test case: `archive cycle SUMMER 2026`<br>
      Expected: All active opportunity contacts with cycle `SUMMER 2026` are archived. A success message is shown.

   1. Test case: `archive cycle INVALID 2026`<br>
      Expected: No opportunity contact is archived. Error details are shown in the status message.

1. Wrong-view restrictions

   1. Prerequisites: Show archived opportunities using `list archive`.

   1. Test case: `archive 1`<br>
      Expected: No opportunity contact is archived. Error details are shown because archiving by index only works in the active list.

   1. Test case: `archive cycle SUMMER 2026`<br>
      Expected: No opportunity contact is archived. Error details are shown because cycle-based archiving also only works in the active list.

   1. Prerequisites: Show active opportunities using `list`.

   1. Test case: `unarchive 1`<br>
      Expected: No opportunity contact is unarchived. Error details are shown because unarchiving only works in the archived list.

1. Commands that work in both views

   1. Deleting from the archived list

      1. Prerequisites: Show archived opportunities using `list archive`. Ensure there is at least one entry.

      1. Test case: `delete 1`<br>
         Expected: The first displayed archived opportunity contact is permanently deleted. A success message is shown.

   1. Editing from the archived list

      1. Prerequisites: Show archived opportunities using `list archive`. Ensure there is at least one entry.

      1. Test case: `edit 1 s/INTERVIEW`<br>
         Expected: The first displayed archived opportunity contact is updated and remains archived. A success message is shown.

1. Unarchiving from the archived list

   1. Prerequisites: Show archived opportunity contacts using the `list archive` command. Ensure there is at least one entry.

   1. Test case: `unarchive 1`<br>
      Expected: The first archived opportunity contact is restored to the active list. A success message is shown.

1. Unarchiving from archived find results

   1. Prerequisites: Run `find a/Jane c/Stripe` and ensure there is at least one archived result shown.

   1. Test case: `unarchive 1`<br>
      Expected: The first displayed archived opportunity contact is restored to the active list. A success message is shown.

### Undo edge cases

1. Undoing a mutating command

   1. Prerequisites: Execute a valid mutating command such as `add`, `edit`, `delete`, `archive`, `unarchive`, or `clear`.

   1. Test case: `undo`<br>
      Expected: The most recent change is reverted. A success message is shown.

1. Read-only commands do not consume undo history

   1. Prerequisites: Execute a valid `add` command, then execute a read-only command such as `list` or `find`.

   1. Test case: `undo`<br>
      Expected: The earlier mutating command is undone.

1. Undoing when there is no history

   1. Prerequisites: Ensure the application has just launched and no mutating commands have been executed.

   1. Test case: `undo`<br>
      Expected: The tracker state remains unchanged. An error message `"No more commands to undo!"` is shown.

1. Rejecting an invalid undo command

   1. Test case: `undo 123`<br>
      Expected: The tracker state remains unchanged. An invalid command format error is shown.

### Clearing all entries

1. Clearing all entries

   1. Prerequisites: Ensure the tracker contains at least one opportunity contact.

   1. Test case: `clear`<br>
      Expected: All opportunity contacts, including archived ones, are removed. A success message is shown.

   1. Test case: `undo` immediately after `clear`<br>
      Expected: All cleared opportunity contacts are restored.

   1. Test case: `clear 123`<br>
      Expected: No opportunity contacts are removed. An invalid command format error is shown.

### Saving data and file recovery

1. Persistence after state-changing commands

   1. Perform one valid `add`, one valid `edit`, one valid `archive` or `unarchive`, and one valid `delete`.

   1. Close the application and reopen it.<br>
      Expected: All successful state changes are retained.

1. Missing data file

   1. Close the app.

   1. Delete `data/addressbook.json`.

   1. Reopen the app.<br>
      Expected: The app does not crash and loads sample data.

   1. Execute a valid mutating command such as an `add`.

   1. Check the `data` folder.<br>
      Expected: `addressbook.json` is recreated after the successful save.

1. Corrupted data file

   1. Close the app.

   1. Edit `data/addressbook.json` so that it is no longer valid JSON.

   1. Reopen the app.<br>
      Expected: The app does not crash. No previously saved opportunity contacts are loaded. The app starts with an empty data set because the invalid file content is discarded.

   1. Execute a valid mutating command such as:
      `add n/Test User e/test@example.com cr/recruiter c/TestCo r/SWE Intern s/SAVED cy/SUMMER 2026`

   1. Close and reopen the app again.<br>
      Expected: The newly added contact is present, showing that the corrupted file has been replaced by a valid saved data file.

1. Optional: save failure when the data file is not writable

   1. Prerequisites: Ensure `data/addressbook.json` already exists.

   1. Make `data/addressbook.json` read-only using your operating system's file permissions.

   1. Reopen the app and execute a valid mutating command such as:
      `add n/Test User e/test@example.com cr/recruiter c/TestCo r/SWE Intern s/SAVED cy/SUMMER 2026`<br>
      Expected: The app does not crash. The command is reverted, and a clear error message is shown indicating that the storage operation failed.

   1. Restore write permission to `data/addressbook.json`.

   1. Execute the same `add` command again.<br>
      Expected: The command now succeeds and the new contact is saved normally.

## **Appendix: Planned Enhancements**

Team size: 5

1. **Refine duplicate handling for shared-mailbox contacts in `add`**: The current `add` logic rejects any new record
when `Email + Company + Role + Cycle` matches an existing record, which can wrongly block distinct contacts who use
the same generic email (e.g., `internships@company.com`). We plan to keep a strict rejection only when **all fields**
 match after normalization (`Name`, `Email`, `ContactRole`, `Company`, `Role`, `Cycle`, `Status`, `Phone`), and
change partial matches on the core tuple (`Email + Company + Role + Cycle`) into a **duplicate warning** instead of
immediate rejection when at least one of `Name`/`ContactRole`/`Status`/`Phone` differs. Example: if `add n/Amy Lee
e/internships@stripe.com cr/recruiter c/Stripe r/SWE Intern s/APPLIED cy/SUMMER 2026` exists, then `add n/Ben Tan
e/internships@stripe.com cr/hiring manager c/Stripe r/SWE Intern s/OA cy/SUMMER 2026` should show a
possible-duplicate warning but still be allowed if the user decides to proceed with the addition or edit.

2. **Extend `find` to support additional prefixed filters with explicit errors for unsupported prefixes**: The current
`find` implementation supports name keywords by default and `c/` for company (with optional `a/` archive scope). Inputs
such as `find Jane r/SWE` are currently interpreted as plain name keywords instead of raising a targeted error for
unsupported filter prefixes. We plan to introduce richer prefixed filtering (e.g., role/status/cycle) and, in the
interim, provide clearer parser feedback when unsupported prefixes are used.

3. **Normalize internal whitespace in `Company` and `Role` fields and show a duplicate warning instead of immediate rejection**:
The current duplicate detection compares `Company` and `Role` using case-insensitive string matching but
does not collapse internal whitespace. This means entries such as `r/SWE Intern` and `r/SWE  Intern` (double space)
are treated as distinct, bypassing duplicate detection. We plan to normalize multiple consecutive spaces into a
single space (e.g., using `.replaceAll("\\s+", " ")`) when `Company` and `Role` values are created, so that
normalization is applied consistently to both CLI input and data loaded from JSON storage, similar to how `Cycle`
input is already normalized during parsing before the `Cycle` object is constructed. Additionally, when a duplicate is detected
as a result of this normalization (i.e., the user accidentally typed extra spaces), the system will show a
duplicate warning instead of an immediate hard rejection, so the user understands why their input was flagged.
Example: `add ... c/Google r/SWE  Intern ...` and `add ... c/Google r/SWE Intern ...` (with the same Email and Cycle)
should trigger a duplicate warning.

4. **Warn users when the data file is corrupted or unreadable on startup**: The current startup behavior follows
NFR 8 by not crashing and starting with an empty data set when an existing storage file cannot be read or parsed.
However, this can be confusing because users may not realize that their previous data file was invalid and may
accidentally overwrite recoverable data after the next successful autosave. We plan to revise this behavior so that
InternTrack shows a clear user-facing warning when the existing data file is corrupted or unreadable, explains that
the previous data could not be loaded, and advises the user to restore or repair the file from a backup if needed.
Where feasible, the app should also avoid overwriting the problematic file until the user has acknowledged the issue
or chosen to start afresh.

4. **Improve error messages for out-of-range index values**: Commands that use displayed list indices, such as
`delete`, `edit`, `archive`, and `unarchive`, currently parse indices as Java `int` values. When a user enters an
extremely large positive number outside the supported range, parsing fails before normal index validation and the app
may show a generic invalid command format message instead of the usual invalid index message. Although such index
values are not realistic for normal InternTrack usage, we plan to refine the parser to detect out-of-range numeric
index inputs and report them as invalid indices. This improves error-message consistency without changing the
practical number of opportunity contacts the app is expected to handle.

5. **Allow escaped forward slashes in slash-restricted text fields for `add` and `edit`**: The current parser and field validation block `/` in values such as `n/NAME`, so legitimate inputs such as `Asha s/o Kumar` cannot be stored. We plan to support escaped slashes by accepting `\/` as a literal slash during parsing, and to update the corresponding field validations so `/` is allowed in stored values for affected fields such as `Name` (and similarly `Company`, `Role`, and `ContactRole`, if they continue to share the same restriction). **Rule:** a literal slash in these fields is accepted only when written as `\/`; any unescaped `/` in these fields is rejected with a clear message to use `\/`. This keeps parser behavior deterministic and prevents accidental prefix interpretation (e.g., ` s/o` being parsed as the `s/` prefix).
Example: `add n/Asha s\/o Kumar e/asha@example.com cr/recruiter c/Acme r/SWE Intern s/SAVED cy/SUMMER 2026` should be parsed, pass validation, and be saved and displayed as `Asha s/o Kumar`. `add n/Asha s/o Kumar ...` (unescaped slash) should be rejected with guidance to use `\/`.
