package net.in.googol.mall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class FirstCartFragment extends Fragment implements CartAdapter.DeleteItem, CartAdapter.TotalPrice {

    private RelativeLayout itemRelLayout;
    private TextView tvTotalSum, tvNoItems;
    private Button btnNext;
    private RecyclerView cartRecView;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_first, container, false);

        initViews(view);
        adapter = new CartAdapter(getActivity(), this);
        cartRecView.setAdapter(adapter);
        cartRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
        if (cartItems != null){
            if (cartItems.size() > 0){
                tvNoItems.setVisibility(View.GONE);
                itemRelLayout.setVisibility(View.VISIBLE);
                adapter.setItems(cartItems);
            }else {
                tvNoItems.setVisibility(View.VISIBLE);
                itemRelLayout.setVisibility(View.GONE);
            }
        }else{
            tvNoItems.setVisibility(View.VISIBLE);
            itemRelLayout.setVisibility(View.GONE);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new SecondCartFragment());
                transaction.commit();
            }
        });

        return view;
    }
    private void initViews(View view){
        itemRelLayout = view.findViewById(R.id.itemRelLayout);
        tvTotalSum = view.findViewById(R.id.tvTotalSum);
        btnNext = view.findViewById(R.id.btnNext);
        cartRecView = view.findViewById(R.id.cartRecView);
        tvNoItems = view.findViewById(R.id.tvNoItems);
    }

    @Override
    public void onDeleteResult(GroceryItem item) {
        Utils.deleteItemFromCart(getActivity(), item);
        ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
        if (cartItems != null){
            if (cartItems.size() > 0){
                tvNoItems.setVisibility(View.GONE);
                itemRelLayout.setVisibility(View.VISIBLE);
                adapter.setItems(cartItems);
            }else {
                tvNoItems.setVisibility(View.VISIBLE);
                itemRelLayout.setVisibility(View.GONE);
            }
        }else{
            tvNoItems.setVisibility(View.VISIBLE);
            itemRelLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getTotalPrice(double price) {
        tvTotalSum.setText(String.valueOf(price) + "$");
    }
}
