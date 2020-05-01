package com.example.ttett.util.charsort;

import android.util.Log;

import com.example.ttett.Entity.Contact;
import com.example.ttett.util.sidebar.ContactComparator;
import com.example.ttett.util.sidebar.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SortUtils {

    private static List<Contact> resultList; // 最终结果（包含分组的字母）
    public static List<String> characterList; // 字母List

    public enum ITEM_TYPE {
        ITEM_TYPE_CHARACTER,
        ITEM_TYPE_CONTACT
    }
    public static List<Contact> contactNameSort(List<Contact> contacts){
        handleContact(contacts);
        return resultList;
    }

    private static void handleContact(List<Contact> contacts) {
        // 联系人名称List（转换成拼音）
        List<String> mContactList = new ArrayList<>();
        Map<String, Contact> map = new HashMap<>();

        for (int i = 0; i < contacts.size(); i++) {
            String pinyin = Utils.getPingYin(contacts.get(i).getName());
            map.put(pinyin, contacts.get(i));
            mContactList.add(pinyin);
        }
        Collections.sort(mContactList, new ContactComparator());
        for (String c: mContactList){
            Log.d("dialog",c);
        }

        resultList = new ArrayList<>();
        characterList = new ArrayList<>();

        for (int i = 0; i < mContactList.size(); i++) {
            String name = mContactList.get(i);
            String character = (name.charAt(0) + "").toUpperCase(Locale.ENGLISH);
            if (!characterList.contains(character)) {
                if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                    characterList.add(character);
                    resultList.add(new Contact(character, ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()));
                } else {
                    if (!characterList.contains("#")) {
                        characterList.add("#");
                        resultList.add(new Contact("#", ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()));
                    }
                }
            }
            Objects.requireNonNull(map.get(name)).setmType(ITEM_TYPE.ITEM_TYPE_CONTACT.ordinal());
            resultList.add(map.get(name));
        }
    }
}
