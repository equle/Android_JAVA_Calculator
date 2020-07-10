package pe.kr.kys.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import pe.kr.kys.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    boolean isFirstInput = true;
    boolean isOperatorClick = false;
    double resultNumber = 0;
    double inputNumber = 0;
    String operator = "=";
    String lastoperator = "＋";
    ActivityMainBinding activityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //바인딩
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
    }

    //클릭된 뷰의 정보를 view변수에 받아와 출력 (On Click설정)
    public void numButtonClick(View view){
        // 들어온 view의 tag 추출 (tag설정)
        if (isFirstInput){
            //setText 는 기존의 데이터를 지우고 새로 등록
            activityMainBinding.resultTextView.setText(view.getTag().toString());
            isFirstInput = false;
            // 숫자 입력시 마지막 연산자 값이 =일 경우 math 값을 공백으로 초기화
            if (operator.equals("=")){
                activityMainBinding.mathTextView.setText(null);
                isOperatorClick = false;
            }
        }else {
            // 0이 계속 입력 되지 않게 조건문
            if (activityMainBinding.resultTextView.getText().toString().equals("0")){
                Toast.makeText(this, "0으로 숫자를 시작할 수 없습니다.", Toast.LENGTH_SHORT).show();
                // 00이 된 resultTextview의 값을 새로 넣을수 있도록 해준다.
                isFirstInput = true;
            }else {
                activityMainBinding.resultTextView.append(view.getTag().toString());
            }
        }
    }

    // all clear 초기화
    public void allClearButtonClick(View view){
        activityMainBinding.resultTextView.setText("0");
        activityMainBinding.mathTextView.setText("");
        resultNumber = 0;
        operator = "=";
        isFirstInput = true;
        isOperatorClick = false;
    }

    //bank space => % Button
    public void backspaceButtonClick (View view){
        // 값이 입력되있을때
        if(!isFirstInput){
            //값을 받아와, 잘라내고, 다시 입력
            String getResultText = activityMainBinding.resultTextView.getText().toString();
            // 길이가 1 보다 클경우
            if (getResultText.length() > 1) {
                String subString = getResultText.substring(0, getResultText.length() - 1);
                activityMainBinding.resultTextView.setText(subString);
            }else {
                activityMainBinding.resultTextView.setText("0");
                isFirstInput = true;
            }
        }
    }

    //소수점
    public void  pointButtonClick (View view){
        if (isFirstInput){
            activityMainBinding.resultTextView.setText("0" + view.getTag().toString());
            isFirstInput = false;
        }else {
            // contains = 문자열안에 해당 문자가 있는가
            if (activityMainBinding.resultTextView.getText().toString().contains(".")){
                Toast.makeText(this, "이미 소수점이 존재합니다.", Toast.LENGTH_SHORT).show();
            }else {
                activityMainBinding.resultTextView.append(view.getTag().toString());
            }
        }

    }

    //operator(연산자)가 클릭되면 실행
    public void operatorClick (View view){
        // equals 연산시 연산자 클릭 유무
        isOperatorClick = true;
        lastoperator = view.getTag().toString();

        if (isFirstInput) {
            if (operator.equals("=")) {
                //현재 입력된 operator 저장
                operator = view.getTag().toString();
                resultNumber = Double.parseDouble(activityMainBinding.resultTextView.getText().toString());
                activityMainBinding.mathTextView.setText(resultNumber + " " + operator + " ");
            }else {
                //현재 입력된 operator 저장
                operator = view.getTag().toString();
                // mathtextview에 있는 string 저장
                String getMathText = activityMainBinding.mathTextView.getText().toString();
                //뒤부분 연산자 잘라내기, substring(여기부터, 여기까지)
                String subString = getMathText.substring(0, getMathText.length() - 2); // 연산자와 공백
                // 입력
                activityMainBinding.mathTextView.setText(subString);
                activityMainBinding.mathTextView.append(operator + " ");
            }
        }else {
            //isFirstInput이 false일경우 데이터를 한번이라도 입력한 적이 있다는 것
            //연산할 값을 변수에 저장
            // 문자열을 더블 형으로 바꿔주는 Double Class 안의 메소드 => parseDouble
            inputNumber = Double.parseDouble(activityMainBinding.resultTextView.getText().toString());

            resultNumber = calculator(resultNumber, inputNumber, operator);
            // double형을 String형으로 바꿔 출력
            activityMainBinding.resultTextView.setText(String.valueOf(resultNumber));
            // 연산후 resultTextView 초기화
            isFirstInput = true;
            // 오퍼레이트 값을 저장 (숫자 -> 연산자 -> 숫자 순)
            operator = view.getTag().toString();
            activityMainBinding.mathTextView.append(inputNumber + " " + operator + " ");
        }
    }

    // equals 연산
    public void equalsButtonClick (View view){
        if(isFirstInput){
            // 다른 연산자가 눌렸을 경우 실행
            if (isOperatorClick){
                //이전 연산자와 같이 mathtextview에 출력
                activityMainBinding.mathTextView.setText(resultNumber + " " + lastoperator + " " + inputNumber + "=");
                //연산
                resultNumber = calculator(resultNumber, inputNumber, lastoperator);
                //연산값을 resultNumber에 저장 후 출력
                activityMainBinding.resultTextView.setText(String.valueOf(resultNumber));
            }
        }else {
            //연산할 값을 변수에 저장
            // 문자열을 더블 형으로 바꿔주는 Double Class 안의 메소드 => parseDouble
            inputNumber = Double.parseDouble(activityMainBinding.resultTextView.getText().toString());
            // 연산 메소드 calculator
            resultNumber = calculator(resultNumber, inputNumber, operator);

            // double형을 String형으로 바꿔 출력
            activityMainBinding.resultTextView.setText(String.valueOf(resultNumber));
            // 연산후 setText로 입력되기 위해
            isFirstInput = true;
            // 오퍼레이트 값을 저장 (숫자 -> 연산자 -> 숫자 순)
            operator = view.getTag().toString();
            activityMainBinding.mathTextView.append(inputNumber + " " + operator + " ");
        }

    }

    // 연산 메소드
    private double calculator(double resultNumber, double inputNumber, String operator) {
        //스위치 케이스 문으로 연산
        switch (operator){
            case "=":
                resultNumber = inputNumber;
                break;
            case "＋":
                resultNumber += inputNumber;
                break;
            case "－":
                resultNumber -= inputNumber;
                break;
            case "×":
                resultNumber *= inputNumber;
                break;
            case "÷":
                resultNumber /= inputNumber;
                break;
        }
        return resultNumber;
    }


}