package com.ideaout.im.domain;

public class Conversation {
    private Integer id;

    private Integer fromUserId;

    private Integer toUserId;

    private String lastText;

    private Integer state;

    private Integer fromUnreadCount;

    private Long timestamp;

    private String extra;

    private Integer toUnreadCount;

    private Integer fromUnreadAddCount;
    private Integer toUnreadAddCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getLastText() {
        return lastText;
    }

    public void setLastText(String lastText) {
        this.lastText = lastText == null ? null : lastText.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getFromUnreadCount() {
        return fromUnreadCount;
    }

    public void setFromUnreadCount(Integer fromUnreadCount) {
        this.fromUnreadCount = fromUnreadCount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra == null ? null : extra.trim();
    }

    public Integer getToUnreadCount() {
        return toUnreadCount;
    }

    public void setToUnreadCount(Integer toUnreadCount) {
        this.toUnreadCount = toUnreadCount;
    }

    public Integer getFromUnreadAddCount() {
        return fromUnreadAddCount;
    }

    public void setFromUnreadAddCount(Integer fromUnreadAddCount) {
        this.fromUnreadAddCount = fromUnreadAddCount;
    }

    public Integer getToUnreadAddCount() {
        return toUnreadAddCount;
    }

    public void setToUnreadAddCount(Integer toUnreadAddCount) {
        this.toUnreadAddCount = toUnreadAddCount;
    }
}