package com.ideaout.im.enumtype;

public enum NotifyType {
    ALL(0, "通用"),
    TaskTakeCheck(1, "接单者做单审核结果通知"),
    TaskTakeAppeal(2, "接单者做单申诉结果通知"),
    Withdraw(3, "提现成功通知"),
    AccountUnNormal(4, "账号异常通知"),
    TaskStateChange(5, "发布任务状态变化通知"),
    MoneyGiveBack(6, "任务资金退回通知"),
    NewTaskTake(7, "有的接单提交通知"),
    Refund(8, "退款"),
    InviteCodeProgress(9, "邀请码进度"),
    ;


    public int value;
    public String desc;

    NotifyType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static NotifyType getOrderType(String value) {
        NotifyType[] orderTypes = NotifyType.values();
        for (NotifyType orderType : orderTypes) {
            if (orderType.value == Integer.parseInt(value)) {
                return orderType;
            }
        }
        return NotifyType.ALL;
    }
}
