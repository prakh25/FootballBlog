package com.example.corelib.realm;

import com.example.corelib.model.tags_list.CategoriesOrTag;
import com.example.corelib.realm.model.RealmCategories;
import com.example.corelib.realm.model.RealmTags;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by prakh on 20-11-2017.
 */

public class RealmManager {

    public static final String CATEGORY_ID = "id";

    private static RealmManager instance;
    private final Realm realm;

    public static RealmManager getInstance() {
        if (instance == null) {
            instance = new RealmManager();
        }
        return instance;
    }

    private RealmManager() {
        realm = Realm.getDefaultInstance();
    }


    /*
     * functions for tags table
     */
    public List<Integer> getTagsList() {
        RealmResults<RealmTags> realmTags =
                realm.where(RealmTags.class).findAllAsync();
        List<Integer> tagsId = new ArrayList<>();
        for(RealmTags tags: realmTags) {
            tagsId.add(tags.getId());
        }
        return tagsId;
    }

    public void saveTags(CategoriesOrTag item) {
        realm.executeTransactionAsync(realm1 -> {
            RealmTags realmTags = findInRealmTags(realm1, item.getId());
            if(realmTags == null) {
                realmTags = realm1.createObject(RealmTags.class, item.getId());
                realmTags.setCount(item.getCount());
                realmTags.setName(item.getName());
            }
        });
    }

    public void removeTags(CategoriesOrTag item) {
        realm.executeTransactionAsync(realm1 -> {
            RealmTags realmTags = findInRealmTags(realm1, item.getId());
            realmTags.deleteFromRealm();
        });
    }

    public boolean hasTags() {
        return (realm.where(RealmTags.class).findAll().size() > 0);
    }

    private RealmTags findInRealmTags(Realm realm, Integer categoryId) {
        return realm.where(RealmTags.class).equalTo(CATEGORY_ID, categoryId)
                .findFirst();
    }

    /*
     * functions for categories table
     */

    public List<Integer> getCategoriesList() {
        RealmResults<RealmCategories> realmCategories =
                realm.where(RealmCategories.class).findAllAsync();
        List<Integer> categoriesId = new ArrayList<>();
        for(RealmCategories categories: realmCategories) {
            categoriesId.add(categories.getId());
        }
        return categoriesId;
    }

    public void saveCategory(CategoriesOrTag item) {
        realm.executeTransactionAsync(realm1 -> {
            RealmCategories realmCategories = findInRealmCategory(realm1, item.getId());
            if(realmCategories == null) {
                realmCategories = realm1.createObject(RealmCategories.class, item.getId());
                realmCategories.setCount(item.getCount());
                realmCategories.setName(item.getName());
            }
        });
    }

    public void removeCategory(CategoriesOrTag item) {
        realm.executeTransactionAsync(realm1 -> {
            RealmCategories realmCategories = findInRealmCategory(realm1, item.getId());
            realmCategories.deleteFromRealm();
        });
    }

    public boolean hasCategory() {
        return (realm.where(RealmCategories.class).findAll().size() > 0);
    }

    private RealmCategories findInRealmCategory(Realm realm, Integer categoryId) {
        return realm.where(RealmCategories.class).equalTo(CATEGORY_ID, categoryId)
                .findFirst();
    }

}
