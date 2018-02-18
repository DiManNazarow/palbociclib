package ru.mbg.palbociclib.new_version.gui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.new_version.models.Dose;
import ru.mbg.palbociclib.utils.GuiUtils;


public class MonitoringActivity extends AppCompatActivity {

    public static final String CYCLE = "cycle";
    public static final String COUNT = "count";

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

    @BindView(R.id.cycle_count_text_view)
    protected TextView mCycleCountTextView;
    @BindView(R.id.day_count_text_view)
    protected TextView mDayCountTextView;

    @BindView(R.id.oak_by_plan)
    protected TextView mOakByPlan;
    @BindView(R.id.control_oak)
    protected TextView mControlOak;
    @BindView(R.id.oak_out_of_plan)
    protected TextView mOakOutOfPlan;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private int mCycle = -1;

    private Dose mDose = null;

    private int mToxic = -1;

    private int mDay = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbar.setTitle(R.string.new_version_monitoring_activity_title);
        mToolbar.setTitleTextColor(Color.WHITE);
        setTitle(R.string.new_version_monitoring_activity_title);
        setNavigationButton();

        if (hasExtra()){
            mCycle = getIntent().getIntExtra(CYCLE, -1);
            mDay = getIntent().getIntExtra(COUNT, -1);
            setupByCycleAndDay();
        }
        mCalculateButton.setEnabled(false);
    }


    private boolean hasExtra(){
        return getIntent().hasExtra(CYCLE) || getIntent().hasExtra(COUNT);
    }

    private void setupByCycleAndDay(){
        if (mCycle != -1) {
            mCycleCountTextView.setText(getString(R.string.new_version_cycle_count, String.valueOf(mCycle)));
        } else {
            mCycleCountTextView.setText(getString(R.string.new_version_cycle_count, "-"));
        }
        if (mDay != -1) {
            mDayCountTextView.setText(getString(R.string.new_version_day_count, String.valueOf(mDay)));
        } else {
            mDayCountTextView.setText(getString(R.string.new_version_day_count_empty));
        }

        switch (mDay){
            case 1:{
                mOakByPlan.setVisibility(View.VISIBLE);
                mControlOak.setVisibility(View.GONE);
                mOakOutOfPlan.setVisibility(View.GONE);
                break;
            }
            case 14:{
                mOakByPlan.setVisibility(View.VISIBLE);
                mControlOak.setVisibility(View.GONE);
                mOakOutOfPlan.setVisibility(View.GONE);
                break;
            }
            case 21:{
                mOakByPlan.setVisibility(View.GONE);
                mControlOak.setVisibility(View.VISIBLE);
                mOakOutOfPlan.setVisibility(View.GONE);
                break;
            }
            default:{
                mOakByPlan.setVisibility(View.GONE);
                mControlOak.setVisibility(View.GONE);
                mOakOutOfPlan.setVisibility(View.VISIBLE);
                break;
            }
        }

        if (mCycle == 1){
            m75Button.setVisibility(View.GONE);
            m100Button.setVisibility(View.GONE);
            findViewById(R.id.button_dose_75_text).setVisibility(View.GONE);
            findViewById(R.id.button_dose_100_text).setVisibility(View.GONE);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_nav, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.info:
//                GuiUtils.displayCustomOkDialog(this, "Оценка гематологической токсичности CTCAE 4.0 (Common Terminology Criteria for Adverse Events)", getString(R.string.new_version_rule), null, false);
//                break;
//            case android.R.id.home:
//                finish();
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    @OnClick(R.id.toxic_info_text)
    public void onToxicInfoClick(){
        GuiUtils.displayCustomOkDialog(this, "Оценка гематологической токсичности CTCAE 4.0 (Common Terminology Criteria for Adverse Events)", getString(R.string.new_version_rule), null, false);
    }

    private void setNavigationButton(){
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        mToxic1Button.setBackgroundResource(R.drawable.start_activity_cycle_button_left_fill);
        mToxic1Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mToxic2Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic2Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic3Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic3Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic4Button.setBackgroundResource(R.drawable.start_activity_cycle_button_right);
        mToxic4Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        processEnableCalculateButton();
        mCalculateButton.setVisibility(View.VISIBLE);
        closeInfo();
    }

    @OnClick(R.id.toxic_button_2)
    protected void onToxic2Click(){
        mToxic = 2;
        mToxic1Button.setBackgroundResource(R.drawable.start_activity_cycle_button_left);
        mToxic1Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic2Button.setBackgroundResource(R.drawable.cycle_button_fill);
        mToxic2Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mToxic3Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic3Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic4Button.setBackgroundResource(R.drawable.start_activity_cycle_button_right);
        mToxic4Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        processEnableCalculateButton();
        mCalculateButton.setVisibility(View.VISIBLE);
        closeInfo();
    }

    @OnClick(R.id.toxic_button_3)
    protected void onToxic3Click(){
        mToxic = 3;
        mToxic1Button.setBackgroundResource(R.drawable.start_activity_cycle_button_left);
        mToxic1Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic2Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic2Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic3Button.setBackgroundResource(R.drawable.cycle_button_fill);
        mToxic3Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mToxic4Button.setBackgroundResource(R.drawable.start_activity_cycle_button_right);
        mToxic4Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        processEnableCalculateButton();
        mCalculateButton.setVisibility(View.VISIBLE);
        closeInfo();
    }

    @OnClick(R.id.toxic_button_4)
    protected void onToxic4Click(){
        mToxic = 4;
        mToxic1Button.setBackgroundResource(R.drawable.start_activity_cycle_button_left);
        mToxic1Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic2Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic2Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic3Button.setBackgroundResource(R.drawable.cycle_button);
        mToxic3Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        mToxic4Button.setBackgroundResource(R.drawable.start_activity_cycle_button_right_fill);
        mToxic4Button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        processEnableCalculateButton();
        mCalculateButton.setVisibility(View.VISIBLE);
        closeInfo();
    }

    @OnClick(R.id.calculate_button)
    protected void onCalculateButtonClick(){
        if (!checkFields()) return;

        mCalculateButton.setVisibility(View.GONE);

        TextView first = (TextView)mInfo.findViewById(R.id.first_action);
        TextView second = (TextView)mInfo.findViewById(R.id.second_action);
        TextView third = (TextView)mInfo.findViewById(R.id.third_action);
        TextView fourth = (TextView)mInfo.findViewById(R.id.fourth_action);

        setGradeUi(first, second, third, fourth);

        mInfo.setVisibility(View.VISIBLE);
    }

    private void closeInfo(){
        TextView continueTextView = (TextView)mInfo.findViewById(R.id.first_action);
        TextView nextOakTextView = (TextView)mInfo.findViewById(R.id.second_action);
        TextView dopInfoTextView = (TextView)mInfo.findViewById(R.id.third_action);
        TextView fourth = (TextView)mInfo.findViewById(R.id.fourth_action);
        continueTextView.setVisibility(View.VISIBLE);
        nextOakTextView.setVisibility(View.VISIBLE);
        dopInfoTextView.setVisibility(View.GONE);
        fourth.setVisibility(View.GONE);
        mInfo.setVisibility(View.GONE);
    }

    private void processEnableCalculateButton(){
        if (mCycle != -1 && mDose != null && mToxic != -1){
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

    public void setGradeUi(TextView first, TextView second, TextView third, TextView fourth){
        if (mCycle == 1){
            if (mDay == 1){
                if (mToxic == 1) {
                    first.setText(R.string.start_monitoring);
                    first.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                    second.setText("Оценить ОАК на 15 день");
                    second.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                } else {
                    first.setText(R.string.delay_monitoring);
                    first.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                    second.setVisibility(View.GONE);
                }
            } else if (mDay == 14){
                if (mToxic >= 4){
                    first.setText(R.string.stop_monitoring);
                    first.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                    second.setText(R.string.continue_in_lover_dose);
                    second.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    third.setText("ОАК в 1 и 15 день нового цикла и по клиническим показаниям");
                    third.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                } else if (mToxic == 3){
                    first.setText(R.string.continue_in_same_dose);
                    setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                    second.setText(R.string.control_on_21_day);
                    second.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                } else if (mToxic <= 2){
                    first.setText(R.string.continue_in_same_dose);
                    setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                    second.setText("Плановый перерыв через 7 дней (22-28 дни цикла)");
                    second.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    third.setText("Начать следующий цикл в прежней дозе");
                    third.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_green);
                    fourth.setText("ОАК в 1 и 15 день нового цикла и по клиническим показаниям");
                    fourth.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(fourth, R.drawable.row_fill_green);
                }
            } else if (mDay == 21){
                if (mToxic >= 4){
                    first.setText(R.string.stop_monitoring);
                    setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                    second.setText(R.string.continue_in_lover_dose);
                    second.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    third.setText("ОАК на 1 и 15 день нового цикла и по клиническим показаниям");
                    third.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                } else {
                    first.setText(R.string.pause);
                    setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                    second.setText(R.string.start_new_cycle_2);
                    second.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    third.setText("ОАК в 1, 15 день и по клиническим показаниям");
                    third.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_green);
                }
            } else if (mDay == -1){
                if (mToxic >= 4){
                    first.setText("Прервать прием до восстановления количества клеток до степени <= 2");
                    setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                    second.setText("Возобновить прием в пониженной дозе");
                    second.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                } else {
                    first.setText("Продолжить прием в прежней дозе");
                    setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                    second.setText("Контроль ОАК с учетом плана и клинических показаний");
                    second.setVisibility(View.VISIBLE);
                    setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                }
            }
        } else if (mCycle == 2){
            if (mDose.equals(Dose.DOSE_125)) {
                if (mDay == 1) {
                    if (mToxic <= 2) {
                        first.setText(R.string.start_monitoring_same_dose);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Оценить ОАК на 15 день");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    } else if (mToxic == 3) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText(R.string.new_cycle_with_looking);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Оценить ОАК на 15 день");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_yellow);
                    } else if (mToxic > 3) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Начать цикл в пониженной дозе при восстановлении показателей ОАК до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    }
                } else if (mDay == 14) {
                    if (mToxic >= 4) {
                        first.setText("Прервать прием до восстановления количества клеток до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText(R.string.continue_in_lover_dose);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    } else if (mToxic == 3) {
                        first.setText(R.string.continue_in_same_dose);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText(R.string.control_on_21_day);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                    } else if (mToxic <= 2) {
                        first.setText(R.string.continue_in_same_dose);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setVisibility(View.VISIBLE);
                        second.setText("Плановый перерыв через 7 дней (22-28 дни цикла)");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Начать следующий цикл в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_green);
                        fourth.setVisibility(View.VISIBLE);
                        fourth.setText("ОАК перед началом нового цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(fourth, R.drawable.row_fill_green);
                    }
                } else if (mDay == 21) {
                    if (mToxic < 4) {
                        first.setText(R.string.pause);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Начать следующий цикл в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                        third.setVisibility(View.VISIBLE);
                        third.setText("ОАК перед началом нового цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_green);
                    } else {
                        first.setText(R.string.stop_monitoring);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText(R.string.start_new_cycle_3_lover);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                        third.setVisibility(View.VISIBLE);
                        third.setText("ОАК в 1 день цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                    }
                } else if (mDay == -1){
                    if (mToxic >= 4){
                        first.setText("Прервать прием до восстановления количества клеток до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Возобновить прием в пониженной дозе");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    } else {
                        first.setText("Продолжить прием в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Контроль ОАК с учетом плана и клинических показаний");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    }
                }
            } else if (mDose.equals(Dose.DOSE_100)){
                if (mDay == 1) {
                    if (mToxic <= 2) {
                        first.setText("Продолжить прием в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Оценить ОАК на 15 день");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    } else if (mToxic == 3) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText(R.string.new_cycle_with_looking);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Оценить ОАК на 15 день");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_yellow);
                    } else if (mToxic > 3) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Начать цикл в пониженной дозе при восстановлении показателей ОАК до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    }
                } else if (mDay == 14) {
                    if (mToxic >= 4) {
                        first.setText("Прервать прием до восстановления количества клеток до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText(R.string.continue_in_lover_dose);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    } else if (mToxic == 3) {
                        first.setText(R.string.continue_in_same_dose);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText(R.string.control_on_21_day);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                    } else if (mToxic <= 2) {
                        first.setText(R.string.continue_in_same_dose);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setVisibility(View.VISIBLE);
                        second.setText("Плановый перерыв через 7 дней (22-28 дни цикла)");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Начать следующий цикл в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_green);
                        fourth.setVisibility(View.VISIBLE);
                        fourth.setText("ОАК перед началом нового цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(fourth, R.drawable.row_fill_green);
                    }
                } else if (mDay == 21) {
                    if (mToxic < 4) {
                        first.setText(R.string.pause);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Начать следующий цикл в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                        third.setVisibility(View.VISIBLE);
                        third.setText("ОАК перед началом нового цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_green);
                    } else {
                        first.setText(R.string.stop_monitoring);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText(R.string.start_new_cycle_3_lover);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                        third.setVisibility(View.VISIBLE);
                        third.setText("ОАК в 1 день цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                    }
                } else if (mDay == -1){
                    if (mToxic >= 4){
                        first.setText("Прервать прием до восстановления количества клеток до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Возобновить прием в пониженной дозе");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    } else {
                        first.setText("Продолжить прием в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Контроль ОАК с учетом плана и клинических показаний");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    }
                }
            } else if (mDose.equals(Dose.DOSE_75)){
                if (mDay == 1) {
                    if (mToxic <= 2) {
                        first.setText("Продолжить прием в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Оценить ОАК на 15 день");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    } else if (mToxic == 3) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText("Начать цикл при восстановлении показателей ОАК до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Оценить ОАК на 15 день");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_yellow);
                    } else if (mToxic > 3) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Начать цикл в пониженной дозе при восстановлении показателей ОАК до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Если требуется снизить дозу < 75 мг, необходимо прекратить прием препарата");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                    }
                } else if (mDay == 14) {
                    if (mToxic >= 4) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Начать цикл в пониженной дозе при восстановлении показателей ОАК до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Если требуется снизить дозу < 75 мг, необходимо прекратить прием препарата");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                    } else if (mToxic == 3) {
                        first.setText(R.string.continue_in_same_dose);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText(R.string.control_on_21_day);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                    } else if (mToxic <= 2) {
                        first.setText(R.string.continue_in_same_dose);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setVisibility(View.VISIBLE);
                        second.setText("Плановый перерыв через 7 дней (22-28 дни цикла)");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Начать следующий цикл в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_green);
                        fourth.setVisibility(View.VISIBLE);
                        fourth.setText("ОАК перед началом нового цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(fourth, R.drawable.row_fill_green);
                    }
                } else if (mDay == 21) {
                    if (mToxic < 4) {
                        first.setText(R.string.pause);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Начать следующий цикл в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                        third.setVisibility(View.VISIBLE);
                        third.setText("ОАК перед началом нового цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_green);
                    } else {
                        first.setText(R.string.stop_monitoring);
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("ОАК в 1 день цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Если требуется снизить дозу < 75 мг, необходимо прекратить прием препарата");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                    }
                } else if (mDay == -1) {
                    if (mToxic >= 4) {
                        first.setText("Прервать прием до восстановления количества клеток до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Возобновить прием в пониженной дозе");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Если требуется снизить дозу < 75 мг, необходимо прекратить прием препарата");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                    } else {
                        first.setText("Продолжить прием в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Контроль ОАК с учетом плана и клинических показаний");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    }
                }
            }
        } else if (mCycle >= 3){
            if (mDose.equals(Dose.DOSE_125)) {
                if (mDay == 1) {
                    if (mToxic <= 2) {
                        first.setText("Продолжить прием в прежней дозе по схеме 3 недели приема/1 неделя перерыв");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Контроль ОАК перед началом каждого цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    } else if (mToxic == 3) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText("Начать цикл при восстановлении показателей ОАК до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Рассмотреть снижение дозы, если это повторный эпизод токсичности 3 степени, или замедленное восстановление (>1 недели)");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_yellow);
                        fourth.setVisibility(View.VISIBLE);
                        fourth.setText("Контроль ОАК перед началом каждого цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(fourth, R.drawable.row_fill_yellow);
                    } else if (mToxic > 3) {
                        first.setText("Перервать прием до восстановления показателей <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText(R.string.start_new_cycle_lover);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Контроль ОАК перед началом каждого цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                    }
                } else if (mDay == -1){
                    if (mToxic >= 4){
                        first.setText("Прервать прием до восстановления количества клеток до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Возобновить прием в пониженной дозе");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    } else {
                        first.setText("Продолжить прием в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Контроль ОАК с учетом плана и клинических показаний");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    }
                }
            } else if (mDose.equals(Dose.DOSE_100)){
                if (mDay == 1) {
                    if (mToxic <= 2) {
                        first.setText("Продолжить прием в прежней дозе по схеме 3 недели приема/1 неделя перерыв");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Контроль ОАК перед началом каждого цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    } else if (mToxic == 3) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText("Начать цикл при восстановлении показателей ОАК до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Рассмотреть снижение дозы, если это повторный эпизод токсичности 3 степени, или замедленное восстановление (>1 недели)");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_yellow);
                        fourth.setVisibility(View.VISIBLE);
                        fourth.setText("Контроль ОАК перед началом каждого цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(fourth, R.drawable.row_fill_yellow);
                    } else if (mToxic > 3) {
                        first.setText("Перервать прием до восстановления показателей <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText(R.string.start_new_cycle_lover);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Контроль ОАК перед началом каждого цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_yellow);
                    }
                } else if (mDay == -1){
                    if (mToxic >= 4){
                        first.setText("Прервать прием до восстановления количества клеток до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Возобновить прием в пониженной дозе");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                    } else {
                        first.setText("Продолжить прием в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Контроль ОАК с учетом плана и клинических показаний");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    }
                }
            } else if (mDose.equals(Dose.DOSE_75)){
                if (mDay == 1) {
                    if (mToxic <= 2) {
                        first.setText("Продолжить прием в прежней дозе по схеме 3 недели приема/1 неделя перерыв");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Контроль ОАК перед началом каждого цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    } else if (mToxic == 3) {
                        first.setText("Отложить прием. ОАК повторить через 1 неделю");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_yellow);
                        second.setText("Начать цикл при восстановлении показателей ОАК до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_yellow);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Если требуется снизить дозу <75 мг, необходимо прекратить прием препарата");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_yellow);
                        fourth.setVisibility(View.VISIBLE);
                        fourth.setText("Контроль ОАК перед началом каждого цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(fourth, R.drawable.row_fill_yellow);
                    } else if (mToxic > 3) {
                        first.setText("Перервать прием до восстановления показателей <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Если требуется снизить дозу <75 мг, необходимо прекратить прием препарата");
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Контроль ОАК перед началом каждого цикла и по клиническим показаниям");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                    }
                } else if (mDay == -1){
                    if (mToxic >= 4){
                        first.setText("Прервать прием до восстановления количества клеток до степени <= 2");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_red);
                        second.setText("Возобновить прием в пониженной дозе");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_red);
                        third.setVisibility(View.VISIBLE);
                        third.setText("Если требуется снизить дозу < 75 мг, необходимо прекратить прием препарата");
                        setViewBackgroundWithoutResettingPadding(third, R.drawable.row_fill_red);
                    } else {
                        first.setText("Продолжить прием в прежней дозе");
                        setViewBackgroundWithoutResettingPadding(first, R.drawable.row_fill_green);
                        second.setText("Контроль ОАК с учетом плана и клинических показаний");
                        second.setVisibility(View.VISIBLE);
                        setViewBackgroundWithoutResettingPadding(second, R.drawable.row_fill_green);
                    }
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
