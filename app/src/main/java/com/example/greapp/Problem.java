package com.example.greapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.RequiresApi;

public class Problem extends LinearLayout {

    LayoutInflater inflater;

    int count=0;
    TextView txt;
    String j;
    Button[] btn = new Button[4];

    int prog = 1;

    Question que;
    List<Answer> ans;
    Integer[] intArray = { 0, 1, 2, 3 };
    int[] arr;

    Boolean bool=true;
    QuestionHelper dbhelper;


    TextToSpeech t1;

    public Problem(Context context) {
        super(context);
        initView(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for(int i = 0 ; i < getChildCount() ; i++){
            getChildAt(i).layout(l, t, r, b);
        }
    }



    //  Click events Below >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    private int generate( int j)
    {
        Random random = new Random();
        int i=0;
        if(j==51) {
            i = random.nextInt(51);
            if (i == 0) {
                i++;
            }
        }
        return i;
    }

    private int[] shuffle(Integer[] intArray)
    {
        List<Integer> intList = Arrays.asList(intArray);

        Collections.shuffle(intList);
        int[] arr = new int[4];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            arr = intList.stream().mapToInt(Integer::intValue).toArray();
        }

        return arr;

    }

    private void initView(final Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.overlay, null);

        // mStorageRef = FirebaseStorage.getInstance().getReference();

        ProgressWheel pw = view.findViewById(R.id.pw_spinner);

        pw.incrementProgress(prog * 20);
        pw.setText(prog + "/5");
        //pw.startSpinning();


        txt = view.findViewById(R.id.que);

        btn[0] = view.findViewById(R.id.btn1);
        btn[1] = view.findViewById(R.id.btn2);
        btn[2] = view.findViewById(R.id.btn3);
        btn[3] = view.findViewById(R.id.btn4);


        Button skip = view.findViewById(R.id.skip);


