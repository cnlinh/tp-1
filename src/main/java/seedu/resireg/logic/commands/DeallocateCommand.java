package seedu.resireg.logic.commands;

import seedu.resireg.commons.core.Messages;
import seedu.resireg.commons.core.index.Index;
import seedu.resireg.logic.commands.exceptions.CommandException;
import seedu.resireg.model.Model;
import seedu.resireg.model.room.Room;
import seedu.resireg.model.student.Student;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.resireg.logic.parser.CliSyntax.PREFIX_ROOM_INDEX;
import static seedu.resireg.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;


/**
 * Adds a student to the address book.
 */
public class DeallocateCommand extends Command {

    public static final String COMMAND_WORD = "deallocate";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deallocates a student from a room. \n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Room deallocated from %1$s: %2$s.";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "This student is not registered in ResiReg";
    public static final String MESSAGE_STUDENT_NOT_ALLOCATED = "This student has not been allocated a room.";

    private final Index studentIndex;

    /**
     * @param studentIndex of the student in the filtered student list to deallocate a room from
     * Creates an DeallocateCommand to deallocate the specified {@code Student}.
     */
    public DeallocateCommand(Index studentIndex) {
        requireNonNull(studentIndex);
        this.studentIndex = studentIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownListStudent = model.getFilteredStudentList();

        if (studentIndex.getZeroBased() >= lastShownListStudent.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Student studentToDeallocate = lastShownListStudent.get(studentIndex.getZeroBased());
        Room roomToDeallocate = studentToDeallocate.getRoom();

        if (!model.hasStudent(studentToDeallocate)) {
            throw new CommandException(MESSAGE_STUDENT_NOT_FOUND);
        } else if (roomToDeallocate == null) {
            throw new CommandException(MESSAGE_STUDENT_NOT_ALLOCATED);
        }

        studentToDeallocate.unsetRoom();
        roomToDeallocate.unsetStudent();

        model.setStudent(studentToDeallocate, studentToDeallocate);
        model.setRoom(roomToDeallocate, roomToDeallocate);

        model.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredRoomList(Model.PREDICATE_SHOW_ALL_ROOMS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, studentToDeallocate, roomToDeallocate));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeallocateCommand // instanceof handles nulls
                && studentIndex.equals(((DeallocateCommand) other).studentIndex));
    }
}
