package com.debugps.people.intefaces;

import com.debugps.people.data.Contact;

public interface OnSettingContact {
    void setFavorited(Contact contact);
    void unsetFavorited(Contact contact);
    void callContact(Contact contact);
    void removeContact(Contact contact);
    void editContact(Contact contact);
}
