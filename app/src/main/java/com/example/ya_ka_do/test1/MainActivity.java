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
 * �������́iInput�j�Ɖ����ǂݏグ�iOutput�j�̃e�X�g�B
 * �}�C�N�ɓ�����������F�����āC���̂܂܉����������C�����ޕԂ��ɃX�s�[�J�o�͂����݂�B
 * @author id:language_and_engineering
 *
 */
public class MainActivity extends Activity implements OnClickListener, TextToSpeech.OnInitListener
{
    // �_�~�[�̎��ʎq
    private static final int REQUEST_CODE = 0;

    // ���������p
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
            // "android.speech.action.RECOGNIZE_SPEECH" �������ɃC���e���g�쐬
            Intent intent = new Intent(
                    RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            // �u���b�����������v�̉�ʂŕ\������镶����
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "�����F�����ł�");

            // �������͊J�n
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // ��Ή��̏ꍇ
            Toast.makeText(this, "�������͂ɔ�Ή��ł��B", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // �C���e���g�̔��s��������
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            // �������͂̌��ʂ̍ŏ�ʂ݂̂��擾
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String s = results.get(0);

            // �\��
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();

            // �����������Ĕ���
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
            // ���������̐ݒ���s��

            float pitch = 1.0f; // ���̍���
            float rate = 1.0f; // �b���X�s�[�h
            Locale locale = Locale.US; // �Ώی���̃��P�[��
            // �����P�[���̈ꗗ�\
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
            // �j��
            tts.shutdown();
        }
    }

}
