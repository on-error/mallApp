package net.in.googol.mall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.nio.channels.AlreadyBoundException;
import java.util.ArrayList;

import static net.in.googol.mall.AllCategoriesDialog.ALL_CATEGORIES;
import static net.in.googol.mall.AllCategoriesDialog.CALLING_ACTIVITY;

public class SearchActivity extends AppCompatActivity implements AllCategoriesDialog.GetCategory {

    private MaterialToolbar toolbar;
    private ImageView btnSearch;
    private EditText edSearch;
    private TextView tvFirstCategory, tvSecondCategory, tvThirdCategory, tvAllCategories;
    private BottomNavigationView bottomNavView;
    private RecyclerView recView;

    private GroceryItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initBottomNavView();
        setSupportActionBar(toolbar);

        adapter = new GroceryItemAdapter(this);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();
        if (intent != null){
            String category = intent.getStringExtra("category");
            if (null != category){
                ArrayList<GroceryItem> items = Utils.getItemsByCategory(this, category);
                if (items != null){
                    adapter.setItems(items);
                }
            }
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearch();
            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                initSearch();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ArrayList<String> categories = Utils.getCategories(this);
        if (categories != null){
            if (categories.size() > 0){
                if (categories.size() == 1){
                    showCategories(categories, 1);
                }
                else if (categories.size() == 2){
                    showCategories(categories, 2);
                }
                else {
                    showCategories(categories, 3);
                }
            }
        }

        tvAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCategoriesDialog dialog = new AllCategoriesDialog();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(ALL_CATEGORIES, Utils.getCategories(SearchActivity.this));
                bundle.putString(CALLING_ACTIVITY, "search_activity");
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "all categories");
            }
        });

    }

    private void showCategories(ArrayList<String> categories, int i) {
        switch (i){
            case 1:
                tvFirstCategory.setVisibility(View.VISIBLE);
                tvFirstCategory.setText(categories.get(0));
                tvSecondCategory.setVisibility(View.GONE);
                tvThirdCategory.setVisibility(View.GONE);
                tvFirstCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(0));
                        if (null != items){
                            adapter.setItems(items);
                        }
                    }
                });
                break;
            case 2:
                tvFirstCategory.setVisibility(View.VISIBLE);
                tvFirstCategory.setText(categories.get(0));
                tvSecondCategory.setVisibility(View.VISIBLE);
                tvSecondCategory.setText(categories.get(1));
                tvThirdCategory.setVisibility(View.GONE);
                tvFirstCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(0));
                        if (null != items){
                            adapter.setItems(items);
                        }
                    }
                });
                tvSecondCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(1));
                        if (null != items){
                            adapter.setItems(items);
                        }
                    }
                });
                break;
            case 3:
                tvFirstCategory.setVisibility(View.VISIBLE);
                tvFirstCategory.setText(categories.get(0));
                tvSecondCategory.setVisibility(View.VISIBLE);
                tvSecondCategory.setText(categories.get(1));
                tvThirdCategory.setVisibility(View.VISIBLE);
                tvThirdCategory.setText(categories.get(2));
                tvFirstCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(0));
                        if (null != items){
                            adapter.setItems(items);
                        }
                    }
                });
                tvSecondCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(1));
                        if (null != items){
                            adapter.setItems(items);
                        }
                    }
                });
                tvThirdCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(2));
                        if (null != items){
                            adapter.setItems(items);
                        }
                    }
                });
                break;
            default:
                tvThirdCategory.setVisibility(View.GONE);
                tvSecondCategory.setVisibility(View.GONE);
                tvFirstCategory.setVisibility(View.GONE);
                break;
        }
    }

    private void initSearch() {
        if (!edSearch.getText().toString().equals("")){
            // TODO: 1/2/2021 getitems
            String name = edSearch.getText().toString();
            ArrayList<GroceryItem> items = Utils.searchForItems(this, name);
            if (items != null){
                adapter.setItems(items);
            }

        }
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        btnSearch = findViewById(R.id.btnSearch);
        edSearch = findViewById(R.id.edSearch);
        tvFirstCategory = findViewById(R.id.tvFirstCategory);
        tvSecondCategory = findViewById(R.id.tvSecondCategory);
        tvThirdCategory = findViewById(R.id.tvThirdCategory);
        tvAllCategories = findViewById(R.id.tvAllCategories);
        bottomNavView = findViewById(R.id.bottomNavView);
        recView = findViewById(R.id.recView);

    }

    private void initBottomNavView() {

        bottomNavView.setSelectedItemId(R.id.search);
        bottomNavView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case R.id.cart:
                        Intent cartintent = new Intent(SearchActivity.this, CartActivity.class);
                        cartintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartintent);
                        break;
                    case R.id.search:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onGetCatergoryResult(String category) {
        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this ,category);
        if (null != items){
            adapter.setItems(items);
        }
    }
}