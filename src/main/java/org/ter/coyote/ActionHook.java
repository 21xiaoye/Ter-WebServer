package org.ter.coyote;

public interface ActionHook {
    void action(ActionCode actionCode, Object param);
}