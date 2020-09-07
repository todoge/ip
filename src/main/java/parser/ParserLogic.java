package parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import king.KingException;
import storage.Storage;
import storage.StorageException;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.TaskList;
import tasks.ToDo;
import ui.UI;

public class ParserLogic {

    private final ParserExceptions exceptions = new ParserExceptions();
    private final Storage storage;
    private TaskList taskList;

    ParserLogic(TaskList taskList, Storage storage){
        this.taskList = taskList;
        this.storage = storage;
    }

    /**
     * Parse the "Bye" command from the user.
     *
     * @return String String Reply from King.
     */
    public String parseBye(){
        return UI.emptyChatBox("Bye! Come back soon.");
    }

    /**
     * Parse the "clear list" command from the user.
     * Clears the taskList data in storage.
     *
     * @return String Reply from King.
     */
    public String parseClearList() throws StorageException{
        taskList.clear();
        storage.persistTaskList(taskList);
        return UI.emptyChatBox("I have cleared the list!");
    }

    /**
     * Parse the "list" command from the user.
     *
     * @return String Reply from King.
     */
    public String parseList(){
        return UI.showTaskList(taskList);
    }

    /**
     * Parse the "todo <task>" command from the user.
     * Updates the data in storage.
     *
     * @param command Phrase entered by user.
     * @return String Reply from King.
     * @throws KingException Throws KingException for invalid commands.
     * @throws StorageException Exception from writing to asset file.
     */
    public String parseToDo(String command) throws KingException, StorageException {
        String task = command.substring(4).trim();
        if (!task.isEmpty()) {
            ToDo todo = new ToDo(task);
            taskList.add(todo);
            storage.persistTaskList(taskList);
            return UI.addItemChatBox(todo.toString(), taskList.size());
        } else {
            throw exceptions.emptyTodoException();
        }
    }

    /**
     * Parse the "event" command from the user.
     * Updates the data in storage.
     *
     * @param command Phrase entered by user.
     * @return String Reply from King.
     * @throws KingException Throws KingException for invalid commands.
     * @throws StorageException Exception from writing to asset file.
     */
    public String parseEvent(String command) throws KingException, StorageException {
        String item = command.substring(5).trim();
        String[] tokens = item.split(" /at ");
        if (tokens.length == 2) {
            Event event = new Event(tokens[0], tokens[1]);
            taskList.add(event);
            storage.persistTaskList(taskList);
            return UI.addItemChatBox(event.toString(), taskList.size());
        } else {
            throw exceptions.badEventSyntaxException();
        }
    }

    /**
     * Parse the "deadline" command from the user.
     * Updates the data in storage.
     *
     * @param command Phrase entered by user.
     * @return String Reply from King.
     * @throws KingException Throws KingException for invalid commands.
     * @throws StorageException Exception from writing to asset file.
     */
    public String parseDeadline(String command) throws KingException, StorageException {
        String item = command.substring(8).trim();
        String[] tokens = item.split(" /by ");
        if (tokens.length != 2) {
            throw exceptions.badDeadlineSyntaxException();
        }
        try {
            LocalDateTime datetime = stringToLocalDateTime(tokens[1]);
            Deadline deadline = new Deadline(tokens[0], datetime);
            taskList.add(deadline);
            storage.persistTaskList(taskList);
            return UI.addItemChatBox(deadline.toString(), taskList.size());
        } catch (KingException badDateTimeSyntax) {
            throw badDateTimeSyntax;
        }
    }

    /**
     * Parse the "delete" command from the user.
     * Updates the data in storage.
     *
     * @param command Phrase entered by user.
     * @return String Reply from King.
     * @throws KingException Throws KingException for invalid commands.
     * @throws StorageException Exception from writing to asset file.
     */
    public String parseDelete(String command) throws KingException, StorageException{
        System.out.println(command);
        String stringItem = command.substring(6).trim();
        try {
            int itemNo = Integer.parseInt(stringItem) - 1;
            Task item = taskList.get(itemNo);
            taskList.delete(itemNo);
            storage.persistTaskList(taskList);
            return UI.deleteItemChatBox(item.toString(), taskList.size());
        } catch (IndexOutOfBoundsException e) {
            throw exceptions.itemNotFoundException(stringItem, e);
        } catch (NumberFormatException e) {
            throw (stringItem.isEmpty())
                    ? exceptions.deleteNotFollowedByNumberException()
                    : exceptions.invalidNumberException(stringItem, e);
        } catch (StorageException e) {
            throw e;
        }
        catch (Exception e) {
            throw exceptions.badDeleteSyntaxException();
        }
    }

    /**
     * Parse the "find" command from the user.
     * Updates the data in storage.
     *
     * @param command Phrase entered by user.
     * @return String Reply from King.
     * @throws StorageException Exception from writing to asset file.
     */
    public String parseFind(String command) throws StorageException {
        String[] keywords = command.substring(4).trim().split(" ");
        return UI.showFoundItems(storage.find(keywords));
    }

    /**
     * Parse the "done" command from the user.
     * Updates the data in storage.
     *
     * @param command Phrase entered by user.
     * @return String Reply from King.
     * @throws KingException Throws KingException for invalid commands.
     * @throws StorageException Exception from writing to asset file.
     */
    public String parseDone(String command) throws KingException, StorageException {
        String stringItem = command.substring(4).trim();
        try {
            int taskNumber = Integer.parseInt(stringItem) - 1;
            Task item = taskList.get(taskNumber);
            item.markAsDone();
            storage.persistTaskList(taskList);
            return UI.emptyChatBox(item.toString());
        } catch (IndexOutOfBoundsException e) {
            throw exceptions.itemNotFoundException(stringItem, e);
        } catch (NumberFormatException e) {
            throw (stringItem.isEmpty())
                    ? exceptions.doneNotFollowedByNumberException()
                    : exceptions.invalidNumberException(stringItem, e);
        } catch (StorageException e) {
            throw e;
        } catch (Exception e) {
            throw exceptions.badDoneSyntaxException();
        }
    }

    // takes a Date Time string and returns a LocalDateTime
    private LocalDateTime stringToLocalDateTime(String localDateTime) throws KingException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");
            return LocalDateTime.parse(localDateTime, formatter);
        } catch (Exception e) {
            throw exceptions.badLocalDateTimeException(e);
        }
    }
}