package su.ias.malina.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import su.ias.malina.data.CategoryData;
import su.ias.malina.data.PartnerData;
import su.ias.malina.data.PartnerDataSimplified;
import su.ias.malina.data.UserCardItem;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA. User: n.senchurin Date: 24.02.14 Time: 17:12
 */
public class DBAdapter {


    private static final String DATABASE_NAME = "malina_data.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PARTNERS = "partners_table";
    public static final String FLD_ID = "_id";
    public static final String FLD_PARTNER_NAME = "partner_name";
    public static final String FLD_CODE = "code";
    public static final String FLD_PARTNER_DESCR = "partner_descr";
    public static final String FLD_REGION = "partner_region";
    public static final String FLD_LOGO_URL = "icon_url";
    public static final String FLD_IS_CARD_EMITTER = "is_emitter";
    public static final String FLD_HAS_TRANSACTIONS = "has_transactions";
    public static final String FLD_HAS_POINTS = "has_points";
    public static final String FLD_PARTNER_TYPE = "partner_type";
    public static final String FLD_ORDER = "fld_order";
    public static final String FLD_FILTER_FLAG = "fld_filter_flag";
    public static final String FLD_POINTS_RULE = "fld_points_rule";

    public static final String TABLE_POINTS = "points_table";
    public static final String FLD_LONG = "longitude";
    public static final String FLD_LAT = "latitude";
    public static final String FLD_PARTNER_ID = "partner_id";
    public static final String FLD_ICON = "icon";
    public static final String FLD_ADDRESS = "address";
    public static final String FLD_NAME = "name";
    public static final String FLD_MOVING_MODE = "moving_mode";
    public static final String FLD_DISTANCE = "distance";


    public static final String TABLE_CATEGORIES = "categories_table";
    public static final String FLD_CATEGORY_PARENT_ID = "category_parent_id";


    /*
     * {"is_irs_enabled":true,"registered_beeline_phone":"79067989698","card":
     * "6393000037606334","type":"Карта МАЛИНА®","status":""}
     */
    public static final String TABLE_USER_CARDS = "cards_table";
    public static final String FLD_CARD_CODE = "card_code";
    public static final String FLD_CARD_PIC_TYPE = "card_pic_type";
    public static final String FLD_CARD_STATUS = "card_status";
    public static final String FLD_IRS_ENABLED = "card_irs_enabled";
    public static final String FLD_BEELINE_PHONE = "card_beeline_phone";


    // Переменная для хранения объекта БД
    private SQLiteDatabase db;

    // Контекст приложения для
    private final Context context;

    // Экземпляр вспомогательного класса для открытия и обновления БД
    private CDBHelper dbHelper;

    private boolean transactionOpened;
    public static final String TYPE_OFFLINE = "offline";
    public static final String TYPE_ONLINE = "online";
    public static final String TYPE_ALL = "all";

    public DBAdapter(Context _context) {

        context = _context;
        dbHelper = new CDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        transactionOpened = false;
    }

    /**
     * Либо создаем новую бд, либо берем существующую
     *
     * @return
     * @throws android.database.SQLException
     */
    public DBAdapter open() throws SQLException {

        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = dbHelper.getReadableDatabase();
        }

