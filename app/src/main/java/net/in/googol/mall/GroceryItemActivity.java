package net.in.googol.mall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class GroceryItemActivity extends AppCompatActivity implements AddReviewDialog.AddReview {

    public static final String GROCERY_ITEMS_KEY = "incoming_item";
    private static final String TAG = "GroceryItemActivity";


    private RecyclerView reviewRecView;
    private ImageView ivImage, firstFilledStar, secondFilledStar, thirdFilledStar, firstEmptyStar, secondEmptyStar, thirdEmptyStar;
    private TextView tvDescription, tvAddReview, tvProdName, tvPrice;
    private Button btnAddToCart;
    private RelativeLayout firstStarRelLayout, secondStarRelLayout, thirdStarRelLayout;
    private MaterialToolbar toolbar;

    private ReviewAdapter reviewAdapter;

    private GroceryItem incomingItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_item);

        initView();

        setSupportActionBar(toolbar);
        reviewAdapter = new ReviewAdapter();
        Intent intent = getIntent();
        if (null != intent){
            incomingItem = intent.getParcelableExtra(GROCERY_ITEMS_KEY);
            if (null != incomingItem){
                Log.d(TAG, "onCreate: Review of this item::::::::::::::::: " + incomingItem.getReviews());
                tvProdName.setText(incomingItem.getName());
                tvDescription.setText(incomingItem.getDescription());
                tvPrice.setText(String.valueOf(incomingItem.getPrice()) + " $");
                Glide.with(this)
                        .asBitmap().load(incomingItem.getImageURL()).into(ivImage);
                ArrayList<Review> reviews = Utils.getReviewsById(this, incomingItem.getId());
                reviewRecView.setAdapter(reviewAdapter);
                reviewRecView.setLayoutManager(new LinearLayoutManager(this));
                if (null != reviews){
                    if (reviews.size() > 0){
                        reviewAdapter.setReviews(reviews);
                    }
                }
                btnAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.addItemToCart(GroceryItemActivity.this , incomingItem);
                        Log.d(TAG, "onClick: cart Items :: " + Utils.getCartItems(GroceryItemActivity.this));
                        Intent intent = new Intent(GroceryItemActivity.this, CartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                tvAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 1/1/2021 add function to add review feature and show a dialog
                        AddReviewDialog dialog = new AddReviewDialog();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(GROCERY_ITEMS_KEY, incomingItem);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), "add review");
                    }
                });

                handleRating();
            }
        }

    }

    private void handleRating() {

        switch (incomingItem.getRate()){
            case 0:
                firstEmptyStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                firstFilledStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 1:
                firstEmptyStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 2:
                firstEmptyStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 3:
                firstEmptyStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        firstStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 1){
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 1);
                    incomingItem.setRate(1);
                    handleRating();
                }
            }
        });

        secondStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 2){
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 2);
                    incomingItem.setRate(2);
                    handleRating();
                }
            }
        });

        thirdStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 3){
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 3);
                    incomingItem.setRate(3);
                    handleRating();
                }
            }
        });
    }

    private void initView() {
        reviewRecView = findViewById(R.id.reviewRecView);
        ivImage = findViewById(R.id.ivImage);
        firstEmptyStar = findViewById(R.id.firstEmptyStar);
        firstFilledStar = findViewById(R.id.firstFilledStar);
        secondEmptyStar = findViewById(R.id.secondEmptyStar);
        secondFilledStar = findViewById(R.id.secondFilledStar);
        thirdEmptyStar = findViewById(R.id.thirdEmptyStar);
        thirdFilledStar = findViewById(R.id.thirdFilledStar);
        tvPrice = findViewById(R.id.tvPrice);
        tvProdName = findViewById(R.id.tvProdName);
        tvAddReview = findViewById(R.id.tvAddReview);
        tvDescription = findViewById(R.id.tvDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        firstStarRelLayout = findViewById(R.id.firstStarRelLayout);
        secondStarRelLayout = findViewById(R.id.secondStarRelLayout);
        thirdStarRelLayout = findViewById(R.id.thirdStarRelLayout);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void onAddReviewResult(Review review) {
        Utils.addReview(this, review);
        ArrayList<Review> reviews = Utils.getReviewsById(this,review.getGroceryItemId());

        if (reviews != null){
            Log.d(TAG, "onAddReviewResult: new Review " + review);
            reviewAdapter.setReviews(reviews);
        }
    }
}