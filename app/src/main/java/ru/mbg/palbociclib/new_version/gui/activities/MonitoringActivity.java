package ru.mbg.palbociclib.new_version.gui.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.db.models.Dose;
import ru.mbg.palbociclib.utils.GuiUtils;


public class MonitoringActivity extends AppCompatActivity {

    public static final String CYCLE = "cycle";
    public static final String COUNT = "count";

    @BindView(R.id.toxic_info_text)
    protected TextView mToxicInfoText;
    @BindView(R.id.cycle_spinner)
    protected Spinner mCycleSpinner;
    @BindView(R.id.days_spinner)
    protected Spinner mDaysSpinner;
    @BindView(R.id.button_dose_125)
    protected ImageButton m125Button;
    @BindView(R.id.button_dose_100)
    protected ImageButton m100Button;
    @BindView(R.id.button_dose_75)
    protected ImageButton m75Button;
    @BindView(R.id.toxic_button_1)
    protected Button mToxic1Button;
    @BindView(R.id.toxic_button_2)
    protected Button mToxic2Button;
    @BindView(R.id.toxic_button_3)
    protected Button mToxic3Button;
    @BindView(R.id.toxic_button_4)
    protected Button mToxic4Button;
    @BindView(R.id.calculate_button)
    protected Button mCalculateButton;
    @BindView(R.id.container)
    protected LinearLayout mContainer;
    @BindView(R.id.info)
    protected LinearLayout mInfo;

    private int mCycle = -1;

    private Dose mDose = null;

    private int mToxic = -1;

    private int mDay = -1;

    private String[] cycles = new String[]{"1-й", "2-й", "3-й и более"};

    private String[] daysFull = new String[]{"1-й", "14-й", "21-й"};

    private String[] daysShort = new String[]{"1-й"};

    private ArrayAdapter<String> mCycleAdapter;

    private  ArrayAdapter<String> mDaysAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        ButterKnife.bind(this);

        setTitle(R.string.new_version_monitoring_activity_title);
        setNavigationButton();

