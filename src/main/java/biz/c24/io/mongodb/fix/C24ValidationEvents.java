package biz.c24.io.mongodb.fix;

import biz.c24.io.api.data.ValidationEvent;

import java.util.List;

public interface C24ValidationEvents {
    
    public List<ValidationEvent> getPassEvents();
    public List<ValidationEvent> getFailEvents();
}
