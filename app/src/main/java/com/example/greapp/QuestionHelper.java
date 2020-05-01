package com.example.greapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QuestionHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "questionDB.db";
    private static final int DATABASE_VERSION = 1;
    //private static String path= "data/data"+ BuildConfig.APPLICATION_ID +"/databases/";
    Context mycontext;
    String pathToSaveDBFile;
    QuestionHelper(Context context)
    {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        mycontext=context;
        pathToSaveDBFile= context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
        Log.e("lol",pathToSaveDBFile);
    }
    /*QuestionHelper (Context context,String filePath)
    {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        mycontext=context;
        pathToSaveDBFile = new StringBuffer(filePath).append("/").append(DATABASE_NAME).toString();
        Log.e("lol",pathToSaveDBFile);
    }*/
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(pathToSaveDBFile);
            checkDB = file.exists();
        } catch(SQLiteException e) {
            Log.d("TAG", e.getMessage());
        }
        return checkDB;
    }
    public void prepareDatabase(){
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            ////
            installDatabase();
        }

    }

    private void installDatabase()
    {
        try
        {
            InputStream input = mycontext.getAssets().open(DATABASE_NAME);
            OutputStream output = new FileOutputStream(pathToSaveDBFile);

            byte [] buffer = new byte[100];
            int length;

            while ((length=input.read(buffer))>0)
            {
                output.write(buffer,0,length);
            }
            output.flush();
            output.close();
            input.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Question getQuestion(int id)
    {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);


        String query= "select q.question, q.answer, q.right, q.wrong, m.meanings  from questions q, meaning m where q.id="+id+" and q.answer=m.answer";

        Cursor cursor =db.rawQuery(query,null);

        Question que = new Question();
        while (cursor.moveToNext())
        {

            que.setId(id);
            que.setQuestions(cursor.getString(0));
            que.setAnswers(cursor.getString(1));
            que.setRight(cursor.getInt(2));
            que.setWrong(cursor.getInt(3));
            que.setMeaning(cursor.getString(4));


        }

        return que;

    }

    public void  questionUpdate(int right , int wrong, int id)
    {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);

        String query = "Update questions SET right="+right+", wrong ="+wrong+" where id ="+id+";";

        db.execSQL(query);

    }

    public List<Answer> get_answer()
    {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query= "Select id, answer, meanings from meaning";

        Cursor cr = db.rawQuery(query, null);
        List<Answer> list = new ArrayList<Answer>();
        while (cr.moveToNext())
        {
            Answer ans = new Answer();
            ans.setId(cr.getInt(0));
            ans.setAnswer(cr.getString(1));
            ans.setMeaning(cr.getString(2));

            list.add(ans);
        }
        return list;
    }

    public Map<String,String> getRatio()
    {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null,SQLiteDatabase.OPEN_READONLY);

        String query = "select id,right,wrong from questions";

        Cursor cr = db.rawQuery(query,null);

        HashMap<String, String> map =new HashMap<>();

        while(cr.moveToNext())
        {
            Integer obj = new Integer(cr.getInt(0));
            map.put(obj.toString(),cr.getInt(1)+" "+cr.getInt(2));
        }

        return map;
    }


}
