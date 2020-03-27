package com.cscec.model.ext;

import com.cscec.model.User;
import java.time.LocalDateTime;
public class UserExt  extends User {

    private String randomId;

    private LocalDateTime lastActiveTime;// 最后活动时间


    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    public LocalDateTime getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(LocalDateTime lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }
}
