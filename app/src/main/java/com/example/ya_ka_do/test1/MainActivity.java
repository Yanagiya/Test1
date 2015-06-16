package com.example.ya_ka_do.test1;

import android.os.Bundle;
import android.app.Activity;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * oons
 */
/**
 * 音声入力（Input）と音声読み上げ（Output）のテスト。
 * マイクに入った音声を認識して，そのまま音声合成し，おうむ返しにスピーカ出力を試みる。
 * @author id:language_and_engineering
 *
 */
public class MainActivity extends Activity implements OnClickListener, TextToSpeech.OnInitListener
{
    // ダミーの識別子
    private static final int REQUEST_CODE = 0;

    // 音声合成用
    TextToSpeech tts = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener( this );

        tts = new TextToSpeech(this, this);
    }


    @Override
    public void onClick(View v)
    {
        try {
            // "android.speech.action.RECOGNIZE_SPEECH" を引数にインテント作成
            Intent intent = new Intent(
                    RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            // 「お話しください」の画面で表示される文字列
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声認識中です");

            // 音声入力開始
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // 非対応の場合
            Toast.makeText(this, "音声入力に非対応です。", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // インテントの発行元を限定
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            // 音声入力の結果の最上位のみを取得
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String s = results.get(0);

            // 表示
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();

            // 音声合成して発音
            if(tts.isSpeaking()) {
                tts.stop();
            }
            tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            // 音声合成の設定を行う

            float pitch = 1.0f; // 音の高低
            float rate = 1.0f; // 話すスピード
            Locale locale = Locale.US; // 対象言語のロケール
            // ※ロケールの一覧表
            //   http://docs.oracle.com/javase/jp/1.5.0/api/java/util/Locale.html

            tts.setPitch(pitch);
            tts.setSpeechRate(rate);
            tts.setLanguage(locale);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( tts != null )
        {
            // 破棄
            tts.shutdown();
        }
    }

}
