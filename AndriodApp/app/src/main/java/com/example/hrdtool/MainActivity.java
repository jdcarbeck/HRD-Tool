package com.example.hrdtool;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Base64.*;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;
import com.google.android.material.tabs.TabLayout;
import com.opencsv.CSVReader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static com.example.hrdtool.CSVForm.AREA;
import static com.example.hrdtool.CSVForm.CITY;
import static com.example.hrdtool.CSVForm.COUNTRY;
import static com.example.hrdtool.CSVForm.TYPES;

import static com.example.hrdtool.CSVHelp.find_user_zone_type;
import static com.example.hrdtool.CSVHelp.index;
import static com.example.hrdtool.CSVHelp.length;
import static com.example.hrdtool.CSVHelp.users;


public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;

    private static final String TAG = "MainActivity";
    PersistableBundle bundle = new PersistableBundle();
    int jobId = 1;

    protected static PublicKey receivedPublicKey;
    protected static SecretKey mySecretKey;
    protected static byte[] iv;
    protected PublicKey myPublicKey;
    protected PrivateKey myPrivateKey;

    public static void generateSymmetricKey()
    {
        KeyGenerator generator;
        try {
            generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            mySecretKey = generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public byte[] encryptSecretKey()
    {
        byte[] encryptedKey = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(3072);
            KeyPair keyPair = generator.generateKeyPair();
            myPublicKey = keyPair.getPublic();
            myPrivateKey = keyPair.getPrivate();            
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.PUBLIC_KEY, receivedPublicKey);
            System.out.println("cipher public key" + Cipher.PUBLIC_KEY);
            encryptedKey = cipher.doFinal(mySecretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encryptedKey;
    }
    public byte[] encryptPayload(String secretKey, String iv, String encryptedText)
    {
        byte[] encryptedPayload = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(3072);
            KeyPair keyPair = generator.generateKeyPair();
            myPublicKey = keyPair.getPublic();
            myPrivateKey = keyPair.getPrivate();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding ");
            cipher.init(Cipher.PUBLIC_KEY, receivedPublicKey);
            JSONObject payload = new JSONObject();
            payload.put("key", secretKey);
            payload.put("iv", iv);
            payload.put("data", encryptedText);
            String payloadStr = payload.toString();
            byte[] payloadBytes = payloadStr.getBytes();
            encryptedPayload = cipher.doFinal(payloadBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException |
                 NoSuchPaddingException | IllegalBlockSizeException |
                BadPaddingException | JSONException e){
            e.printStackTrace();
        }
        return encryptedPayload;
    }

    public byte[] encryptText(String textToEncrypt)
    {
        byte[] byteCipherText = null;
        try {
            Cipher encCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encCipher.init(Cipher.ENCRYPT_MODE, mySecretKey);
            AlgorithmParameters params = encCipher.getParameters();
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byteCipherText = encCipher.doFinal(textToEncrypt.getBytes());
        } catch (NoSuchAlgorithmException | InvalidParameterSpecException
                | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
        }
        return byteCipherText;
    }
//    public byte[] decryptSecretKey(byte[] encryptedSecretKey)         //for testing decryption
//    {
//        byte[] decryptedKey = null;
//        try {
//            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
//            cipher.init(Cipher.PRIVATE_KEY, myPrivateKey);
//            decryptedKey = cipher.doFinal(encryptedSecretKey);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        }
//        return decryptedKey;
//    }
//    public String decryptText(byte[] decryptedKey, byte[] encryptedText)      //for testing decryption
//    {
//        String decryptedPlainText = null;
//        try {
//            SecretKey originalKey = new SecretKeySpec(decryptedKey , 0, decryptedKey.length, "AES");
//            Cipher aesCipher2 = Cipher.getInstance("AES");
//            aesCipher2.init(Cipher.DECRYPT_MODE, originalKey);
//            byte[] bytePlainText = aesCipher2.doFinal(encryptedText);
//            decryptedPlainText = new String(bytePlainText);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        }
//        return decryptedPlainText;
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout mTabLayout = findViewById(R.id.tab_layout);
        ViewPager mViewPager = findViewById(R.id.pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        scheduleJobGetPublicKey();
    }

    //This method is called by OkHttp when a a public key is recevied as a response
    public static void setPublicKey(String publicKeyResponse) {
        try {
            byte[] publicBytes = Base64.decode(publicKeyResponse, Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            System.out.println(pubKey);
            receivedPublicKey = pubKey;
            System.out.println("public key received");
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        catch(InvalidKeySpecException e){
            e.printStackTrace();
        }

    }
    public void scheduleJobGetPublicKey() {
        ComponentName componentName = new ComponentName(this, DataSendingService.class);
        bundle.putString("route", "key");
        bundle.putString("encryptedPayload", "");
        bundle.putString("encryptedFormData", "");
        JobInfo info = new JobInfo.Builder(jobId, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setExtras(bundle)
                .build();
        jobId += 1;
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }


    }
    public void scheduleJob(JSONObject rcvJSON) {

        ComponentName componentName = new ComponentName(this, DataSendingService.class);

        JSONObject formJson = rcvJSON;
        String android_id = Secure.getString(MainActivity.this.getContentResolver(),
                Secure.ANDROID_ID);
        try {
            formJson.put("id", android_id);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        String jsonString = formJson.toString();

        generateSymmetricKey();

        byte[] encryptedText = encryptText(jsonString);
        String strEncodedSecretKey  = Base64.encodeToString(mySecretKey.getEncoded(), Base64.NO_WRAP);
        String strEncodedIv = Base64.encodeToString(iv, Base64.NO_WRAP);
        String strEncodedJson = Base64.encodeToString(encryptedText, Base64.NO_WRAP);

        byte[] encryptedPayload = encryptPayload(strEncodedSecretKey, strEncodedIv, strEncodedJson);


//        byte[] decryptedSecretKey = decryptSecretKey(encryptedSecretKey);
//        System.out.println(decryptedSecretKey);
//        String decryptedText = decryptText(decryptedSecretKey, encryptedText);
//        System.out.println(decryptedText);
        try {
            String encryptedPayloadStr = Base64.encodeToString(encryptedPayload, Base64.NO_WRAP);

            System.out.println(encryptedPayloadStr);

            bundle.putString("route", "api/form/");
            bundle.putString("encryptedPayload", encryptedPayloadStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JobInfo info = new JobInfo.Builder(jobId, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setExtras(bundle)
                .build();
        jobId += 1;
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        private int[] images = new int[]{R.drawable.ic_content_paste_black_24dp, R.drawable.ic_people_outline_black_24dp};
        private String[] titles = new String[]{"Form","Help Info"};

        public MyPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public int getCount(){ return NUM_ITEMS; }

        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    return FormFragment.newInstance(0, titles[0]);
                case 1:
                    return HelpFragment.newInstance(1,titles[1]);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            Drawable image = getDrawable(images[position]);
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            SpannableString sb = new SpannableString("" + titles[position]);
//            ImageSpan imageSpan = new ImageSpan(image);
//            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;
            return  titles[position];
        }
    }

    public void getHelp(CSVReader reader, String type, String area) {
        try {
            if(type.equalsIgnoreCase(
                    TYPES[0]))
                type = TYPES[1];
            if(area.equalsIgnoreCase(AREA[0]))
                area = AREA[1];

            users = find_user_zone_type(reader, area, type);
            length = users.size();

            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_prev);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);

            prev.setEnabled(false);
            if(users.size() <= 1)
                next.setEnabled(false);
            else
                next.setEnabled(true);

            if(users.isEmpty()) {
                TextView support_name = (TextView) findViewById(R.id.support_name);
                support_name.setText(R.string.default_support);
                TextView support_address = (TextView) findViewById(R.id.support_address);
                support_address.setText(null);
                TextView support_telephone = (TextView) findViewById(R.id.support_telephone);
                support_telephone.setText(null);
                TextView support_type = (TextView) findViewById(R.id.support_type);
                support_type.setText(null);
                TextView support_email = (TextView) findViewById(R.id.support_email);
                support_email.setText(null);
            }
            else {
                CSVHelp.CSVHelpRow user = users.get(index-1);
                this.display_CSVRow(user);
                String ratio = index+"/"+length;
                page_ratio.setText(ratio);
            }
        } catch  (Exception e) {
            e.printStackTrace();

        }
    }
    private void display_CSVRow(CSVHelp.CSVHelpRow user) {

        Log.d("DEBUG", "Inside the display function");
        TextView support_name = (TextView) findViewById(R.id.support_name);
        support_name.setText(user.getName());
        TextView support_type = (TextView) findViewById(R.id.support_type);
        support_type.setText(user.getType());
        TextView support_address = (TextView) findViewById(R.id.support_address);
        support_address.setText(user.getCompleteAddress());

        // Make the address a link
        Pattern pattern = Pattern.compile(".*", Pattern.DOTALL);
        Linkify.addLinks(support_address, pattern, "geo:0,0?q=");
        support_address.setMovementMethod(LinkMovementMethod.getInstance());

        TextView support_telephone = (TextView) findViewById(R.id.support_telephone);
        support_telephone.setText(user.getCleanTelephone());

        // Make the telephone number a phone link
        Linkify.addLinks(support_telephone, Patterns.PHONE,"tel:",Linkify.sPhoneNumberMatchFilter,Linkify.sPhoneNumberTransformFilter);
        support_telephone.setMovementMethod(LinkMovementMethod.getInstance());

        TextView support_email = (TextView) findViewById(R.id.support_email);
        support_email.setText(user.getEmail());
    }
    public void nextUser(View view) {
        if(index >= length) {
            index = length;
            return;
        }
        else {
            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_prev);
            index++;
            this.display_CSVRow(users.get(index-1));
            if(index == length)
                next.setEnabled(false);
            prev.setEnabled(true);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);
            String ratio = index + "/" + length;
            page_ratio.setText(ratio);

        }
        return;
    }
    public void previousUser(View view) {
        if(index <= 1) {
            index = 1;
            return;
        } else {
            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_prev);
            index--;
            this.display_CSVRow(users.get(index-1));
            if(index == 1)
                prev.setEnabled(false);
            next.setEnabled(true);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);
            String ratio = index + "/" + length;
            page_ratio.setText(ratio);
        }
    }

}



