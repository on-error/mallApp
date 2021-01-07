package net.in.googol.mall;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.in.googol.mall.SecondCartFragment.ORDER_KEY;

public class ThirdCartFragment extends Fragment {
    private static final String TAG = "ThirdCartFragment";

    private Button btnCheckout, btnBack;
    private TextView tvPrice, tvAddress, tvNumber, tvItemsNames;
    private RadioGroup radioGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart_third, container, false);
        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null){
            String jsonOrder = bundle.getString(ORDER_KEY);
            if (jsonOrder != null){
                Gson gson = new Gson();
                Type type = new TypeToken<Order>(){}.getType();
                Order order = gson.fromJson(jsonOrder, type);
                if (null != order){
                    String items = "";
                    for (GroceryItem i: order.getItems()){
                        items += "\n\t" + i.getName();
                    }
                    tvItemsNames.setText(items);
                    tvAddress.setText(order.getAddress());
                    tvNumber.setText(order.getPhoneNumber());
                    tvPrice.setText(String.valueOf(order.getTotalPrice()));

                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle backBundle = new Bundle();
                            backBundle.putString(ORDER_KEY, jsonOrder);
                            SecondCartFragment secondCartFragment = new SecondCartFragment();
                            secondCartFragment.setArguments(backBundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, secondCartFragment);
                            transaction.commit();

                        }
                    });

                    btnCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (radioGroup.getCheckedRadioButtonId()){
                                case R.id.rbPayPal:
                                    order.setPaymentMethod("PayPal");
                                    break;
                                case R.id.rbCreditCard:
                                    order.setPaymentMethod("Credit Card");
                                    break;
                                default:
                                    order.setPaymentMethod("Unknowm");
                                    break;
                            }

                            order.setSuccess(true);
                            // TODO: 1/4/2021 Send the details to server using retrofit

                            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                                    .setLevel(HttpLoggingInterceptor.Level.BODY);

                            OkHttpClient client = new OkHttpClient.Builder()
                                    .addInterceptor(interceptor)
                                    .build();

                            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .client(client)
                                    .build();

                            OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
                            Call<Order> call = endPoint.newOrder(order);
                            call.enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Call<Order> call, Response<Order> response) {
                                    Log.d(TAG, "onResponse: Code : " + response.code());
                                    if (response.isSuccessful()){
                                        // TODO: 1/4/2021 navigate to payment result
                                        Bundle resultBundle = new Bundle();
                                        resultBundle.putString(ORDER_KEY, gson.toJson(response.body()));
                                        PaymentResultFragment paymentResultFragment = new PaymentResultFragment();
                                        paymentResultFragment.setArguments(resultBundle);
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.container, paymentResultFragment);
                                        transaction.commit();
                                    }
                                    else {
                                    }
                                }

                                @Override
                                public void onFailure(Call<Order> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvNumber = view.findViewById(R.id.tvNumber);
        tvItemsNames = view.findViewById(R.id.tvItemsNames);
        radioGroup = view.findViewById(R.id.rgPaymentMethod);
    }
}
