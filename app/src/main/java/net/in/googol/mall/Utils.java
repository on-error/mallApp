package net.in.googol.mall;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.mbms.MbmsErrors;

import androidx.constraintlayout.solver.GoalRow;

import com.google.gson.Gson;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.crypto.AEADBadTagException;

public class Utils {

    private static final String CART_ITEMS_KEY = "cart_items";
    private static int ID = 0;
    private static int ORDER_ID = 0;

    private static final String ALL_TIMES_KEY = "all_times";
    private static final String DB_NAME = "fake_database";
    private static Gson gson = new Gson();
    private static Type groceryListType = new TypeToken<ArrayList<GroceryItem>>(){}.getType();

    public static void intiSharedPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        ArrayList<GroceryItem> allitems = gson.fromJson(sharedPreferences.getString(ALL_TIMES_KEY, null), groceryListType);
        if (null == allitems)
        {
            initAllItems(context);
        }
    }

    private static void initAllItems(Context context) {

        ArrayList<GroceryItem> allitems = new ArrayList<>();
        allitems.add(new GroceryItem( "Milk", "Milk is rich in nutrient", "https://m.media-amazon.com/images/I/71CJIcdJ1gL._SS500_.jpg",
                "Drink", 2.2, 8));

        allitems.add(new GroceryItem("Soda", "Sweet Soft Drink", "https://cdn.vox-cdn.com/thumbor/XhE3IYApaaom476JCACvHWkfhPE=/0x0:1000x833/1200x800/filters:focal(420x337:580x497)/cdn.vox-cdn.com/uploads/chorus_image/image/62220779/shutterstock_225147871.0.0.jpg",
                "Drink", 1.6, 10));

        allitems.add(new GroceryItem("Ice Cream", "Ice cream (derived from earlier iced cream or cream ice)[1] is a sweetened frozen food typically eaten as a snack or dessert. It may be made from dairy milk or cream and is flavoured with a sweetener, either sugar or an alternative, and any spice, such as cocoa or vanilla.",
                "https://www.crazyinspiredlife.com/wp-content/uploads/2019/08/Strawberry-Rose-Cones-Down-v2.jpg", "Food", 5.76, 10));

        allitems.add(new GroceryItem("Shampoo", "Shampoo is good for washing Hairs", "https://mk0plaineproduc2a6do.kinstacdn.com/wp-content/uploads/shampoo-citrus-lavender-1.jpg",
                "Cleanser", 22, 4));
        allitems.add(new GroceryItem("Spaghetti", "Spaghetti is long , thin, solid, cyclindrical, pasta", "https://feelgoodfoodie.net/wp-content/uploads/2017/03/Spaghetti-and-Meatballs-7.jpg",
                "Food", 45, 100));

        allitems.add(new GroceryItem("Soap", "It is used to clean yourself ", "https://images-na.ssl-images-amazon.com/images/I/71R5xBOtLRL._SY355_.jpg",
                "Cleanser", 7.2, 222));

        allitems.add(new GroceryItem("Pistachio", "Nuts ", "https://post.greatist.com/wp-content/uploads/sites/3/2020/02/322899_2200-732x549.jpg", "Nuts", 18, 17));

        allitems.add(new GroceryItem("Walnut", "Nuts", "https://images-prod.healthline.com/hlcmsresource/images/AN_images/benefits-of-walnuts-1296x728-feature.jpg",
                "Nuts",18, 10));


        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALL_TIMES_KEY, gson.toJson(allitems));

        editor.commit();
    }

    public static ArrayList<GroceryItem> getAllItems(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        ArrayList<GroceryItem> allItems = gson.fromJson(sharedPreferences.getString(ALL_TIMES_KEY, null), groceryListType);
        return allItems;
    }

    public static int getID() {
        ID++;
        return ID;
    }

    public static void changeRate(Context context, int itemId, int newRate){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<GroceryItem> allItems = gson.fromJson(sharedPreferences.getString(ALL_TIMES_KEY, null), groceryListType);
        if (allItems != null){
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for (GroceryItem i: allItems){
                if (i.getId() == itemId){
                    i.setRate(newRate);
                    newItems.add(i);
                }
                else {
                    newItems.add(i);
                }
            }
            editor.remove(ALL_TIMES_KEY);
            editor.putString(ALL_TIMES_KEY, gson.toJson(newItems));
            editor.commit();
        }
    }

    public static void addReview(Context context, Review review){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (allItems != null){
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for (GroceryItem i : allItems){
                if (i.getId() == review.getGroceryItemId()){
                    ArrayList<Review> reviews = i.getReviews();
                    if (reviews == null){
                        reviews = new ArrayList<>();
                    }
                    try {
                        reviews.add(review);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    i.setReviews(reviews);
                }
                newItems.add(i);
            }
            editor.remove(ALL_TIMES_KEY);
            editor.putString(ALL_TIMES_KEY, gson.toJson(newItems));
            editor.commit();
        }
    }

    public static ArrayList<Review> getReviewsById(Context context, int itemId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (allItems != null){
            for (GroceryItem i: allItems){
                if (i.getId() == itemId){
                    ArrayList<Review> reviews = i.getReviews();
                    return reviews;
                }
            }
        }
        return null;
    }

    public static void addItemToCart(Context context, GroceryItem item){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        ArrayList<GroceryItem> cartItems = gson.fromJson(sharedPreferences.getString(CART_ITEMS_KEY, null), groceryListType);
        if (cartItems == null){
            cartItems = new ArrayList<>();
        }

        cartItems.add(item);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART_ITEMS_KEY);
        editor.putString(CART_ITEMS_KEY,gson.toJson(cartItems));
        editor.commit();
    }

    public static ArrayList<GroceryItem> getCartItems(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        ArrayList<GroceryItem> cartItems = gson.fromJson(sharedPreferences.getString(CART_ITEMS_KEY, null), groceryListType);
        return cartItems;
    }

    public static ArrayList<GroceryItem> searchForItems(Context context, String text){
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems){
            ArrayList<GroceryItem> items = new ArrayList<>();
            for (GroceryItem item : allItems){
                if (item.getName().equalsIgnoreCase(text)){
                    items.add(item);
                }

                String[] names = item.getName().split(" ");
                for (int i=0; i<names.length; i++){
                    if (text.equalsIgnoreCase(names[i])){
                        boolean doesExist = false;
                        for (GroceryItem j : items){
                            if (j.getId() == item.getId()){
                                doesExist = true;
                            }
                        }

                        if (!doesExist){
                            items.add(item);
                        }
                    }
                }
            }
            return items;
        }
        return null;
    }

    public static ArrayList<String> getCategories(Context context){
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems){
            ArrayList<String> categories = new ArrayList<>();
            for (GroceryItem item: allItems){
                boolean doesExist = false;
                for (String s : categories){
                    if (item.getCategory().equals(s)){
                        doesExist = true;
                    }
                }
                if (!doesExist){
                    categories.add(item.getCategory());
                }
            }
            return categories;
        }
        return null;
    }

    public static ArrayList<GroceryItem> getItemsByCategory(Context context, String category){
        ArrayList<GroceryItem> allItems = Utils.getAllItems(context);
        if (null != allItems){
            ArrayList<GroceryItem> items = new ArrayList<>();
            for(GroceryItem item: allItems){
                if (item.getCategory().equals(category)){
                    items.add(item);
                }
            }
            return items;
        }
        return null;
    }

    public static void deleteItemFromCart(Context context, GroceryItem item){
        ArrayList<GroceryItem> cartItems = getCartItems(context);
        if (null != cartItems){
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for (GroceryItem i: cartItems){
                if (i.getId() != item.getId()){
                    newItems.add(i);
                }
            }

            SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(CART_ITEMS_KEY);
            editor.putString(CART_ITEMS_KEY, gson.toJson(newItems));
            editor.commit();
        }
    }

    public static int getOrderId() {
        ORDER_ID++;
        return ORDER_ID;
    }

    public static void clearCartItems(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART_ITEMS_KEY);
        editor.commit();
    }

    public static void increasePopularityPoint(Context context, GroceryItem item, int points){
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems){
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for (GroceryItem i: allItems){
                if (i.getId() == item.getId()){
                    i.setPopularityPoint(i.getPopularityPoint() + 1);
                    newItems.add(i);
                }
                newItems.add(i);
            }

            SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  sharedPreferences.edit();
            editor.remove(ALL_TIMES_KEY);
            Gson gson = new Gson();
            editor.putString(ALL_TIMES_KEY, gson.toJson(newItems));
            editor.commit();
        }
    }
}
