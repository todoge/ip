/**
 * Handles all the UI design of the replies generated by King.
 * The text decorations are all handled by the UI.
 * Long texts are automatically wrapped by the UI and texts
 * are wrapped with a buffer to maintain the words in the text.
 *
 * @author jun siang
 */
package ui;

import tasks.TaskList;

public class UI {

    // maximum number of characters across a screen
    public static final int CHAT_WIDTH = 50;

    // Buffer to wrap text to the next line if the text if too long.
    // This buffer helps to keep words intact while wrapping the text
    // to the next line.
    public static final int BUFFER = CHAT_WIDTH - 6;

    /**
     * Returns the Welcome message and logo.
     *
     * @return String welcome message.
     */
    public static String welcome() {
        String logo =
                " ____  __.__\n"
                        + "|    |/ _|__| ____    ____\n"
                        + "|      < |  |/    \\  / ___\\\n"
                        + "|    |  \\|  |   |  \\/ /_/  >\n"
                        + "|____|__ \\__|___|  /\\___  /\n"
                        + "        \\/       \\//_____/\n";

        return logo + "\n" + "Hello! I'm King!\nWhat can I do for you?";
    }

    /**
     * Returns a chat box for KING wrapped around a given text.
     *
     * @param chatContent text string.
     * @return String chat box around text.
     * @see king.King
     * @deprecated no longer in use after moving UI to javafx.
     */
    @Deprecated
    public static String kingChatBox(String chatContent) {
        return "\t" + printKingBorder("King says", 5, '-') + "\n\t"
                + formatStringIfLong(chatContent) + "\n"
                + "\t" + printKingBorder("", CHAT_WIDTH, '-') + "\n";
    }

    /**
     * Returns a chat box for KING wrapped around a given text.
     *
     * @param chatContent text string.
     * @return String chat box around text.
     * @see king.King
     * @deprecated
     */
    @Deprecated
    public static String userChatBox(String chatContent) {
        return "\t" + printKingBorder("", CHAT_WIDTH, '=') + "\n\t"
                + formatStringIfLong(chatContent) + "\n"
                + "\t" + printKingBorder("", CHAT_WIDTH, '=') + "\n";
    }

    /**
     * Returns an empty chat box.
     *
     * @param chatContent text string.
     */
    public static String emptyChatBox(String chatContent) {
        return chatContent;
    }

    /**
     * Returns an error box wrapped around an error message.
     *
     * @param error error message.
     * @return String error box around error message.
     */
    public static String errorBox(String error) {
        return printKingBorder("Error Encountered", 4, '=') + "\n\t"
                + formatStringIfLong(error) + "\n"
                + printKingBorder("", CHAT_WIDTH, '=') + "\n";
    }

    /**
     * Returns a chat box wrap around a message when the user successfully adds an item.
     *
     * @param chatContent text String.
     * @param numOfItems  number of items in the new TaskList.
     * @return String chat box after adding item.
     * @see TaskList
     */
    public static String addItemChatBox(String chatContent, int numOfItems) {
        return emptyChatBox("Got it. I've added this task:\n"
                + "\t" + chatContent
                + "\nNow you have " + numOfItems + " tasks in the list.");
    }

    /**
     * Returns a chat box wrap around a message when the user successfully deletes an item.
     *
     * @param chatContent    text String.
     * @param numOfItemsLeft number of items left in the new TaskList.
     * @return String chat box around text.
     * @see TaskList
     */
    public static String deleteItemChatBox(String chatContent, int numOfItemsLeft) {
        return emptyChatBox("I have deleted the following item:\n"
                + "\t" + chatContent
                + "\nYou got " + numOfItemsLeft + " task(s) left.");
    }

    /**
     * Returns a chat box wrap around a message when the user sets an item as DONE.
     *
     * @param chatContent text String.
     * @return String chat box around text.
     * @see tasks.Task
     */
    public static String doneChatBox(String chatContent) {
        return emptyChatBox("Nice! I've marked this task as done:\n\t"
                + chatContent
        );
    }

    /**
     * Returns a formatted chat string containing the items in the TaskList.
     *
     * @param taskList the task list to be printed.
     * @return String formatted String with items in the taskList.
     */
    public static String showTaskList(TaskList taskList) {
        StringBuilder formatted = new StringBuilder();
        int len = taskList.size();
        for (int number = 0; number < len; number++) {
            formatted.append("\n\t  " + (number + 1) + ". " + taskList.get(number).toString());
        }
        return emptyChatBox("There are " + len + " items in your list:"
                + formatted.toString());
    }

    /**
     * Returns a formatted string containing items found in the list.
     *
     * @param taskList taskList of the items found.
     * @return String formatted printable list
     */
    public static String showFoundItems(TaskList taskList) {
        StringBuilder formatted = new StringBuilder();
        int len = taskList.size();
        for (int number = 0; number < len; number++) {
            formatted.append("\n\t  " + (number + 1) + ". " + taskList.get(number).toString());
        }
        return emptyChatBox("I found " + len + " items with the given keyword(s):"
                + formatted.toString());
    }

    // folds a string if it is too long to fit into the chat box
    private static String formatStringIfLong(String unformattedString) {
        int len = unformattedString.length();
        if (len > CHAT_WIDTH) {
            StringBuilder longString = new StringBuilder(unformattedString);
            int processedLength = 0;
            int pos = 0;
            while (pos != longString.length()) {
                char c = longString.charAt(pos);
                if (c == '\n') {
                    processedLength = 0;
                    pos++;
                } else if (c == '\t') {
                    pos++;
                } else if ((processedLength >= BUFFER && c == ' ') || processedLength == CHAT_WIDTH) {
                    longString.insert(pos, "\n\t");
                    pos += 4;
                    processedLength = 1;
                } else if (processedLength == 0 && c == ' ') {
                    longString.deleteCharAt(pos);
                } else {
                    pos++;
                    processedLength++;
                }
            }
            return longString.toString();
        } else {
            return unformattedString;
        }
    }

    private static String printKingBorder(String text, int posOfText, char symbol) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < posOfText; i++) {
            result.append(symbol);
        }
        result.append(text);
        for (int i = result.length(); i < CHAT_WIDTH; i++) {
            result.append(symbol);
        }
        return result.toString();
    }
}
