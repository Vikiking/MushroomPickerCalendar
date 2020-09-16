package com.example.mushroompickercalendar;

import java.util.UUID;

public class CodeConvert {

    UUID mId;

    public CodeConvert() {
        this(UUID.randomUUID());
    }

    public CodeConvert(UUID id) {
        mId = id;
    }

}
