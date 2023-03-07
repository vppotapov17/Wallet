package com.example.wallet.screens.Wallets;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.R;
import com.example.wallet.models.Wallet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class WalletsViewModel extends ViewModel {

    private static final String CBR_BASE_URL = "https://cbr.ru/scripts/XML_daily.asp";
    private static final String USD_ID = "R01235";
    private static final String EUR_ID = "R01239";
    private static final String CNY_ID = "R01375";
    private static final String BYN_ID = "R01090B";
    private static final String KZT_ID = "R01335";
    private static final String UAH_ID = "R01720";
    private static final String GBP_ID = "R01035";
    private static final String CHF_ID = "R01775";

    // названия валют и их ID
    private HashMap<String, String> walletNames;

    private SimpleDateFormat dateFormat;

    // список валют
    private MutableLiveData<List<Wallet>> walletListLiveData;
    private List<Wallet> walletList;

    public WalletsViewModel(SharedPreferences preferences){
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.walletList = new ArrayList<>();
        this.walletListLiveData = new MutableLiveData<>();

        walletNames = new HashMap<>();

        walletNames.put(BYN_ID, "Бел. рубль (BYN)");
        walletNames.put(KZT_ID, "Тенге (KZT)");
        walletNames.put(UAH_ID, "Гривна (UAH)");
        walletNames.put(USD_ID, "Доллар (USD)");
        walletNames.put(CHF_ID, "Франк (CHF)");
        walletNames.put(EUR_ID, "Евро (EUR)");
        walletNames.put(CNY_ID, "Юань (CNY)");
        walletNames.put(GBP_ID, "Фунт ст. (GBP)");

        getData();

    }

    void getData(){


        Completable.fromRunnable(()->{
            try {
                HashMap<String, HashMap<String, String>> valuesForDates = new HashMap<>();
                GregorianCalendar calendar = new GregorianCalendar();

                while (valuesForDates.size() < 3){
                    List<Object> resultForDate = getValuesForDate(dateFormat.format(calendar.getTime()));
                    valuesForDates.put((String)resultForDate.get(0), (HashMap<String, String>) resultForDate.get(1));
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }

                Wallet usd = new Wallet(R.drawable.usd, walletNames.get(USD_ID));
                Wallet eur = new Wallet(R.drawable.eur, walletNames.get(EUR_ID));
                Wallet uah = new Wallet(R.drawable.uah, walletNames.get(UAH_ID));
                Wallet cny = new Wallet(R.drawable.cny, walletNames.get(CNY_ID));
                Wallet byn = new Wallet(R.drawable.byn, walletNames.get(BYN_ID));
                Wallet kzt = new Wallet(R.drawable.kzt, walletNames.get(KZT_ID));
                Wallet gbp = new Wallet(R.drawable.gbp, walletNames.get(GBP_ID));
                Wallet chf = new Wallet(R.drawable.chf, walletNames.get(CHF_ID));

                valuesForDates.forEach((key, value) -> {
                    usd.addValue(Double.parseDouble(value.get(USD_ID)), key);
                    eur.addValue(Double.parseDouble(value.get(EUR_ID)), key);
                    cny.addValue(Double.parseDouble(value.get(CNY_ID)), key);
                    byn.addValue(Double.parseDouble(value.get(BYN_ID)), key);
                    gbp.addValue(Double.parseDouble(value.get(GBP_ID)), key);
                    chf.addValue(Double.parseDouble(value.get(CHF_ID)), key);

                    // UAH
                    String uahValueString = value.get(UAH_ID);
                    Double uahValue = Double.parseDouble(uahValueString);
                    uahValue *= 1000;
                    uahValue = (double) Math.round(uahValue) / 10000;

                    uah.addValue(Double.parseDouble(uahValue.toString()), key);

                    // KZT
                    String kztValueString = value.get(KZT_ID);
                    Double kztValue = Double.parseDouble(kztValueString);
                    kztValue *= 100;
                    kztValue = (double) Math.round(kztValue) / 10000;

                    kzt.addValue(Double.parseDouble(kztValue.toString()), key);
                });


                walletList.add(usd);
                walletList.add(eur);
                walletList.add(cny);
                walletList.add(kzt);
                walletList.add(byn);
                walletList.add(uah);
                walletList.add(gbp);
                walletList.add(chf);
            }
            catch (Exception e){
                walletListLiveData.postValue(null);
            }
        }).subscribeOn(Schedulers.io()).subscribe(() -> {
            walletListLiveData.postValue(walletList);
        });


    }

    private List<Object> getValuesForDate(String date){

        List<Object> resultList = new ArrayList<>();
        HashMap<String, String> resultValues = new HashMap<>();
        String resultRealDate = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            URL url = new URL(CBR_BASE_URL + "?date_req=" + date);
            parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));


            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {

                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("ValCurs")){
                        resultRealDate = parser.getAttributeValue(0).replace(".", "/");
                    }
                    if (parser.getName().equals("Valute")) {
                        String ID = parser.getAttributeValue(0);
                        if (walletNames.containsKey(ID)){
                            while (true) {
                                if (parser.getEventType() == XmlPullParser.START_TAG) {
                                    if (parser.getName().equals("Value")) {
                                        parser.next();
                                        resultValues.put(ID, parser.getText().replace(",", "."));
                                    }
                                }
                                if (parser.getEventType() == XmlPullParser.END_TAG) {
                                    if (parser.getName().equals("Valute")) break;
                                }
                                parser.next();
                            }
                        }
                    }
                }

                parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.d("AAA", e.getMessage());
            return null;
        } catch (Exception e){
            return null;
        }

        resultList.add(0, resultRealDate);
        resultList.add(1, resultValues);

        return resultList;
    }

    public LiveData<List<Wallet>> getWalletListLiveData(){
        return walletListLiveData;
    }

}
