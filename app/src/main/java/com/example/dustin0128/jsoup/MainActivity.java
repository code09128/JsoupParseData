package com.example.dustin0128.jsoup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private TextView tv1,tv2,tv3;
    private VersionAPI versionAPI = new VersionAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        iv = findViewById(R.id.imageView);

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                getContent();
//            }
//        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                getContent();
            }
        }).start();
    }



    private void getContent() {
        try {
//            Document doc = Jsoup.connect("http://home.meishichina.com/show-top-type-recipe.html").get();
            final Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=tw.com.omnihealthgroup.register")
                    .timeout(3 * 1000)
                    // UserAgent是發送給服務器的當前瀏覽器的信息。
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com") //引用網站
                    .get();

            Log.e("message","title ="+doc.title());


                //APP標題
                Elements ele = doc.select("h1.AHFaub"); //獲取h1.AHFaub的class
                final String details = ele.select("span").text();  //獲取span本文內容
                Log.e("message","_title = "+details);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(details != null){
                            tv1.setText(details);
                        }else{
                            tv1.setText("xxx");
                        }
                    }
                });

                //APP版本
                Elements ele1 = doc.select("span.htlgb");   //獲取span.htlgb的class
                final String vis = ele1.get(6).text();    //獲取span地6項本文內容
                Log.e("message","_vision = "+vis);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        tv2.setText(vis);
                    }
                });

                //說明內容
                Elements ele2 = doc.select("div.W4P4ne");   //獲取div.DWPxHb的class
                final String vis2 = ele2.select("meta").attr("content"); //獲取mata ,attr(獲取)content屬性內的文字
                Log.e("message","_description = "+vis2);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        tv3.setText(vis2);
                    }
                });

                //圖片路徑
                Elements ele3 = doc.select("div.dQrBL"); //獲取div.dQrBL的class
                final String pic = ele3.select("img").attr("src"); //獲取img圖片 src來源
                Log.e("message","_pic = "+pic);

                final Bitmap bitmap = getBitmap(pic); //宣告一個圖片來源 代的參數為圖片網址
//                //BASE64編碼============================================================================
//                //轉換字串 把其他地方的讀入的數據寫到這裡面，最後獲取數據所有的 byte[]
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                // Base64解碼 encodeToString 則直接將返回String類型的加密數據
                String icon = Base64.encodeToString(byteArray,Base64.DEFAULT);

                Log.e("message","_icon = "+icon);
//                //============================================================================
//                byte[] bitmapByte;
//                bitmapByte = Base64.decode(icon, Base64.DEFAULT);
//                final Bitmap bitmaps = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
//
//                Log.e("message","_bitmaps = "+bitmaps);

                //接著用setImageBitmap來顯示圖片 由於要跨thread所以要將Bitmap設成final形式
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
////                        if(bitmap != null){
//                            iv.setImageBitmap(bitmap);
////                        }else {
////                            iv.setImageResource(R.mipmap.ic_launcher);
////                        }
//                    }
//                });

//            "http://tp3-pp001.24drs.com/appinfo/GetAppPic.ashx?t=s&a=tw.com.omnihealthgroup.nursing"
            URL url = new URL(pic);
            final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            Log.e("message", "_bmp = " + bmp);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(bmp);
                }
            });

//            Elements ele1 = doc.select("div.DWPxHb");
//            String title = ele1.select("a").attr("title");
//            Log.e("message","eat_title = "+ele1);

//            Elements ele = doc.select("div.pic");
//            String title = ele.select("a").attr("title");
//            Log.e("message","eat_title = "+title);

//            String pic = ele.get(2).select("a").select("img").attr("data-src");
//            Log.e("message","pic = "+pic);
//
//            Elements deta = doc.select("div.detail");
//            String detaUrl = deta.get(1).select("a").attr("href");
//            Log.e("message","detaUrl = "+detaUrl);
//
//            Elements txt = doc.select("p.subcontent");
//            String txtStr = txt.get(2).text();
//            Log.e("message","txtStr = "+txtStr);

        } catch (IOException e) {
            Log.i("LKing", e.toString());
        }

    }

    //設定一個methid, 回傳Bitmap的值, 帶入的參數是String型態的網址
    public static Bitmap getBitmap(String src){
        try {
            //接著設定URL來接String型態的網址
            URL url = new URL(src);
            //在來設定連練並執行, 這邊使用的是HttpURLConnection,
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.connect();

            //最後將得到的值, 用BitmapFactory轉成Bitmap檔
            InputStream input = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