        t1 = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {
                t1.setLanguage(Locale.UK);
            }
        });


        skip.setOnClickListener(v -> {

            if (skip.getText().equals("Next") && prog < 6) {
                prog++;
                pw.incrementProgress(prog * 20);
                pw.setText(prog + "/5");
            } else if (skip.getText().equals("Next") && prog > 5)
                context.stopService(new Intent(getContext(), ServiceOverlay.class));


            skip.setText("Skip");

            btn[0].setTextColor(Color.WHITE);
            btn[1].setTextColor(Color.WHITE);
            btn[2].setTextColor(Color.WHITE);
            btn[3].setTextColor(Color.WHITE);

            bool = true;

            que = dbhelper.getQuestion(generate(51));
            ans = dbhelper.get_answer();
            arr = shuffle(intArray);

            txt.setText(que.getQuestions());

            for (int i0 = 0; i0 < 3; i0++) {
                String x = ans.get(generate(51)).getAnswer();
                if (!x.equals(que.getAnswers()))
                    btn[arr[i0]].setText(x);
                else
                    btn[arr[i0]].setText(ans.get(generate(51)).getAnswer());
            }

            btn[arr[3]].setText(que.getAnswers());

            speak();

        });

        dbhelper = new QuestionHelper(context);
        dbhelper.prepareDatabase();
        que = dbhelper.getQuestion(generate(51));
        ans = dbhelper.get_answer();
        arr = shuffle(intArray);
        txt.setText(que.getQuestions());


        for (int i0 = 0; i0 < 3; i0++) {
            String x = ans.get(generate(51)).getAnswer();
            if (!x.equals(que.getAnswers()))
                btn[arr[i0]].setText(x);
            else
                btn[arr[i0]].setText(ans.get(generate(51)).getAnswer());
        }

        btn[arr[3]].setText(que.getAnswers());


        btn[0].setOnClickListener(v -> {

            if (arr[3] == 0) {
                //Toast.makeText(context,"Correct  " +que.getMeaning(), Toast.LENGTH_LONG).show();
                if (bool) {
                    int i1, j;
                    i1 = que.getRight() + 1;
                    j = que.getWrong();
                    dbhelper.questionUpdate(i1, j, que.getId());
                }

                /*prog++;
                pw.incrementProgress(prog * 20);
                pw.setText(prog + "/5");*/
                if (prog >= 5)
                    context.stopService(new Intent(getContext(), ServiceOverlay.class));
                else {
                    skip.setText("Next");
                    skip.callOnClick();
                }
            } else {
                if (bool) {
                    bool = false;
                    int i1, j;
                    i1 = que.getRight();
                    j = que.getWrong() + 1;
                    dbhelper.questionUpdate(i1, j, que.getId());
                    //Toast.makeText(context, que.getAnswers(), Toast.LENGTH_LONG).show();
                    btn[0].setTextColor(Color.RED);
                    btn[arr[3]].setTextColor(Color.GREEN);
                }
                skip.setText("Next");
            }
        });
        btn[1].setOnClickListener(v -> {
            if (arr[3] == 1) {
                //Toast.makeText(context,"Correct  " +que.getMeaning(), Toast.LENGTH_LONG).show();
                if (bool) {
                    int i12, j;
                    i12 = que.getRight() + 1;
                    j = que.getWrong();
                    dbhelper.questionUpdate(i12, j, que.getId());
                }

                if (prog >= 5)
                    context.stopService(new Intent(getContext(), ServiceOverlay.class));
                else {
                    skip.setText("Next");
                    skip.callOnClick();
                }
            } else {
                if (bool) {
                    bool = false;
                    int i12, j;
                    i12 = que.getRight();
                    j = que.getWrong() + 1;
                    dbhelper.questionUpdate(i12, j, que.getId());
                    //Toast.makeText(context, que.getAnswers(), Toast.LENGTH_LONG).show();
                    btn[1].setTextColor(Color.RED);
                    btn[arr[3]].setTextColor(Color.GREEN);
                }
                skip.setText("Next");
            }
        });
        btn[2].setOnClickListener(v -> {
            if (arr[3] == 2) {
                //Toast.makeText(context,"Correct  " +que.getMeaning(), Toast.LENGTH_LONG).show();
                if (bool) {
                    int i13, j;
                    i13 = que.getRight() + 1;
                    j = que.getWrong();
                    dbhelper.questionUpdate(i13, j, que.getId());
                }

                if (prog >= 5)
                    context.stopService(new Intent(getContext(), ServiceOverlay.class));
                else {
                    skip.setText("Next");
                    skip.callOnClick();
                }
            } else {
                if (bool) {
                    bool = false;
                    int i13, j;
                    i13 = que.getRight();
                    j = que.getWrong() + 1;
                    dbhelper.questionUpdate(i13, j, que.getId());
                    //Toast.makeText(context, que.getAnswers(), Toast.LENGTH_LONG).show();
                    btn[2].setTextColor(Color.RED);
                    btn[arr[3]].setTextColor(Color.GREEN);
                }
                skip.setText("Next");
            }
        });
        btn[3].setOnClickListener(v -> {
            if (arr[3] == 3) {
                //Toast.makeText(context,"Correct  "+que.getMeaning(), Toast.LENGTH_LONG).show();
                if (bool) {
                    int i14, j;
                    i14 = que.getRight() + 1;
                    j = que.getWrong();
                    dbhelper.questionUpdate(i14, j, que.getId());
                }

                if (prog >= 5)
                    context.stopService(new Intent(getContext(), ServiceOverlay.class));
                else {
                    skip.setText("Next");
                    skip.callOnClick();
                }
            } else {
                //Toast.makeText(context, que.getAnswers(), Toast.LENGTH_LONG).show();
                if (bool) {
                    bool = false;
                    int i14, j;
                    i14 = que.getRight();
                    j = que.getWrong() + 1;
                    dbhelper.questionUpdate(i14, j, que.getId());
                    btn[3].setTextColor(Color.RED);
                    btn[arr[3]].setTextColor(Color.GREEN);
                }
                skip.setText("Next");
            }
        });

        Button vic = view.findViewById(R.id.voice);

        vic.setOnClickListener(v -> {

            speak();

        });

        this.addView(view);
    }


    void speak(){
        t1.speak("Please provide the answer to the Question that Follows as", TextToSpeech.QUEUE_ADD, null);
        t1.playSilence(500, TextToSpeech.QUEUE_ADD, null);

        String xyz = txt.getText().toString().replaceFirst("_","Dash");
        xyz = xyz.replace("_","");

        t1.speak(xyz, TextToSpeech.QUEUE_ADD,null);



        t1.playSilence(500, TextToSpeech.QUEUE_ADD, null);

        t1.speak("Option one", TextToSpeech.QUEUE_ADD, null);
        t1.playSilence(500, TextToSpeech.QUEUE_ADD, null);

        t1.speak(btn[0].getText().toString(), TextToSpeech.QUEUE_ADD, null);
        t1.playSilence(500, TextToSpeech.QUEUE_ADD, null);


        t1.speak("Option Two", TextToSpeech.QUEUE_ADD, null);
        t1.playSilence(500, TextToSpeech.QUEUE_ADD, null);
        t1.speak(btn[1].getText().toString(), TextToSpeech.QUEUE_ADD, null);
        t1.playSilence(500, TextToSpeech.QUEUE_ADD, null);

        t1.speak("Option Three", TextToSpeech.QUEUE_ADD, null);
        t1.playSilence(500, TextToSpeech.QUEUE_ADD, null);
        t1.speak(btn[2].getText().toString(), TextToSpeech.QUEUE_ADD, null);
        t1.playSilence(500, TextToSpeech.QUEUE_ADD, null);

        t1.speak("Option Four", TextToSpeech.QUEUE_ADD, null);
        t1.playSilence(500, TextToSpeech.QUEUE_ADD, null);
        t1.speak(btn[3].getText().toString(), TextToSpeech.QUEUE_ADD, null);
    }

}
