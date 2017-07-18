package com.example.leejohn.xposedwechatrandomgame;

/**
 * Created by leejohn on 2017/7/19.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener
{
    private TextView tvDiceNum;
    private TextView tvMorraNum;
    private Button btSetDice;
    private Button btSetMorra;
    private int diceNum = 0; // 默认点数1
    private int morraNum = 0; // 默认剪刀

    String[] morraStr = { "剪刀", "石头", "布" };


    SharedPreferences.Editor edit ;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setView();
        setListener();
        setValue();
    }

    private void setView(){
        tvDiceNum = (TextView) findViewById(R.id.tv_dice_num);
        btSetDice = (Button) findViewById(R.id.bt_set_dice);

        tvMorraNum = (TextView) findViewById(R.id.tv_morra_num);
        btSetMorra = (Button) findViewById(R.id.bt_set_morra);
    }

    private void setValue(){
        tvDiceNum.setText("点数" + (diceNum+1));
        tvMorraNum.setText(morraStr[morraNum]);

        SharedPreferences sp = getSharedPreferences("com.example.leejohn.xposedwechatrandomgame_preferences", Activity.MODE_WORLD_READABLE);
        edit = sp.edit();
    }



    private void setListener(){
        btSetDice.setOnClickListener(this);
        btSetMorra.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_set_dice: // 设置骰子点数
                showSetDiceDialog();
                break;

            case R.id.bt_set_morra: // 设置猜拳
                showSetMorraDialog();
                break;

        }
    }

    private void showSetMorraDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);

        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合 第二个参数代表索引，指定默认哪一个单选框被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(morraStr, morraNum, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                morraNum = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                saveMorraNum();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                morraNum = 0;
            }
        });

        builder.show();
    }

    private void showSetDiceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        final String[] diceStr = { "点数1", "点数2", "点数3", "点数4", "点数5", "点数6" };

        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合 第二个参数代表索引，指定默认哪一个单选框被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(diceStr, diceNum, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                diceNum = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                saveDiceNum();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                diceNum = 6;
            }
        });

        builder.show();
    }

    protected void saveDiceNum(){
        edit.putInt("dice_num",diceNum);
        edit.apply();
        Log.i("Xposed","save 点击 ： " + diceNum);
        tvDiceNum.setText("点数" + (diceNum + 1));
    }

    protected void saveMorraNum(){
        edit.putInt("morra_num",morraNum);
        edit.apply();
        Log.i("Xposed","save 点击 ： " + morraNum);

        tvMorraNum.setText(morraStr[morraNum]);
    }
}