        if (hasExtra()){
            mCycle = getIntent().getIntExtra(CYCLE, -1);
            mDay = getIntent().getIntExtra(COUNT, -1);
            setupByCycleAndDay();
        } else {
            setupClearView();
        }
        mCalculateButton.setEnabled(false);
        mToxicInfoText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(motionEvent.getRawX() >= (mToxicInfoText.getRight() - mToxicInfoText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        GuiUtils.displayOkDialog(MonitoringActivity.this, "Определение степени гематоксичности (грейда)", getString(R.string.new_version_rule), null, false);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (hasExtra()){
            switch (mCycle){
                case 1: mCycleSpinner.setSelection(0); break;
                case 2: mCycleSpinner.setSelection(1); break;
                case 3: mCycleSpinner.setSelection(2); break;
                default: mCycleSpinner.setSelection(2); break;
            }
            switch (mDay){
                case 1: mDaysSpinner.setSelection(0); break;
                case 14: mDaysSpinner.setSelection(1); break;
                case 21: mDaysSpinner.setSelection(2); break;
            }
        }
        addSpinnerListeners();
    }

    private void setupClearView(){

        mCycleAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, cycles);
        mCycleAdapter.setDropDownViewResource(R.layout.spinner_item);
        mCycleSpinner.setAdapter(mCycleAdapter);

        mDaysAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, daysFull);
        mDaysAdapter.setDropDownViewResource(R.layout.spinner_item);
        mDaysSpinner.setAdapter(mDaysAdapter);
    }

    private boolean hasExtra(){
        return getIntent().hasExtra(CYCLE) && getIntent().hasExtra(COUNT);
    }

    private void setupByCycleAndDay(){

        mCycleAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, cycles);
        mCycleAdapter.setDropDownViewResource(R.layout.spinner_item);
        mCycleSpinner.setAdapter(mCycleAdapter);

        if (mCycle >= 3){
            mDaysAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, daysShort);
        } else {
            mDaysAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, daysFull);
        }
        mDaysAdapter.setDropDownViewResource(R.layout.spinner_item);
        mDaysSpinner.setAdapter(mDaysAdapter);

        if (mCycle == 1){
            m75Button.setVisibility(View.GONE);
            m100Button.setVisibility(View.GONE);
            findViewById(R.id.button_dose_75_text).setVisibility(View.GONE);
            findViewById(R.id.button_dose_100_text).setVisibility(View.GONE);
        }

    }

    private void addSpinnerListeners(){
        mCycleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCycle = i + 1;
                if (i >= 2){
                    mDaysAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, daysShort);
                    mDaysAdapter.setDropDownViewResource(R.layout.spinner_item);
                    mDaysSpinner.setAdapter(mDaysAdapter);
                } else {
                    mDaysAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, daysFull);
                    mDaysAdapter.setDropDownViewResource(R.layout.spinner_item);
                    mDaysSpinner.setAdapter(mDaysAdapter);
                }
                if (mCycle == 1){
                    m75Button.setVisibility(View.GONE);
                    m100Button.setVisibility(View.GONE);
                    findViewById(R.id.button_dose_75_text).setVisibility(View.GONE);
                    findViewById(R.id.button_dose_100_text).setVisibility(View.GONE);
                } else {
                    m75Button.setVisibility(View.VISIBLE);
                    m100Button.setVisibility(View.VISIBLE);
                    findViewById(R.id.button_dose_75_text).setVisibility(View.VISIBLE);
                    findViewById(R.id.button_dose_100_text).setVisibility(View.VISIBLE);
                }
                processEnableCalculateButton();
                mCalculateButton.setVisibility(View.VISIBLE);
                closeInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mDaysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getAdapter().getCount() == 1){
                    mDay = 1;
                } else {
                    switch (i){
                        case 0: mDay = 1; break;
                        case 1: mDay = 14; break;
                        case 2: mDay = 21; break;
                    }
                }
                processEnableCalculateButton();
                mCalculateButton.setVisibility(View.VISIBLE);
                closeInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_nav, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                GuiUtils.displayOkDialog(this, "Определение гематоксичности (грейда)", getString(R.string.new_version_rule), null, false);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void setNavigationButton(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.button_dose_125)
    protected void on125DoseClick(){
        mDose = Dose.DOSE_125;
        m125Button.setBackgroundResource(R.drawable.round_pill_fill);
        m100Button.setBackgroundResource(R.drawable.round_pill);
        m75Button.setBackgroundResource(R.drawable.round_pill);
        processEnableCalculateButton();
    }

    @OnClick(R.id.button_dose_100)
    protected void on100DoseClick(){
        mDose = Dose.DOSE_100;
        m125Button.setBackgroundResource(R.drawable.round_pill);
        m100Button.setBackgroundResource(R.drawable.round_pill_fill);
        m75Button.setBackgroundResource(R.drawable.round_pill);
        processEnableCalculateButton();
    }

    @OnClick(R.id.button_dose_75)
    protected void on75DoseClick(){
        mDose = Dose.DOSE_75;
        m125Button.setBackgroundResource(R.drawable.round_pill);
        m100Button.setBackgroundResource(R.drawable.round_pill);
        m75Button.setBackgroundResource(R.drawable.round_pill_fill);
        processEnableCalculateButton();
    }

    @OnClick(R.id.toxic_button_1)
    protected void onToxic1Click(){
        mToxic = 1;
        mToxic1Button.setBackgroundResource(R.drawable.cycle_button_fill);
        mToxic1Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mToxic2Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic2Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic3Button.setBackgroundResource(R.drawable.grade_3_monitoring_background);
        mToxic3Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic4Button.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.red)));
        mToxic4Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        processEnableCalculateButton();
        mCalculateButton.setVisibility(View.VISIBLE);
        closeInfo();
    }

    @OnClick(R.id.toxic_button_2)
    protected void onToxic2Click(){
        mToxic = 2;
        mToxic1Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic1Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic2Button.setBackgroundResource(R.drawable.cycle_button_fill);
        mToxic2Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mToxic3Button.setBackgroundResource(R.drawable.grade_3_monitoring_background);
        mToxic3Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic4Button.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.red)));
        mToxic4Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        processEnableCalculateButton();
        mCalculateButton.setVisibility(View.VISIBLE);
        closeInfo();
    }

    @OnClick(R.id.toxic_button_3)
    protected void onToxic3Click(){
        mToxic = 3;
        mToxic1Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic1Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic2Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic2Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic3Button.setBackgroundResource(R.drawable.cycle_button_fill);
        mToxic3Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mToxic4Button.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.red)));
        mToxic4Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        processEnableCalculateButton();
        mCalculateButton.setVisibility(View.VISIBLE);
        closeInfo();
    }

    @OnClick(R.id.toxic_button_4)
    protected void onToxic4Click(){
        mToxic = 4;
        mToxic1Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic1Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic2Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic2Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic3Button.setBackgroundResource(R.drawable.grade_3_monitoring_background);
        mToxic3Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic4Button.setBackgroundResource(R.drawable.cycle_button_fill);
        mToxic4Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        processEnableCalculateButton();
        mCalculateButton.setVisibility(View.VISIBLE);
        closeInfo();
    }

    @OnClick(R.id.calculate_button)
    protected void onCalculateButtonClick(){
        if (!checkFields()) return;

        mCalculateButton.setVisibility(View.GONE);

        TextView continueTextView = (TextView)mInfo.findViewById(R.id.continue_text);
        TextView nextOakTextView = (TextView)mInfo.findViewById(R.id.next_oak_text);
        TextView dopInfoTextView = (TextView)mInfo.findViewById(R.id.dop_info);

        setGradeUi(continueTextView, nextOakTextView, dopInfoTextView);

        mInfo.setVisibility(View.VISIBLE);
    }

    private void closeInfo(){
        TextView continueTextView = (TextView)mInfo.findViewById(R.id.continue_text);
        TextView nextOakTextView = (TextView)mInfo.findViewById(R.id.next_oak_text);
        TextView dopInfoTextView = (TextView)mInfo.findViewById(R.id.dop_info);
        continueTextView.setVisibility(View.VISIBLE);
        nextOakTextView.setVisibility(View.VISIBLE);
        dopInfoTextView.setVisibility(View.GONE);
        mInfo.setVisibility(View.GONE);
    }

    private void processEnableCalculateButton(){
        if (mCycle != -1 && mDay != -1 && mDose != null && mToxic != -1){
            mCalculateButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            mCalculateButton.setEnabled(true);
        } else {
            mCalculateButton.setEnabled(false);
        }
    }

    private boolean checkFields(){
        if (mCycle == -1){
            GuiUtils.displayOkDialog(this, "Указаны не все данные", "Укажите номер цикла", null, false);
            return false;
        }
        if (mDay == -1){
            GuiUtils.displayOkDialog(this, "Указаны не все данные", "Укажите день ОАК", null, false);
            return false;
        }
        if (mDose == null){
            GuiUtils.displayOkDialog(this, "Указаны не все данные", "Укажите текущую дозировку", null, false);
            return false;
        }
        if (mToxic == -1){
            GuiUtils.displayOkDialog(this, "Указаны не все данные", "Укажите степень гематоксичности", null, false);
            return false;
        }
        return true;
    }

    public void setGradeUi(TextView continueTextView, TextView nextOakTextView, TextView dopInfo){
        if (mCycle == 1){
            if (mDay == 1){
                continueTextView.setText(R.string.start_monitoring);
                setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_green);
                nextOakTextView.setVisibility(View.GONE);
            } else if (mDay == 14){
                if (mToxic >= 4){
                    continueTextView.setText(R.string.stop_monitoring);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_red);
                    nextOakTextView.setText(R.string.continue_in_lover_dose);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_red);
                } else if (mToxic == 3){
                    continueTextView.setText(R.string.continue_in_same_dose);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_yellow);
                    nextOakTextView.setText(R.string.control_on_21_day);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_yellow);
                } else if (mToxic <= 2){
                    continueTextView.setText(R.string.continue_in_same_dose);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_green);
                    nextOakTextView.setVisibility(View.GONE);
                }
            } else if (mDay == 21){
                if (mToxic >= 4){
                    continueTextView.setText(R.string.stop_monitoring);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_red);
                    nextOakTextView.setText(R.string.start_new_cycle_2_lover);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_red);
                } else {
                    continueTextView.setText(R.string.pause);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_green);
                    nextOakTextView.setText(R.string.start_new_cycle_2);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_green);
                }
            }
        } else if (mCycle == 2){
            if (mDay == 1){
                if (mToxic <= 2){
                    continueTextView.setText(R.string.start_monitoring_same_dose);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_green);
                    nextOakTextView.setText(R.string.control_on_14_day);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_green);
                } else if (mToxic == 3){
                    continueTextView.setText(R.string.pause_monitoring);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_yellow);
                    nextOakTextView.setText(R.string.new_cycle_with_looking);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_yellow);
                    dopInfo.setVisibility(View.VISIBLE);
                    dopInfo.setText(R.string.control_on_14_day);
                    setViewBackgroundWithoutResettingPadding(dopInfo, R.drawable.row_fill_yellow);
                } else if (mToxic > 3){
                    continueTextView.setText(R.string.delay_monitoring);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_red);
                    nextOakTextView.setText(R.string.start_new_cycle_lover);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_red);
                }
            } else if (mDay == 14){
                if (mToxic >= 4){
                    continueTextView.setText(R.string.stop_monitoring);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_red);
                    nextOakTextView.setText(R.string.continue_in_lover_dose);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_red);
                } else if (mToxic == 3) {
                    continueTextView.setText(R.string.continue_in_same_dose);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_yellow);
                    nextOakTextView.setText(R.string.control_on_21_day);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_yellow);
                } else if (mToxic <= 2){
                    continueTextView.setText(R.string.continue_in_same_dose);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_green);
                    nextOakTextView.setVisibility(View.GONE);
                }
            } else if (mDay == 21){
                if (mToxic < 4){
                    continueTextView.setText(R.string.pause);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_green);
                    nextOakTextView.setText(R.string.start_new_cycle_3);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_green);
                } else {
                    continueTextView.setText(R.string.stop_monitoring);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_red);
                    nextOakTextView.setText(R.string.start_new_cycle_3_lover);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_red);
                }
            }
        } else if (mCycle >= 3){
            if (mDay == 1){
                if (mToxic <= 2){
                    continueTextView.setText(R.string.start_monitoring_same_dose);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_green);
                    nextOakTextView.setText(R.string.control_on_14_day);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_green);
                } else if (mToxic == 3){
                    continueTextView.setText(R.string.pause_monitoring);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_yellow);
                    nextOakTextView.setText(R.string.new_cycle_with_zitopenii);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_yellow);
                } else if (mToxic > 3){
                    continueTextView.setText(R.string.delay_monitoring);
                    setViewBackgroundWithoutResettingPadding(continueTextView, R.drawable.row_fill_yellow);
                    nextOakTextView.setText(R.string.start_new_cycle_lover);
                    setViewBackgroundWithoutResettingPadding(nextOakTextView, R.drawable.row_fill_yellow);
                }
            }
        }
    }

    public void setViewBackgroundWithoutResettingPadding(final View v, final int backgroundResId) {
        final int paddingBottom = v.getPaddingBottom(), paddingLeft = v.getPaddingLeft();
        final int paddingRight = v.getPaddingRight(), paddingTop = v.getPaddingTop();
        v.setBackgroundResource(backgroundResId);
        v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

}
