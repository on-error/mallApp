package net.in.googol.mall;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.tv.TvContentRating;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static net.in.googol.mall.GroceryItemActivity.GROCERY_ITEMS_KEY;

public class AddReviewDialog extends DialogFragment {

    private TextView tvItemName, tvWarning;
    private EditText edUsername, edReview;
    private Button btnAddReview;

    public interface AddReview{
        void onAddReviewResult(Review review);
    }

    private AddReview addReview;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_review, null);
        initViews(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(view);
        Bundle bundle = getArguments();
        if (bundle != null){
            final GroceryItem item = bundle.getParcelable(GROCERY_ITEMS_KEY);
            if (item != null){
                tvItemName.setText(item.getName());
                btnAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = edUsername.getText().toString();
                        String review = edReview.getText().toString();
                        String date = getCurrentDate();
                        if (username.equals("") || review.equals("")){
                            tvWarning.setText("Fill all the blanks");
                            tvWarning.setVisibility(View.VISIBLE);
                        }
                        else {
                            tvWarning.setVisibility(View.GONE);
                            try {
                                addReview = (AddReview) getActivity();
                                addReview.onAddReviewResult(new Review(item.getId(), username, review, date));
                                dismiss();
                            }catch (ClassCastException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }

        return builder.create();
    }

    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY");
        return sdf.format(calendar.getTime());
    }

    private void initViews(View view) {

        tvItemName = view.findViewById(R.id.tvItemName);
        tvWarning = view.findViewById(R.id.tvWarning);
        edUsername = view.findViewById(R.id.edUsername);
        edReview = view.findViewById(R.id.edReview);
        btnAddReview = view.findViewById(R.id.btnAddReview);

    }
}
