package com.nerothtr.githubrepo;

import android.app.Application;

import com.nerothtr.githubrepo.response.Owner;
import com.nerothtr.githubrepo.response.Repository;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.annotations.RealmModule;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .modules(new MyRealmModule())
                .schemaVersion(0)
//                .migration(new MyMigration())
                .name("github.realm")
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

    }

    @RealmModule(classes = {Repository.class, Owner.class})
    public static class MyRealmModule {
    }

    private static class MyMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();


            if (oldVersion == 0) {
                RealmObjectSchema repository = schema.get("Repository");
                repository.addField("id_tmp", String.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject obj) {
                                int oldType = obj.getInt("id");
                                if (oldType == 1) {
                                    obj.setString("id_tmp", "1");
                                } else if (oldType == 2) {
                                    obj.setString("id_tmp", "2");
                                } else if (oldType == 3) {
                                    obj.setString("id_tmp", "3");
                                }
                            }
                        }).removeField("id")
                        .renameField("id_tmp", "id");
                oldVersion++;
            }

            if (oldVersion == 1) {
                RealmObjectSchema repository = schema.get("Repository");
                repository.addField("id_tmp", String.class, FieldAttribute.PRIMARY_KEY).removeField("id")
                        .renameField("id_tmp", "id");
                oldVersion++;
            }

            if (oldVersion == 2) {
                RealmObjectSchema realmObjectSchema = schema.get("Repository");
                realmObjectSchema.addField("avatarUrl", String.class);
                oldVersion++;
            }

            if (oldVersion == 3) {
                RealmObjectSchema realmObjectSchema = schema.get("Repository");
                realmObjectSchema.removeField("avatarUrl");
                oldVersion++;
            }

            if (oldVersion == 4) {
                RealmObjectSchema repository = schema.get("Repository");
                repository.addField("id_tmp", Integer.class)
                        .transform(obj -> {
                            String oldType = obj.getString("id");
                            if (oldType.equals("one")) {
                                obj.setInt("id_tmp", 4);
                            } else if (oldType.equals("two")) {
                                obj.setInt("id_tmp", 5);
                            } else if (oldType.equals("three")) {
                                obj.setInt("id_tmp", 6);
                            }
                        }).removeField("id")
                        .renameField("id_tmp", "id");
                oldVersion++;
            }

            if (oldVersion == 5) {
                RealmObjectSchema repository = schema.get("Repository");
                repository.addField("id_temp", Integer.class, FieldAttribute.PRIMARY_KEY)
                        .transform(obj -> {
                            String oldType = obj.getString("id");
                            if (oldType.equals("one")) {
                                obj.setInt("id_tmp", 4);
                            } else if (oldType.equals("two")) {
                                obj.setInt("id_tmp", 5);
                            } else if (oldType.equals("three")) {
                                obj.setInt("id_tmp", 6);
                            }
                        })
                        .removeField("id")
                        .renameField("id_temp", "id");

                oldVersion++;
            }
        }
    }
}
