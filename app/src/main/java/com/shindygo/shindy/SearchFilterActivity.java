package com.shindygo.shindy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.shindygo.shindy.model.Filter;
import com.shindygo.shindy.utils.FontUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFilterActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.distance)
    Spinner distance;
    @BindView(R.id.sp_religion)
    Spinner spReligion;
    @BindView(R.id.gender)
    Spinner gender;
    @BindView(R.id.sp_gender_pref)
    Spinner spGenderPref;
    @BindView(R.id.age_from)
    EditText ageFrom;
    @BindView(R.id.age_to)
    EditText ageTo;


    public final static String FILTER="filter";
    Filter filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        ButterKnife.bind(this);
        FontUtils.setFont(title, FontUtils.Be_Bright);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distance.setSelection(0);
                spReligion.setSelection(0);
                gender.setSelection(0);
                spGenderPref.setSelection(0);
                ageFrom.setText("");
                ageTo.setText("");

            }
        });
        filter=  getIntent().getParcelableExtra(FILTER);
        if (filter!=null){
            distance.setSelection(filter.getDistancePos());
            spReligion.setSelection(filter.getReligionPos());
            gender.setSelection(filter.getGenderPos());
            spGenderPref.setSelection(filter.getGenderPref());
            if (filter.getAgeFrom()<1)
                ageFrom.setText("");
            else
                ageFrom.setText(String.valueOf(filter.getAgeFrom()));
            if (filter.getAgeTo()<1)
                ageTo.setText("");
            else
                ageTo.setText(""+filter.getAgeTo());
        }
    }

    private void finishWithResult() {
       /* Bundle conData = new Bundle();

        if (!ageTo.getText().toString().isEmpty()&&!ageTo.getText().toString().isEmpty()) {
            conData.putInt("ageto", Integer.parseInt(ageTo.getText().toString()));
            conData.putInt("agefrom", Integer.parseInt(ageFrom.getText().toString()));
        }
        conData.putInt("distance", distance.getSelectedItemPosition());
        conData.putInt("gender", gender.getSelectedItemPosition() + 1);
        conData.putInt("gender_pref", spGenderPref.getSelectedItemPosition());
        conData.putInt("religion", spReligion.getSelectedItemPosition());
*/
        Filter filter = new Filter( ageTo.getText(),  ageFrom.getText(),distance.getSelectedItemPosition(),spReligion.getSelectedItemPosition(),gender.getSelectedItemPosition(),spGenderPref.getSelectedItemPosition());
        Intent intent = new Intent();
        intent.putExtra(FILTER,filter);
      //  intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }

}
