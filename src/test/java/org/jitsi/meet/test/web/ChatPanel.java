/*
 * Copyright @ 2018 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.meet.test.web;

import org.jitsi.meet.test.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.*;

import static org.testng.AssertJUnit.fail;

/**
 * Represents the chat panel in a particular {@link WebParticipant}.
 *
 * @author Boris Grozev
 */
public class ChatPanel
{
    /**
     * Converts boolean to "open" or "closed" text.
     *
     * @param isOpen
     * @return "open" or "closed".
     */
    static private String openClosedStr(boolean isOpen)
    {
        return isOpen ? "open" : "closed";
    }

    /**
     * The participant.
     */
    private final WebParticipant participant;

    /**
     * Initializes a new {@link ChatPanel} instance.
     * @param participant the participant for this {@link ChatPanel}.
     */
    ChatPanel(WebParticipant participant)
    {
        this.participant = Objects.requireNonNull(participant, "participant");
    }

    /**
     * @return {@code true} if the chat panel is open/visible and {@code false}
     * otherwise.
     */
    public boolean isOpen()
    {
        Object o = participant.executeScript("return APP.UI.isChatVisible();");
        return o != null && Boolean.parseBoolean(o.toString());
    }

    /**
     * Clicks on the "chat" toolbar button which opens or closes the chat panel.
     */
    public void clickToolbarButton()
    {
        MeetUIUtils.clickOnButton(
            participant.getDriver(),
            "toolbar_button_chat",
            true);
    }

    /**
     * Presses the "chat" keyboard shortcut which opens or closes the chat
     * panel.
     */
    public void pressShortcut()
    {
        participant.pressShortcut('c');
    }

    /**
     * Will wait until chat panel is opened or closed. If the opened/closed
     * condition is not met after given timeout it will fail the test.
     *
     * @param howManySeconds - How many seconds, before the wait will fail
     * the test.
     * @param isOpen - Will it wait for the chat to be opened opr closed ?
     */
    public void waitForOpenedOrClosed(int howManySeconds, final boolean isOpen)
    {
        WebDriverWait wait = new WebDriverWait(participant.getDriver(), 2);
        try
        {
            wait.until((ExpectedCondition<Boolean>) d -> isOpen == isOpen());
        }
        catch (TimeoutException exc)
        {
            fail(
                String.format(
                    "The chat was expected to be %s"
                        + ", but was %s after %d seconds of waiting.",
                    openClosedStr(isOpen),
                    openClosedStr(!isOpen),
                    howManySeconds));
        }
    }
}