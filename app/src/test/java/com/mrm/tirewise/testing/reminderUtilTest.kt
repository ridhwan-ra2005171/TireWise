package com.mrm.tirewise.testing

import com.google.common.truth.Truth.assertThat
import org.junit.Test
class reminderUtilTest{

    @Test
    fun `upsertReminder - empty reminder name returns false`(){
        val result = ReminderUtil.validateReminderInput(
            "fasdf",
            "sadfb",
            "2341asdf",
            "",
            "24-02-2023",
            "12:00",
            "reminder note",
        )
        //import the one from 'truth'
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertReminder - valid input for all reminder field returns true`(){
        val result = ReminderUtil.validateReminderInput(
            "fasdfnew",
            "htrs-fdgh-8793",
            "sdfasdf1321",
            "Oil Change",
            "24-02-2023",
            "12:00",
            "Change oil after 5000km",
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `upsertReminder - userId does not exist from existing user returns false`(){
        val result = ReminderUtil.validateReminderInput(
            "fasdfnew",
            "htrs-fdgh-1234",
            "sdfasdf1321",
            "Oil Change",
            "24-02-2023",
            "12:00",
            "Change oil after 5000km",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertReminder - reminderId already exist returns false`(){
        val result = ReminderUtil.validateReminderInput(
            "asdf-sadf-1321",
            "htrs-fdgh-8793",
            "sdfasdf1321",
            "Oil Change",
            "24-02-2023",
            "12:00",
            "Change oil after 5000km",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertReminder - vehicleId does not exist returns false`(){
        val result = ReminderUtil.validateReminderInput(
            "asdf-sadf-1321",
            "htrs-fdgh-8793",
            "",
            "Oil Change",
            "24-02-2023",
            "12:00",
            "Change oil after 5000km",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `getReminders - non existing userId returns false`(){
        val result = ReminderUtil.validategetReminders(
            userId=""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `getReminders -  valid existing userId returns true`(){
        val result = ReminderUtil.validategetReminders(
            userId="htrs-fdgh-8793"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `getReminder - non existing userId returns false`(){
        val result = ReminderUtil.validategetReminder(
            userId=""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `getReminder - valid existing userId returns true`(){
        val result = ReminderUtil.validategetReminder(
            userId="htrs-fdgh-8793"
        )
        assertThat(result).isTrue()
    }


    @Test
    fun `deleteReminder - non existing reminderId returns false`(){
        val result = ReminderUtil.validateDeleteReminder(
            reminderId = ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `deleteReminder - valid reminderId return true`(){
        val result = ReminderUtil.validateDeleteReminder(
            reminderId = "ashe-hdff-7562"
        )
        assertThat(result).isTrue()
    }


}