package net.in.googol.mall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static net.in.googol.mall.SecondCartFragment.ORDER_KEY;

public class PaymentResultFragment extends Fragment {

    private Button btnHome;
    private TextView tvPayment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_result, container, false);

        initView(view);

        Bundle bundle = getArguments();
        if (bundle != null){
            String jsonOrder = bundle.getString(ORDER_KEY);
            if (jsonOrder != null){
                Gson gson = new Gson();
                Type type = new TypeToken<Order>(){}.getType();
                Order order = gson.fromJson(jsonOrder, type);
                if (null != order){
                    if (order.isSuccess()){
                        tvPayment.setText("Payment was successful \n\t Your order will arrive in 3 days");
                        // TODO: 1/4/2021 Increase Popularity Points
                        // TODO: 1/4/2021 Increase user Point
                        Utils.clearCartItems(getActivity());
                        for (GroceryItem i: order.getItems()){
                            Utils.increasePopularityPoint(getActivity(), i, 1);
                        }
                    }else {
                        tvPayment.setText("Payment Failed");
                    }
                }

                btnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView(View view) {

        btnHome = view.findViewById(R.id.btnHome);
        tvPayment = view.findViewById(R.id.tvPayment);

    }
}
