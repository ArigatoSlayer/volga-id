package com.petrdulnev.timetableservice.validation;

import com.petrdulnev.timetableservice.model.Timetable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeValidator implements ConstraintValidator<TimeValidation, Timetable> {

    @Override
    public boolean isValid(Timetable timetable, ConstraintValidatorContext constraintValidatorContext) {
        int fromMinutes = timetable.getFrom().getMinute();
        int toMinutes = timetable.getTo().getMinute();

        int fromSeconds = timetable.getFrom().getSecond();
        int toSeconds = timetable.getTo().getSecond();

        int fromHours = timetable.getFrom().getHour();
        int toHours = timetable.getTo().getHour();

        if (fromMinutes % 30 != 0 && toMinutes % 30 != 0) {
            return false;
        }

        if (fromSeconds != 0 && toSeconds != 0) {
            return false;
        }

        if (!timetable.getFrom().isBefore(timetable.getTo())) {
            return false;
        }

        if (toHours - fromHours > 12) {
            return false;
        }

        return true;
    }
}