        return this;
    }


    public void close() {
        db.close();
    }


    public void clearTable(String tableName) {
        db.delete(tableName, null, null);
    }


    public void deletePointsForMode(int movingType) {
        db.delete(TABLE_POINTS, FLD_MOVING_MODE + " =? ", new String[]{String.valueOf(movingType)});
    }




    public HashSet<Integer> getEnabledPartnersIdsByRegion(String region) {

        ArrayList<Integer> arr = new ArrayList<Integer>();

        String selection = FLD_REGION + " =? ";
        String[] selectionArgs = new String[]{region};

        Cursor partnersCursor = db.query(TABLE_PARTNERS, null, selection, selectionArgs, null, null, FLD_PARTNER_NAME, null);

        if (partnersCursor.moveToFirst()) {

            while (!partnersCursor.isAfterLast()) {
                arr.add(partnersCursor.getInt(partnersCursor.getColumnIndex(FLD_ID)));
                partnersCursor.moveToNext();
            }
        }

        return new HashSet<Integer>(arr) ;
    }



    public ArrayList<PartnerData> getEnabledPartnersByRegion(String region) {

        ArrayList<PartnerData> arr = new ArrayList<PartnerData>();

        String selection = FLD_REGION + " =? ";
        String[] selectionArgs = new String[]{region};

        Cursor partnersCursor = db.query(TABLE_PARTNERS, null, selection, selectionArgs, null, null, FLD_PARTNER_NAME, null);

        if (partnersCursor.moveToFirst()) {

            while (!partnersCursor.isAfterLast()) {

                PartnerData singlePartner = new PartnerData();

                // здесь необязательно выдирать все данные о партнере
                singlePartner.setId(partnersCursor.getInt(partnersCursor.getColumnIndex(FLD_ID)));
                singlePartner.setName(partnersCursor.getString(partnersCursor.getColumnIndex(FLD_PARTNER_NAME)));
                singlePartner.setLogoUrl(partnersCursor.getString(partnersCursor.getColumnIndex(FLD_LOGO_URL)));
                singlePartner.setShowOnMapFlag(partnersCursor.getInt(partnersCursor.getColumnIndex(FLD_FILTER_FLAG)) == 1);
                arr.add(singlePartner);

                partnersCursor.moveToNext();
            }
        }

        return arr;
    }





    public void saveUserCard(UserCardItem cardItem) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(FLD_CARD_CODE, cardItem.getCode());
        contentValues.put(FLD_CARD_PIC_TYPE, cardItem.getType());
        contentValues.put(FLD_CARD_STATUS, cardItem.getStatus());
        contentValues.put(FLD_IRS_ENABLED, cardItem.getIrsEnabled());
        contentValues.put(FLD_BEELINE_PHONE, cardItem.getBeelinePhoneNum());

        db.insert(TABLE_USER_CARDS, null, contentValues);
    }




    public ArrayList<UserCardItem> getAllCards() {

        ArrayList<UserCardItem> arr = new ArrayList<UserCardItem>();

        Cursor cardsCursor = db.query(TABLE_USER_CARDS, null, null, null, null, null, null, null);

        if (cardsCursor.moveToFirst()) {

            while (!cardsCursor.isAfterLast()) {

                UserCardItem singleItem = new UserCardItem();
                singleItem.setCode(cardsCursor.getString(cardsCursor.getColumnIndex(FLD_CARD_CODE)));
                singleItem.setType(cardsCursor.getString(cardsCursor.getColumnIndex(FLD_CARD_PIC_TYPE)));
                singleItem.setStatus(cardsCursor.getString(cardsCursor.getColumnIndex(FLD_CARD_STATUS)));
                singleItem.setIrsEnabled(cardsCursor.getInt(cardsCursor.getColumnIndex(FLD_IRS_ENABLED)) == 1);
                singleItem.setBeelinePhoneNum(cardsCursor.getString(cardsCursor.getColumnIndex(FLD_BEELINE_PHONE)));

                arr.add(singleItem);

                cardsCursor.moveToNext();
            }
        }

        return arr;
    }





    public ArrayList<UserCardItem> getActiveCards() {

        ArrayList<UserCardItem> arr = new ArrayList<UserCardItem>();

        String selection = FLD_CARD_STATUS + " =? ";
        String[] selectionArgs = new String[]{"Активна"};

        Cursor cardsCursor = db.query(TABLE_USER_CARDS, null, selection, selectionArgs, null, null, null, null);

        if (cardsCursor.moveToFirst()) {

            while (!cardsCursor.isAfterLast()) {

                UserCardItem singleItem = new UserCardItem();
                singleItem.setCode(cardsCursor.getString(cardsCursor.getColumnIndex(FLD_CARD_CODE)));
                singleItem.setType(cardsCursor.getString(cardsCursor.getColumnIndex(FLD_CARD_PIC_TYPE)));
                singleItem.setStatus(cardsCursor.getString(cardsCursor.getColumnIndex(FLD_CARD_STATUS)));
                singleItem.setIrsEnabled(cardsCursor.getInt(cardsCursor.getColumnIndex(FLD_IRS_ENABLED)) == 1);
                singleItem.setBeelinePhoneNum(cardsCursor.getString(cardsCursor.getColumnIndex(FLD_BEELINE_PHONE)));

                arr.add(singleItem);

                cardsCursor.moveToNext();
            }
        }

        return arr;
    }



    public void saveFilteredIds(ArrayList<PartnerData> partnersArr) {

        beginTransaction();
        for (PartnerData partner : partnersArr) {

            ContentValues newField = new ContentValues();
            newField.put(FLD_FILTER_FLAG, partner.getShowOnMapFlag());

            String selection = FLD_ID + " = ? ";
            String[] args = new String[]{String.valueOf(partner.getId())};
            db.update(TABLE_PARTNERS, newField, selection, args);
        }

        endTransaction(true);
    }


    public void saveMapPoint(Double longitude, Double latitude, int partnerId, String name, String iconUrl, String address, int movingMode, int distance) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(FLD_LONG, longitude);
        contentValues.put(FLD_LAT, latitude);
        contentValues.put(FLD_PARTNER_ID, partnerId);
        contentValues.put(FLD_NAME, name);
        contentValues.put(FLD_ICON, iconUrl);
        contentValues.put(FLD_ADDRESS, address);
        contentValues.put(FLD_MOVING_MODE, movingMode);
        contentValues.put(FLD_DISTANCE, distance);

        db.insert(TABLE_POINTS, null, contentValues);
    }



    public Cursor getMapPoints(int movingMode) {
        return db.query(TABLE_POINTS, null, FLD_MOVING_MODE + " =? ", new String[]{String.valueOf(movingMode)}, null, null, null);
    }



    public ArrayList<Integer> getParntersIndexesForMap(String region) {

        ArrayList<Integer> arr = new ArrayList<Integer>();

        String selection = FLD_REGION + " =? and " + FLD_FILTER_FLAG + "=?";
        String[] selectionArgs = new String[]{region, "1"};

        Cursor partnersCursor = db.query(TABLE_PARTNERS,//
                new String[]{FLD_ID, FLD_FILTER_FLAG}, selection, selectionArgs, null, null, null, null);

        if (partnersCursor.getCount() != 0) {
            partnersCursor.moveToFirst();
            while (!partnersCursor.isAfterLast()) {
                arr.add(partnersCursor.getInt(partnersCursor.getColumnIndex(FLD_ID)));
                partnersCursor.moveToNext();
            }
        }

        return arr;
    }





    /**
     * список партнеров для режима "на диване" (только онлайновые партнеры)
     * @param region
     * @param type
     * @return
     */
    public ArrayList<PartnerDataSimplified> getPartnersByType(String region, String type) {

        ArrayList<PartnerDataSimplified> arr = new ArrayList<PartnerDataSimplified>();

        String selection ;
        String[] selectionArgs;

        if(type.equals(TYPE_OFFLINE) || type.equals(TYPE_ONLINE)) {
            selection = FLD_REGION + " =? and " + FLD_FILTER_FLAG + "=? and "+FLD_PARTNER_TYPE +"=? ";
            selectionArgs = new String[]{region, "1", type};
        }else {
            //если режим TYPE_ALL, то выбираем из всех
            selection = FLD_REGION + " =? and " + FLD_FILTER_FLAG + "=? ";
            selectionArgs = new String[]{region, "1"};
        }

        Cursor partnersCursor = db.query(TABLE_PARTNERS,//
                new String[]{FLD_ID, FLD_PARTNER_NAME, FLD_LOGO_URL, FLD_POINTS_RULE,FLD_PARTNER_TYPE},
                selection, selectionArgs, null, null, FLD_PARTNER_NAME, null);


        if (partnersCursor.getCount() != 0) {
            partnersCursor.moveToFirst();
            while (!partnersCursor.isAfterLast()) {

                PartnerDataSimplified partnerDataSimplified = new PartnerDataSimplified();
                partnerDataSimplified.setId(partnersCursor.getInt(partnersCursor.getColumnIndex(FLD_ID)));
                partnerDataSimplified.setPartnerName(partnersCursor.getString(partnersCursor.getColumnIndex(FLD_PARTNER_NAME)));
                partnerDataSimplified.setLogoUrl(partnersCursor.getString(partnersCursor.getColumnIndex(FLD_LOGO_URL)));
                partnerDataSimplified.setPointsRule(partnersCursor.getString(partnersCursor.getColumnIndex(FLD_POINTS_RULE)));
                partnerDataSimplified.setType(partnersCursor.getString(partnersCursor.getColumnIndex(FLD_PARTNER_TYPE)));
                arr.add(partnerDataSimplified);

                partnersCursor.moveToNext();
            }
        }

        return arr;
    }



    public String getPartnerDescriptionById(String region, int partnerId) {
        String descr = "";

        String selection = FLD_REGION + " =? and " + FLD_ID + "=?";
        String[] selectionArgs = new String[]{region, String.valueOf(partnerId)};

        Cursor partnersCursor = db.query(TABLE_PARTNERS,//
               new String[]{FLD_ID, FLD_PARTNER_DESCR}, selection, selectionArgs, null, null, null, null);

        if (partnersCursor.getCount() != 0) {
            partnersCursor.moveToFirst();
            descr = partnersCursor.getString(partnersCursor.getColumnIndex(FLD_PARTNER_DESCR));
        }

        return descr;
    }


    // todo: освободиться от этого метода и объединить с предыдущим
    public String getPartnerNameById(String region, int partnerId) {
        String name = "";

        String selection = FLD_REGION + " =? and " + FLD_ID + "=?";
        String[] selectionArgs = new String[]{region, String.valueOf(partnerId)};

        Cursor partnersCursor = db.query(TABLE_PARTNERS,//
                new String[]{FLD_ID, FLD_PARTNER_NAME}, selection, selectionArgs, null, null, null, null);

        if (partnersCursor.getCount() != 0) {
            partnersCursor.moveToFirst();
            name = partnersCursor.getString(partnersCursor.getColumnIndex(FLD_PARTNER_NAME));
        }

        return name;
    }



    public void beginTransaction() {
//        Log.e("my info", "Открываем транзакцию");
        db.beginTransaction();
        transactionOpened = true;
    }




    public void endTransaction(boolean b) {

        if (transactionOpened) {

//            Log.e("@", "Закрываем транзакцию с результатом: " + b);

            if (b) {
                db.setTransactionSuccessful();
            }

            db.endTransaction();

            transactionOpened = false;
        }
    }





    public void saveFilteredCategories(ArrayList<CategoryData> arr) {

        beginTransaction();

        for (CategoryData category : arr) {

            ContentValues updateCategoryCV = new ContentValues();
            updateCategoryCV.put(FLD_FILTER_FLAG, category.selected ? 1 : 0);

            String selection = FLD_ID + " = ? ";
            String[] args = new String[]{String.valueOf(category.id)};
            db.update(TABLE_CATEGORIES, updateCategoryCV, selection, args);

        }

        endTransaction(true);

    }




    public int[] getSelectedCategories() {

        String selection = FLD_FILTER_FLAG + " =? " ;
        String[] selectionArgs = new String[]{"1"};

        Cursor selectedCategoriesCursor = db.query(TABLE_CATEGORIES,//
                new String[]{ FLD_ID },
                selection, selectionArgs, null, null, null, null);

        int len = selectedCategoriesCursor.getCount();
        int[] selectedCategoriesArr = new int[len];
        if (selectedCategoriesCursor.moveToFirst()) {
            for (int i = 0; i < len; i++) {
                selectedCategoriesArr[i] = selectedCategoriesCursor.getInt(0); // only 1 column
                selectedCategoriesCursor.moveToNext();
            }
        }

        return selectedCategoriesArr;
    }

    /**
     * медод возвращает категории и подкатегории в виде линейного списка
     * родительской категорией считается, если parentId==0;
     *
     * @return
     */
    public ArrayList<CategoryData> getSortedCategories() {

        ArrayList<CategoryData> sortedArr = new ArrayList<CategoryData>();

        String selection = FLD_CATEGORY_PARENT_ID + " =? " ;
        String[] selectionArgs = new String[]{"0"};

        Cursor categoriesCursor = db.query(TABLE_CATEGORIES,//
                                            new String[]{ FLD_ID, FLD_CATEGORY_PARENT_ID, FLD_FILTER_FLAG, FLD_NAME },
                                            selection, selectionArgs, null, null, null, null);

        if (categoriesCursor.moveToFirst()) {
            while (!categoriesCursor.isAfterLast()) {

                //берем из списка родительских, помещаем в возвращаемый массив, после идут либо дочерние, либо следующая родительская
                CategoryData category = new CategoryData();
                int categoryId = categoriesCursor.getInt(categoriesCursor.getColumnIndex(FLD_ID));
                category.id = categoryId;
                category.name = categoriesCursor.getString(categoriesCursor.getColumnIndex(FLD_NAME));
                category.parentId = 0;
                category.selected = categoriesCursor.getInt(categoriesCursor.getColumnIndex(FLD_FILTER_FLAG)) == 1;
                sortedArr.add(category);

                //после родительской помещаем все подкатегории
                selectionArgs = new String[]{String.valueOf(category.id)};
                Cursor subCategoriesCursor = db.query(TABLE_CATEGORIES,//
                        new String[]{FLD_ID, FLD_CATEGORY_PARENT_ID, FLD_FILTER_FLAG, FLD_NAME},
                        selection, selectionArgs, null, null, null, null);

                if (subCategoriesCursor.moveToFirst()) {
                    while (!subCategoriesCursor.isAfterLast()) {

                        CategoryData subCategory = new CategoryData();
                        subCategory.id = subCategoriesCursor.getInt(subCategoriesCursor.getColumnIndex(FLD_ID));
                        subCategory.name = subCategoriesCursor.getString(subCategoriesCursor.getColumnIndex(FLD_NAME));
                        subCategory.parentId = categoryId;
                        subCategory.selected = subCategoriesCursor.getInt(subCategoriesCursor.getColumnIndex(FLD_FILTER_FLAG)) == 1;
                        sortedArr.add(subCategory);

                        subCategoriesCursor.moveToNext();
                    }
                }

                categoriesCursor.moveToNext();
            }
        }

        return sortedArr;
    }





    public void saveCategory(int id, int parent_id, String name) {
        try {

            ContentValues insertCategoryCV = new ContentValues();
            insertCategoryCV.put(FLD_ID, id);
            insertCategoryCV.put(FLD_CATEGORY_PARENT_ID, parent_id);
            insertCategoryCV.put(FLD_NAME, name);
            insertCategoryCV.put(FLD_FILTER_FLAG, 1);
            db.insertOrThrow(TABLE_CATEGORIES, null, insertCategoryCV);

        } catch (SQLiteConstraintException exception) {

            ContentValues updateCategoryCV = new ContentValues();
            updateCategoryCV.put(FLD_CATEGORY_PARENT_ID, parent_id);
            updateCategoryCV.put(FLD_NAME, name);

            String selection = FLD_ID + " = ? ";
            String[] args = new String[]{String.valueOf(id)};
            db.update(TABLE_CATEGORIES, updateCategoryCV, selection, args);
        }

    }





    public void savePartner(PartnerData partnerObj) {

        try {

            ContentValues cv = new ContentValues();
            cv.put(FLD_ID, partnerObj.getId());
            cv.put(FLD_PARTNER_NAME, partnerObj.getName());
            cv.put(FLD_REGION, partnerObj.getRegion());
            cv.put(FLD_LOGO_URL, partnerObj.getLogoUrl());
            cv.put(FLD_FILTER_FLAG, partnerObj.getShowOnMapFlag() ? "1" : "0"); // todo:
            cv.put(FLD_PARTNER_TYPE, partnerObj.getType());
            cv.put(FLD_POINTS_RULE, partnerObj.getPointsRule());


            // cv.put(FLD_PARTNER_DESCR, partnerObj.getDescription());
            // cv.put(FLD_CODE, partnerObj.getCode() == null ? "" : partnerObj.getCode());
            // cv.put(FLD_IS_CARD_EMITTER, partnerObj.isCardEmitter() ? "1" : "0");
            // cv.put(FLD_HAS_POINTS, partnerObj.hasPoints() ? "1" : "0");
            // cv.put(FLD_HAS_TRANSACTIONS, partnerObj.hasTransactions() ? "1" : "0");
//            cv.put(FLD_ORDER, partnerObj.getOrder());

            db.insertOrThrow(TABLE_PARTNERS, null, cv);

        } catch (SQLiteConstraintException exception) {
            Log.i("@", " " + exception.toString());
        }
    }




    public Cursor getFilteredMapPoints(int movingMode) {

/*        Cursor points = getMapPoints(movingMode);
        Log.i("@", "количество для режима: " + movingMode + ",  точек всего: " + points.getCount());
        points.close();

        Cursor filteredpartners = db.query(TABLE_PARTNERS, null, FLD_FILTER_FLAG + "=? ", new String[]{String.valueOf(1)}, null, null, null);
        Log.i("@", "количество выбранных партнеров: " + filteredpartners.getCount());
        filteredpartners.close();*/


        String sql = "SELECT " + TABLE_POINTS + ".* FROM " + TABLE_POINTS +
              "  INNER JOIN " + TABLE_PARTNERS + " ON " + TABLE_POINTS + "." + FLD_PARTNER_ID + " = " + TABLE_PARTNERS + "." + FLD_ID + " AND " +
                TABLE_PARTNERS + "." + FLD_FILTER_FLAG + " = 1 AND " + TABLE_POINTS + "." + FLD_MOVING_MODE + " =? " + " ORDER BY " + FLD_DISTANCE;

//        Log.w("my info", "Запускаем запрос: " + sql);

        return db.rawQuery(sql, new String[]{String.valueOf(movingMode)});
    }




    private static class CDBHelper extends SQLiteOpenHelper {

        public CDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }



        @Override
        public void onCreate(SQLiteDatabase db) {

            String createPartnersTable = "CREATE TABLE " + TABLE_PARTNERS + "(" //
                    + FLD_ID + " INTEGER NOT NULL PRIMARY KEY," //
                    + FLD_PARTNER_NAME + " TEXT ," //
                    + FLD_CODE + " TEXT ," //
                    + FLD_PARTNER_DESCR + " TEXT ," //
                    + FLD_REGION + " TEXT ," //
                    + FLD_LOGO_URL + " TEXT ," //
                    + FLD_PARTNER_TYPE + " TEXT ," //
                    + FLD_POINTS_RULE + " TEXT ," //
                    + FLD_IS_CARD_EMITTER + " INTEGER ," //
                    + FLD_HAS_POINTS + " INTEGER ," //
                    + FLD_HAS_TRANSACTIONS + " INTEGER ," //
                    + FLD_FILTER_FLAG + " INTEGER ," //
                    + FLD_ORDER + " INTEGER );";


            String createPointsTable = "CREATE TABLE " + TABLE_POINTS + "(" //
                    + FLD_ID + " integer primary key autoincrement, " //
                    + FLD_LONG + " integer not null, " //
                    + FLD_LAT + " integer not null, " //
                    + FLD_PARTNER_ID + " integer not null, " //
                    + FLD_MOVING_MODE + " integer not null, " + FLD_NAME + " text, " //
                    + FLD_ICON + " text, " //
                    + FLD_DISTANCE + " integer not null, " //
                    + FLD_ADDRESS + " text );";

            String createUserCardsTable = "CREATE TABLE " + TABLE_USER_CARDS//
                    + "(" + FLD_ID + " integer primary key autoincrement, "//
                    + FLD_CARD_CODE + " text, "                        //
                    + FLD_CARD_PIC_TYPE + " integer not null, "//
                    + FLD_CARD_STATUS + " integer not null, "//
                    + FLD_IRS_ENABLED + " integer not null, "  //
                    + FLD_BEELINE_PHONE + " text );";            //


            String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + "(" //
                    + FLD_ID + " integer primary key , " //
                    + FLD_CATEGORY_PARENT_ID + " integer not null, " //
                    + FLD_FILTER_FLAG + " INTEGER ," //
                    + FLD_NAME + " text );";

            db.execSQL(createPartnersTable);
            db.execSQL(createPointsTable);
            db.execSQL(createUserCardsTable);
            db.execSQL(createCategoriesTable);
        }


        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {

        }

    }

}
