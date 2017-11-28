package com.andreasgift.totalzero;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


//Total Zero. The app to train your brain by using simple math
public class MainActivity extends AppCompatActivity {
    MenuItem easyChecked;
    MenuItem difficultCheck;

    final String LVLSHRPREFS = "levelSharedPrefs";
    SharedPreferences levelShrdPrefs;
    int LVLEASYSHAREDPREFSS = 1;
    int LVLDIFFCLTSHRDPREFS =0;
    int difficultyMode;

    TextView welcomeText;
    TextView scoreText;
    TextView timerText;
    TextView variable1;
    TextView variable2;
    TextView variable3;
    TextView variable4;
    TextView variable5;
    TextView variable6;
    TextView variable7;
    TextView variable8;
    TextView sign1;
    TextView sign2;
    TextView sign3;
    TextView sign4;
    TextView sign5;
    TextView sign6;
    TextView equalSign;

    Button restartButton;
    String equationString;

    TextView equationText;
    CountDownTimer timerMachine;

    Random randon;
    int seed = 0;
    int score = 0;
    int timer = 60000; //60 sec for each stage
    private int baseNumber;

    MediaPlayer backSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        levelShrdPrefs = getSharedPreferences(LVLSHRPREFS, Context.MODE_PRIVATE);
        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("key")){
                    difficultyMode = sharedPreferences.getInt(key,LVLDIFFCLTSHRDPREFS);
                    if (difficultyMode == 1){
                    Toast.makeText(getApplicationContext(),"Level : Easy",Toast.LENGTH_LONG).show();
                }else {
                        Toast.makeText(getApplicationContext(),"Level : Difficult",Toast.LENGTH_LONG).show();
                    }}
            }
        };
        levelShrdPrefs.registerOnSharedPreferenceChangeListener(listener);

        randon = new Random(seed);
        baseNumber = 1470;

        welcomeText = (TextView) findViewById(R.id.welcome_text);
        equationText = (TextView) findViewById(R.id.equation_text);
        scoreText = (TextView) findViewById(R.id.score);
        timerText = (TextView) findViewById(R.id.timer);

        variable1 = (TextView) findViewById(R.id.variabel1);
        variable2 = (TextView) findViewById(R.id.variabel2);
        variable3 = (TextView) findViewById(R.id.variabel3);
        variable4 = (TextView) findViewById(R.id.variabel4);
        variable5 = (TextView) findViewById(R.id.variabel5);
        variable6 = (TextView) findViewById(R.id.variabel6);
        variable7 = (TextView) findViewById(R.id.variabel7);
        variable8 = (TextView) findViewById(R.id.variabel8);
        sign1 = (TextView) findViewById(R.id.sign1);
        sign2 = (TextView) findViewById(R.id.sign2);
        sign3 = (TextView) findViewById(R.id.sign3);
        sign4 = (TextView) findViewById(R.id.sign4);
        sign5 = (TextView) findViewById(R.id.sign5);
        sign6 = (TextView) findViewById(R.id.sign6);
        equalSign = (TextView) findViewById(R.id.equalsign);

        restartButton = (Button) findViewById(R.id.restart_button);
        setAllNumber();


        welcomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backSound = MediaPlayer.create(MainActivity.this, R.raw.heyyou);
                backSound.setLooping(true);

                view.setVisibility(View.INVISIBLE);
                setAllNumber();
                score = 0;
                scoreText.setText(toStr(score));
                timerMachine.start();
                backSound.start();
            }
        });

        final Handler handler = new Handler();

        equalSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equationString = equationString + " =";
                equationText.setText(equationString);
                equationText.setText(equationString + toStr(calculate()));
                if (calculate() == 0) {
                    score = score + 1;
                    scoreText.setText("Score: " + score);}

                    //set the new set of number after display the previous result for 6 sec
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setAllNumber();
                        }
                    }, 2000);
                }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcomeText.setVisibility(View.INVISIBLE);
                setAllNumber();
            }
        });

        timerMachine = new CountDownTimer(timer, 1000) {

            @Override
            public void onTick(long miliSecBfrFinish) {
                timerText.setText(Long.toString(miliSecBfrFinish / 1000));
            }

            @Override
            public void onFinish() {
                welcomeText.setText("Your score is :" + score + "\n" + "Click & play again");
                timerText.setText("0");
                welcomeText.setVisibility(View.VISIBLE);
                backSound.stop();
                backSound.reset();
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        easyChecked = menu.findItem(R.id.level_easy);
        difficultCheck = menu.findItem(R.id.level_difficult);
        if (difficultyMode == LVLEASYSHAREDPREFSS){
            easyChecked.setChecked(true);
            difficultCheck.setChecked(false);
        }else {
            easyChecked.setChecked(false);
            difficultCheck.setChecked(true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = levelShrdPrefs.edit();
        if (item.getItemId() == R.id.level_easy){
            editor.putInt("key",LVLEASYSHAREDPREFSS);
            editor.commit();
            item.setChecked(true);
            difficultCheck.setChecked(false);
                return true;}
                else{
            editor.putInt("key",LVLDIFFCLTSHRDPREFS);
            editor.commit();
            item.setChecked(true);
            easyChecked.setChecked(false);
                return true;
        }
    }

    //Set all number and sign on the TEXTvIEW
    private void setAllNumber() {
        if (difficultyMode == LVLEASYSHAREDPREFSS){
            baseNumber = randomInt(8,33);
        }else {
            baseNumber = randomInt(146, 1499);
        }
        equationString = toStr(baseNumber);
        equationText.setText(equationString);

        variable1.setText(toStr(randomInt(0, 10)));
        variable1.setVisibility(View.VISIBLE);
        variable2.setText(toStr(randomInt(0, 10)));
        variable2.setVisibility(View.VISIBLE);
        variable3.setText(toStr(randomInt(0, 10)));
        variable3.setVisibility(View.VISIBLE);
        variable4.setText(toStr(randomInt(0, 10)));
        variable4.setVisibility(View.VISIBLE);
        variable5.setText(toStr(randomInt(0, 10)));
        variable5.setVisibility(View.VISIBLE);
        variable6.setText(toStr(randomInt(0, 10)));
        variable6.setVisibility(View.VISIBLE);
        variable7.setText(toStr(randomInt(0, 10)));
        variable7.setVisibility(View.VISIBLE);
        variable8.setText(toStr(randomInt(0, 10)));
        variable8.setVisibility(View.VISIBLE);
        sign1.setText(returnSign());
        sign1.setVisibility(View.VISIBLE);
        sign2.setText(returnSign());
        sign2.setVisibility(View.VISIBLE);
        sign3.setText(returnSign());
        sign3.setVisibility(View.VISIBLE);
        sign4.setText(returnSign());
        sign4.setVisibility(View.VISIBLE);
        sign5.setText(returnSign());
        sign5.setVisibility(View.VISIBLE);
        sign6.setText(returnSign());
        sign6.setVisibility(View.VISIBLE);
        equalSign.setText("=");

        clickVariable(variable1);
        clickVariable(variable2);
        clickVariable(variable3);
        clickVariable(variable4);
        clickVariable(variable5);
        clickVariable(variable6);
        clickVariable(variable7);
        clickVariable(variable8);
        clickVariable(sign1);
        clickVariable(sign2);
        clickVariable(sign3);
        clickVariable(sign4);
        clickVariable(sign5);
        clickVariable(sign6);
    }


    //This method to set the action happen after click each variable
    public void clickVariable(TextView sview) {
        sview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((TextView) view).getText().toString().equals("()")) {
                    ((TextView) view).setText(")");
                    equationString = equationString + "(";
                    equationText.setText(equationString);
                } else {
                    view.setVisibility(View.INVISIBLE);
                    equationString = equationString + ((TextView) view).getText().toString();
                    equationText.setText(equationString);
                }
            }
        });
    }

    public int calculate() {
        int tempNumb = -1;
        int result = 0;
        ArrayList<Integer> arrayNumb = new ArrayList<Integer>();
        ArrayList<String> arraySign = new ArrayList<String>();

        //Classified the string and number into 2 different array
        for (int i = 0; i < equationString.length(); i++) {
            String chars = String.valueOf(equationString.charAt(i));
            if (chars.matches("\\d+(?:\\.\\d+)?")) {
                if (tempNumb == -1 && Integer.parseInt(chars)>0) {
                    tempNumb = Integer.parseInt(chars);
                }
                else if (tempNumb > 0) {
                    tempNumb = tempNumb * 10 + Integer.parseInt(chars);
                }
                else {
                    tempNumb = Integer.parseInt(chars);
                }
            } else {
                if (chars.equals(" ")) {
                } else if (tempNumb >= 0) {
                    arrayNumb.add(tempNumb);
                    tempNumb = -1;
                    arraySign.add(chars);
                } else {
                    arraySign.add(chars);
                }
            }
        }


        //Calculation determine whether () sign is exist or not exist on the ArraySign
        result = arrayNumb.get(0);
        if (arrayNumb.size() == arraySign.size()) {
            result = linearArrayCalc(arrayNumb, arraySign);
        } else {
            int a = 0; //index where ( sign on arraySign
            int b = 0; //index where ) sign on arraySign

            for (int i = 0; i < arraySign.size() - 1; i++) {
                if (whatSign(arraySign.get(i)) == 5) {
                    a = i;
                }
                if (whatSign(arraySign.get(i)) == 6) {
                    b = i;
                }
            }

            //calculate the result inside ()
            ArrayList<Integer> number = new ArrayList<>();
            ArrayList<String> sign = new ArrayList<>();

            for (int i = a; i < b; i++) {
                number.add(arrayNumb.get(i));
                sign.add(arraySign.get(i + 1));
            }
            int variableReplacement = linearArrayCalc(number, sign);

            //rearrange the arrayNumb by eliminate the calculation inside ()
            while (arrayNumb.size() > a) {
                arrayNumb.remove(a);
            }
            arrayNumb.add(a, variableReplacement);

            //rearrange the arraySign by elimate the sign inside ()
            while (arraySign.size() > a + 1) {
                arraySign.remove(a);
            }

            //finally recalculate the simplfying formula
            result = linearArrayCalc(arrayNumb, arraySign);
        }
        return result;
    }


    //This method to calculate linear formula  within two variables
    //Ex a+b, a-b, axb; a=baseNumb & b=nextNumb
    public int linearCalc(String sign, int baseNumb, int nextNumb) {
        double tempResult = 0;
        if (whatSign(sign) == 1) {return baseNumb + nextNumb;}
        if (whatSign(sign) == 2) {return baseNumb - nextNumb;}
        if (whatSign(sign) == 3) {tempResult = baseNumb * (double)nextNumb;
        return (int)tempResult;}
        if (whatSign(sign) == 4) {
            if (nextNumb > baseNumb){
                Toast.makeText(getApplicationContext(), "Invalid argument. Restart the problem ", Toast.LENGTH_SHORT).show();
            tempResult =1;}
            else{
            tempResult =  baseNumb / (double)nextNumb;}
        return  (int)tempResult;}
        if (whatSign(sign) == 7) {return baseNumb;}
        else {return 0;}
    }

    //This method to calculate the linear formula with more than two variable
    // Ex: a+b-c; axb-c; etc
    public int linearArrayCalc(ArrayList<Integer> number, ArrayList<String> sign) {
        int result = number.get(0);
        if (number.size() == sign.size()) {
            for (int i = 0; i < number.size() - 1; i++) {
                result = linearCalc(sign.get(i), result, number.get(i + 1));
            }
        } else {
            Toast.makeText(this, "Error on input", Toast.LENGTH_LONG).show();
        }
        return result;
    }

    //This method to identify what is the sign on the current char
    public int whatSign(String chars) {
        String sign1 = "+";
        String sign2 = "-";
        String sign3 = "x";
        String sign4 = ":";
        String sign5 = "(";
        String sign6 = ")";
        String sign7 = "=";

        if (chars.equals(sign1)) {
            return 1;
        }
        if (chars.equals(sign2)) {
            return 2;
        }
        if (chars.equals(sign3)) {
            return 3;
        }
        if (chars.equals(sign4)) {
            return 4;
        }
        if (chars.equals(sign5)) {
            return 5;
        }
        if (chars.equals(sign6)) {
            return 6;
        }
        if (chars.equals(sign6)) {
            return 7;
        } else {
            return 0;
        }
    }


    //This method the return sign randomly. This sign will be plotted as grid layout
    public String returnSign() {
        String sign1 = "+";
        String sign2 = "-";
        String sign3 = "x";
        String sign4 = ":";
        String sign0 = "()";

        int no = randomInt(0, 5);

        switch (no) {
            case 1:
                return sign1;
            case 2:
                return sign2;
            case 3:
                return sign3;
            case 4:
                return sign4;
            default:
                return sign0;
        }
    }

    //This method to create random int with range from low to high
    public int randomInt(int low, int high) {
        Random r = new Random();
        int Low = low;
        int High = high;
        int result = r.nextInt(High - Low) + Low;
        return result;
    }

    //This method to convert int to String
    public String toStr(int number) {
        return Integer.toString(number);
    }


}



