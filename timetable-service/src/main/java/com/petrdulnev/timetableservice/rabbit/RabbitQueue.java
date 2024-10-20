package com.petrdulnev.timetableservice.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class RabbitQueue {
    //Queue acc
    public static final String CHECK_ACCOUNT = "CHECK_ACCOUNT";
    public static final String GET_ID_FROM_TOKEN = "GET_ID_FROM_TOKEN";
    public static final String CHECK_DOCTOR = "CHECK_DOCTOR";

    //Queue Hospital
    public static final String CHECK_HOSPITAL_AND_ROOM = "CHECK_HOSPITAL_AND_ROOM";

    //Queue History
    public static final String CREATE_HOSPITAL = "CREATE_HOSPITAL";

    // for rabbit connection
    @RabbitListener(queues = "GET_ID_FROM_TOKEN")
    private void aVoid() {
    }
}
