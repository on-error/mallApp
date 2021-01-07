package net.in.googol.mall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SecondCartFragment extends Fragment {

    private EditText edAddress, edPhone, edEmail, edZipcode;
    private Button btnBack, btnNext;
    private TextView tvWarning;
    public static final String ORDER_KEY = "order";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart_second, container, false);

        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null){
            String jsonOrder = bundle.getString(ORDER_KEY);
            if (jsonOrder != null){
                Gson gson = new Gson();
                Type type = new TypeToken<Order>(){}.getType();
                Order order = gson.fromJson(jsonOrder, type);
                if (null != order){
                    edAddress.setText(order.getAddress());
                    edEmail.setText(order.getEmail());
                    edPhone.setText(order.getPhoneNumber());
                    edZipcode.setText(order.getZipcode());
                }
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new FirstCartFragment());
                transaction.commit();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()){
                    tvWarning.setVisibility(View.GONE);

                    ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
                    if (cartItems != null){
                        Order order = new Order();
                        order.setItems(cartItems);
                        order.setAddress(edAddress.getText().toString());
                        order.setEmail(edEmail.getText().toString());
                        order.setPhoneNumber(edPhone.getText().toString());
                        order.setZipcode(edZipcode.getText().toString());
                        order.setTotalPrice(calculateTotalPrice(cartItems));

                        Gson gson = new Gson();
                        String jsonOrder = gson.toJson(order);
                        Bundle bundle = new Bundle();
                        bundle.putString(ORDER_KEY, jsonOrder);

                        ThirdCartFragment thirdCartFragment = new ThirdCartFragment();
                        thirdCartFragment.setArguments(bundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, thirdCartFragment);
                        transaction.commit();

                    }
                }
                else {
                    tvWarning.setText("Fill in all the blanks");
                    tvWarning.setVisibility(View.VISIBLE);
                }
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private double calculateTotalPrice(ArrayList<GroceryItem> items){
        double price = 0;
        for (GroceryItem i: items){
            price += i.getPrice();
        }
        price = Math.round(price*100.0)/100.0;

        return price;

    }

    private boolean validateData() {
        if (edZipcode.getText().toString().equals("")|| edPhone.getText().toString().equals("") || edEmail.getText().toString().equals("") || edAddress.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void initViews(View view) {
        edAddress = view.findViewById(R.id.edAddress);
        edPhone = view.findViewById(R.id.edPhone);
        edEmail = view.findViewById(R.id.edEmail);
        edZipcode = view.findViewById(R.id.edZipcode);
        btnBack = view.findViewById(R.id.btnBack);
        btnNext = view.findViewById(R.id.btnNext);
        tvWarning = view.findViewById(R.id.tvWarning);
    }
}
