package ru.netology.pages;

import com.codeborne.selenide.Condition;

public class CreditPage extends Page {

    public CreditPage() {}

    public void checkOperationApproved() {
        notificationSuccess.waitUntil(Condition.visible, 15000);
    }

    public void checkOperationDeclined() {
        notificationError.waitUntil(Condition.visible, 15000);
    }
}
